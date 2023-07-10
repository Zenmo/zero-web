import {LatLngBounds} from "leaflet";
import {boundingBoxToBAG} from "./bag2d";


export function getBag3dFeatures(boundingBox: LatLngBounds) {

    const params = new URLSearchParams({
        request: "GetFeature",
        typeName: "BAG3D:lod12",
        count: "100",
        crs: "EPSG:3857", // web mercator projection
        outputFormat: "json",
        srsName: "EPSG:4326", // GPS
        bbox: boundingBoxToBAG(boundingBox),
    });

    const url = 'https://data.3dbag.nl/api/BAG3D/wfs?' + params.toString();

    fetch(url)
}

function getBag3dByPandId(pandId: string) {
    const params = new URLSearchParams({
        request: "GetFeature",
        typeName: "BAG3D:lod12",
        count: "100",
        outputFormat: "json",
        CQL_FILTER: `identificatie='NL.IMBAG.Pand.${pandId}'`,
    });

    const url = 'https://data.3dbag.nl/api/BAG3D/wfs?' + params.toString();

    fetch(url)
}