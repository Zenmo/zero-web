import {BBox2d} from '@turf/helpers/dist/js/lib/geojson'
import {MultiPolygon} from 'geojson'
import {LatLngBounds} from 'leaflet'
import {PostcodeKleinverbruik} from './types'

type ResponseBody = {
    type: 'FeatureCollection',
    features: EnexisPostcodeKleinverbruikFeature[],
    totalFeatures: number,
    numberMatched: number,
    numberReturned: number,
    timestamp: string,
    crs: {
        type: 'name',
        properties: {
            name: 'urn:ogc:def:crs:EPSG::4326',
        }
    }
    bbox: BBox2d,
}

export type EnexisPostcodeKleinverbruikFeature = {
    type: 'Feature',
    id: string, // example: postcode_kleinverbruik_g.210853
    geometry: MultiPolygon,
    geometry_name: 'geom',
    properties: EnexisPostcodeKleinverbruikProperties
}

// https://opendata.enexis.nl/geoserver/wfs?request=DescribeFeatureType&typeName=postcode_kleinverbruik_e
export type EnexisPostcodeKleinverbruikProperties = {
    fid: number,
    netbeheerder: string
    netgebied: string
    straatnaam: string
    postcodevan: string // 1234 AB (here with space)
    postcodetot: string
    woonplaats: string
    landcode: string
    productsoort: string
    verbruikssegment: string
    aansluitingenaantal: number
    leveringsrichtingperc: number
    fysiekestatusperc: number
    soortaansluitingperc: number
    soortaansluiting: string
    sjvgemiddeld: number
    sjvlaagtariefperc: number
    slimmemeterperc: number
}

// Enexis can be queried by postal code like we do for the other providers.
// But querying by bounding box is faster.
export const fetchEnexisKleinverbruik = async (boundingBox: LatLngBounds): Promise<PostcodeKleinverbruik[]> => {
    // the full dataset is 125.516 features
    const MAX = 1000

    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'postcode_kleinverbruik_e,postcode_kleinverbruik_g',
        count: MAX.toString(),
        srsName: 'EPSG:4326', // output coordinate system
        outputFormat: 'json',
        bbox: [
            boundingBox.getWest(),
            boundingBox.getSouth(),
            boundingBox.getEast(),
            boundingBox.getNorth(),
            'EPSG:4326', // input coordinate system
        ].join(','),
    })

    const url = 'https://opendata.enexis.nl/geoserver/wfs?' + params.toString()

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting Enexis kleinverbruik data')
    }

    const json = await response.json() as ResponseBody
    if (json.numberMatched > MAX) {
        throw new Error('Maximum aantal Enexis postcodes overschreden')
    }

    return toSharedFormat(json.features)
}

// Enexis returns everything lowercase.
// This conversion exists to match the other providers.
const camelCasedProperties: (keyof PostcodeKleinverbruik)[] = [
    'postcodeVan',
    'postcodeTot',
    'aansluitingenAantal',
    'leveringsrichtingPerc',
    'fysiekeStatusPerc',
    'soortAansluitingPerc',
    'soortAansluiting',
    'sjvGemiddeld',
    'sjvLaagTariefPerc',
    'slimmeMeterPerc',
]

const getNewKey = (key: string): keyof PostcodeKleinverbruik => {
    const camelCasedKey = camelCasedProperties.find(p => p.toLowerCase() === key)
    if (camelCasedKey) {
        return camelCasedKey
    } else {
        // it's a correctly lowercased property or a property we don't know about
        return key as keyof PostcodeKleinverbruik
    }
}

const toSharedFormat = (properties: EnexisPostcodeKleinverbruikFeature[]): PostcodeKleinverbruik[] => {
    return properties.map((p) => {
        const partial: Partial<PostcodeKleinverbruik> = {}
        for (const [key, value] of Object.entries(p.properties)) {
            const newKey = getNewKey(key)
            // @ts-ignore
            partial[newKey] = value
        }

        const postcodeKleinverbruik = partial as PostcodeKleinverbruik

        postcodeKleinverbruik.postcodeTot = postcodeKleinverbruik.postcodeTot.replace(' ', '')
        postcodeKleinverbruik.postcodeVan = postcodeKleinverbruik.postcodeVan.replace(' ', '')

        return postcodeKleinverbruik
    })
}
