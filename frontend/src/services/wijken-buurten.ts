import {MultiPolygon} from 'geojson'
import {LatLng} from 'leaflet'
import {BoundingBox} from './bag2d'
import center from '@turf/center'
import {geoJsonPositionToLeaflet} from './util'

export async function fetchGemeenteList(): Promise<string[]> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'gemeenten',
        service: 'WFS',
        version: '2.0.0',
        propertyName: 'gemeentenaam',
        // We're using XML output here because it has the side-effect of not returning the geometry.
        // This makes the response much smaller.
        //outputFormat: 'json',
        filter: `
            <Filter>
                <PropertyIsEqualTo>
                    <PropertyName>water</PropertyName>
                    <Literal>NEE</Literal>
                </PropertyIsEqualTo>
            </Filter>
        `,
    })

    const url = 'https://service.pdok.nl/cbs/wijkenbuurten/2022/wfs/v1_0?' + params.toString()
    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting gemeente list')
    }

    const body = await response.text()
    const document = new DOMParser().parseFromString(body, 'text/xml')
    const elements = document.getElementsByTagName('wijkenbuurten:gemeentenaam')
    const gemeenten: string[] = Array.prototype.map.call(elements, (element: Element) => {
        return element.textContent
    }) as string[]

    return gemeenten.sort()
}

export async function fetchBuurtList(gemeente: string): Promise<string[]> {
    if (!gemeente) {
        throw Error('No gemeente to fetch buurten for')
    }

    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'buurten',
        service: 'WFS',
        version: '2.0.0',
        propertyName: 'buurtnaam',
        // We're using XML output here because it has the side-effect of not returning the geometry.
        // This makes the response much smaller.
        //outputFormat: 'json',
        filter: `
            <Filter>
                <AND>
                    <PropertyIsEqualTo>
                        <PropertyName>gemeentenaam</PropertyName>
                        <Literal>${gemeente}</Literal>
                    </PropertyIsEqualTo>
                    <PropertyIsEqualTo>
                        <PropertyName>water</PropertyName>
                        <Literal>NEE</Literal>
                    </PropertyIsEqualTo>
                </AND>
            </Filter>
        `,
    })

    const url = 'https://service.pdok.nl/cbs/wijkenbuurten/2022/wfs/v1_0?' + params.toString()
    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting buurten list')
    }

    const body = await response.text()
    const document = new DOMParser().parseFromString(body, 'text/xml')
    const elements = document.getElementsByTagName('wijkenbuurten:buurtnaam')
    const buurten: string[] = Array.prototype.map.call(elements, (element: Element) => {
        return element.textContent
    }) as string[]

    return buurten.sort()
}

// https://service.pdok.nl/cbs/wijkenbuurten/2022/wfs/v1_0?request=DescribeFeatureType&service=WFS&version=2.0.0&typeName=buurten
export interface BuurtProperties {
    buurtcode: string
    buurtnaam: string
    wijkcode: string
    gemeentecode: string
    gemeentenaam: string
    indelingswijzigingWijkenEnBuurten: number
    water: string
    meestVoorkomendePostcode: string
    dekkingspercentage: number
    omgevingsadressendichtheid: number
    stedelijkheidAdressenPerKm2: number
    bevolkingsdichtheidInwonersPerKm2: number
    aantalInwoners: number
    mannen: number
    vrouwen: number
    percentagePersonen0Tot15Jaar: number
    percentagePersonen15Tot25Jaar: number
    percentagePersonen25Tot45Jaar: number
    percentagePersonen45Tot65Jaar: number
    percentagePersonen65JaarEnOuder: number
    percentageOngehuwd: number
    percentageGehuwd: number
    percentageGescheid: number
    percentageVerweduwd: number
    aantalHuishoudens: number
    percentageEenpersoonshuishoudens: number
    percentageHuishoudensZonderKinderen: number
    percentageHuishoudensMetKinderen: number
    gemiddeldeHuishoudsgrootte: number
    percentageWesterseMigratieachtergrond: number
    percentageNietWesterseMigratieachtergrond: number
    percentageUitMarokko: number
    percentageUitNederlandseAntillenEnAruba: number
    percentageUitSuriname: number
    percentageUitTurkije: number
    percentageOverigeNietwestersemigratieachtergrond: number
    oppervlakteTotaalInHa: number
    oppervlakteLandInHa: number
    oppervlakteWaterInHa: number
    jrstatcode: string
    jaar: number
}

export interface Buurt {
    type: 'Feature',
    id: string // e.g. buurten.c443da6a-ae59-4cc3-b0a1-3e426a8eaa2b
    properties: BuurtProperties,
    bbox: BoundingBox,
    geometry: MultiPolygon,
}

export function getBuurtCenter(buurt: Buurt): LatLng {
    const feature = center(buurt.geometry)

    return geoJsonPositionToLeaflet(feature.geometry.coordinates)
}

export async function fetchBuurt(gemeente: string, buurt: string): Promise<Buurt> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'buurten',
        service: 'WFS',
        version: '2.0.0',
        outputFormat: 'json',
        srsName: 'EPSG:4326', // output coordinate system
        filter: `
            <Filter>
                <AND>
                    <PropertyIsEqualTo>
                        <PropertyName>buurtnaam</PropertyName>
                        <Literal>${buurt}</Literal>
                    </PropertyIsEqualTo>
                    <PropertyIsEqualTo>
                        <PropertyName>gemeentenaam</PropertyName>
                        <Literal>${gemeente}</Literal>
                    </PropertyIsEqualTo>
                    <PropertyIsEqualTo>
                        <PropertyName>water</PropertyName>
                        <Literal>NEE</Literal>
                    </PropertyIsEqualTo>
                </AND>
            </Filter>
        `,
    })

    const url = 'https://service.pdok.nl/cbs/wijkenbuurten/2022/wfs/v1_0?' + params.toString()
    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting buurt geometry')
    }

    const body = await response.json()
    const features = body.features
    if (features.length != 1) {
        throw Error('Expected exactly one buurt')
    }

    return features[0]
}
