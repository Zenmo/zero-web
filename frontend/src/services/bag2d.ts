import {LatLngBounds} from "leaflet";
import proj4 from "proj4";

interface Response {
    type: "FeatureCollection",
    numberMatched: number,
    name: "pand",
    features: Bag2DFeature[],
    bbox: BoundingBox,
}

export interface Bag2DFeature {
    type: "Feature",
    id: string // e.g. pand.97faabcf-a317-4c15-ae42-d1b8d04beb7d
    properties: FeatureProperties,
    bbox: BoundingBox,
    geometry: {
        type: "Polygon",
        coordinates: number[][]
    }
}

interface FeatureProperties {
    identificatie: string, // numeric
    rdf_seealso: string, // url
    bouwjaar: number,
    status: string,
    gebruiksdoel: string,
    aantal_verblijfsobjecten: number,
}

type BoundingBox = [number, number, number, number]

export async function getBag2dFeatures(boundingBox: LatLngBounds): Promise<Response> {
    const params = new URLSearchParams({
        request: "GetFeature",
        service: "WFS",
        typeName: "bag:pand",
        count: "100",
        crs: "EPSG:3857", // web mercator projection
        outputFormat: "json",
        srsName: "EPSG:4326", // GPS
        bbox: boundingBoxToBAG(boundingBox),
        version: "2.0.0"
    });

    const url = 'https://service.pdok.nl/lv/bag/wfs/v2_0?' + params.toString();

    const response = await fetch(url)
    if (response.status != 200) {
        throw Error("Failure getting BAG 2D data")
    }

    return await response.json()
}

// definitie van de Nederlandse coördinatenstelsel
// bron: iemand op het internet
const RD = "+proj=sterea +lat_0=52.15616055555555 +lon_0=5.38763888888889 +k=0.9999079 +x_0=155000 +y_0=463000 +ellps=bessel +units=m +towgs84=565.2369,50.0087,465.658,-0.406857330322398,0.350732676542563,-1.8703473836068,4.0812 +no_defs";

// Translate coordinate system into a number useful for PDOK BAG service
export function boundingBoxToBAG(boundingBox: LatLngBounds): string {
    const {x: x1, y: y1} = proj4("EPSG:4326", RD, {
        x: boundingBox.getWest(),
        y: boundingBox.getSouth(),
    })
    const {x: x2, y: y2} = proj4("EPSG:4326", RD, {
        x: boundingBox.getEast(),
        y: boundingBox.getNorth(),
    })

    return [x1, y1, x2, y2].join(",")
}
