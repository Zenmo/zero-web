import {LatLngBounds} from "leaflet";
import {BAG_MAX_PANDEN, BoundingBox, boundingBoxToBAG} from "./bag2d";
import {Point} from "geojson";

export type Bag2DVerblijfsobject = {
    type: "Feature",
    id: string // e.g. "verblijfsobject.144b58ad-1e01-4025-b275-db34fc19ebf7"
    properties: VerblijfsobjectProperties,
    bbox: BoundingBox,
    geometry: Point,
}

export type VerblijfsobjectProperties = {
    identificatie: string, // example: "0772010000726364"
    pandidentificatie: string, // example: "0772100000306503"
    rdf_seealso: string, // url
    oppervlakte: number,
    status: string,
    gebruiksdoel: string,
    openbare_ruimte: string,
    huisnummer: number,
    huisletter: string,
    toevoeging: string,
    postcode: string,
    woonplaats: string,
    bouwjaar: string,
    pandstatus: string,
    // probably incomplete
}

export async function getBagVerblijfsobjecten(boundingBox: LatLngBounds): Promise<Bag2DVerblijfsobject[]> {
    const params = new URLSearchParams({
        request: "GetFeature",
        service: "WFS",
        typeName: "bag:verblijfsobject",
        // TODO: verify pagination cut-off
        count: BAG_MAX_PANDEN.toString(),
        outputFormat: "json",
        srsName: "EPSG:4326", // GPS
        bbox: [
            boundingBox.getWest(),
            boundingBox.getSouth(),
            boundingBox.getEast(),
            boundingBox.getNorth(),
            "urn:ogc:def:rs:EPSG::4326", // input coordinate system
        ].join(","),
        version: "2.0.0"
    })

    const url = 'https://service.pdok.nl/lv/bag/wfs/v2_0?' + params.toString();

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error("Failure getting BAG Verblijfsobjecten")
    }

    const body = await response.json() as any
    if (body.numberMatched > BAG_MAX_PANDEN) {
        throw new Error("Maximum aantal verblijfsobjecten overschreden")
    }

    return body.features
}