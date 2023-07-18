import {LatLngBounds, Polygon} from "leaflet";
import {MultiPolygon} from "geojson"
import {BoundingBox} from "./bag2d";
import {Bag3DFeature} from "./3dbag_new";

const MAX = 100

export enum Verbruiktype {
    GAS = "g",
    ELEKTRICITEIT = "e",
}

type ResponseBody = {
    type: "FeatureCollection",
    features: PostcodeKleinverbruikFeature[],
    totalFeatures: number,
    numberMatched: number,
    numberReturned: number,
    timestamp: string,
    crs: {
        type: "name",
        properties: {
            name: "urn:ogc:def:crs:EPSG::4326",
        }
    }
    bbox: BoundingBox,
}

export type PostcodeKleinverbruikFeature = {
    type: "Feature",
    id: string, // example: lod12.7552479
    geometry: MultiPolygon,
    geometry_name: "geom",
    properties: PostcodeKleinverbruikProperties
}

export type PostcodeKleinverbruikProperties = {
    fid: number,
    netbeheerder: string
    netgebied: string
    straatnaam: string
    postcodevan: string
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

export const getPostcodeKleinverbruik = async (boundingBox: LatLngBounds, verbruiktype: Verbruiktype): Promise<PostcodeKleinverbruikFeature[]> => {
    const params = new URLSearchParams({
        request: "GetFeature",
        typeName: "postcode_kleinverbruik_" + verbruiktype,
        count: MAX.toString(),
        srsName: "EPSG:4326", // output coordinate system
        outputFormat: "json",
        bbox: [
            boundingBox.getWest(),
            boundingBox.getSouth(),
            boundingBox.getEast(),
            boundingBox.getNorth(),
            "EPSG:4326" // input coordinate system
        ].join(","),
    })

    const url = 'https://opendata.enexis.nl/geoserver/wfs?' + params.toString();

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error("Failure getting Enexis kleinverbruik data")
    }

    const json = await response.json() as ResponseBody
    if (json.numberMatched > MAX) {
        throw new Error("Maximum aantal Enexis postcodes overschreden")
    }

    return json.features
}
