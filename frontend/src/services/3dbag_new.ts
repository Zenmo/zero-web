import {LatLngBounds, Polygon} from 'leaflet'
import {BAG_MAX_PANDEN, BoundingBox} from './bag2d'

type ResponseBody = {
    type: 'FeatureCollection',
    features: Bag3DFeature[],
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
    bbox: BoundingBox,
}

export type Bag3DFeature = {
    type: 'Feature',
    id: string, // example: lod12.7552479
    geometry: Polygon,
    geometry_name: 'geom',
    properties: FeatureProperties
}

type FeatureProperties = {
    fid: number
    b3_dd_id: number
    identificatie: string, // contains pand id, e.g. "NL.IMBAG.Pand.0772100000936241"
    b3_pand_deel_id: number,
    b3_h_50p: number,
    b3_h_70p: number
    b3_h_max: number
    b3_h_min: number
    // TODO: why is volume and maaiveld hoogte missing?
    // https://geoforum.nl/t/missing-properties-in-getfeatures/8482
}

export async function getBag3dFeatures(boundingBox: LatLngBounds): Promise<Bag3DFeature[]> {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'BAG3D:lod12',
        count: BAG_MAX_PANDEN.toString(),
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

    // This is the new endpoint.
    // Unfortunately it isn't working entirely yet.
    // See https://geoforum.nl/t/missing-properties-in-getfeatures/8482
    const url = 'https://data.3dbag.nl/api/BAG3D/wfs?' + params.toString()

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error('Failure getting BAG 2D data')
    }

    const body = await response.json() as ResponseBody
    if (body.numberMatched > BAG_MAX_PANDEN) {
        throw new Error('Maximum aantal panden overschreden')
    }

    return body.features
}

// This is a lot slower than searching by bounding box
function getBag3dByPandId(pandId: string) {
    const params = new URLSearchParams({
        request: 'GetFeature',
        typeName: 'BAG3D:lod12',
        count: BAG_MAX_PANDEN.toString(),
        outputFormat: 'json',
        CQL_FILTER: `identificatie='NL.IMBAG.Pand.${pandId}'`,
    })

    const url = 'https://data.3dbag.nl/api/BAG3D/wfs?' + params.toString()

    fetch(url)
}