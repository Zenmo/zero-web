import {MapContainer, Marker, Popup, TileLayer} from "react-leaflet";
import {geoJson, LatLng, LatLngBounds, Icon} from "leaflet";
import "leaflet/dist/leaflet.css";
import {useEffect, useLayoutEffect, useRef} from "react";
import proj4 from "proj4";

import markerIconPng from 'leaflet/dist/images/marker-icon.png';
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png';

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// definitie van de Nederlandse coördinatenstelsel
// bron: iemand op het internet
const RD = "+proj=sterea +lat_0=52.15616055555555 +lon_0=5.38763888888889 +k=0.9999079 +x_0=155000 +y_0=463000 +ellps=bessel +units=m +towgs84=565.2369,50.0087,465.658,-0.406857330322398,0.350732676542563,-1.8703473836068,4.0812 +no_defs";

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

function getBag3dData() {
    const boundingBox = disruptorBuildingLocation.toBounds(300)

    const params = new URLSearchParams({
        request: "GetFeature",
        typeName: "BAG3D:lod12",
        count: "100",
        crs: "EPSG:3857", // web mercator projection
        outputFormat: "json",
        srsName: "EPSG:4326", // GPS
        bbox: boundingBoxToBag(boundingBox),
    });

    const url = 'https://data.3dbag.nl/api/BAG3D/wfs?' + params.toString();
}

async function addJsonToMap(mapRef: any) {
    const boundingBox = disruptorBuildingLocation.toBounds(300)

    const params = new URLSearchParams({
        request: "GetFeature",
        service: "WFS",
        typeName: "bag:pand",
        count: "100",
        crs: "EPSG:3857", // web mercator projection
        outputFormat: "json",
        srsName: "EPSG:4326", // GPS
        bbox: boundingBoxToBag(boundingBox),
        version: "2.0.0"
    });

    const url = 'https://service.pdok.nl/lv/bag/wfs/v2_0?' + params.toString();

    const response = await fetch(url)
    if (response.status != 200) {
        return
    }

    const json = await response.json()

    for (const feature of json.features) {
        geoJson(feature.geometry).addTo(mapRef.current);
    }
}

// Translate coordinate system into a number useful for PDOK BAG service
function boundingBoxToBag(boundingBox: LatLngBounds): string {
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

export const MainMap = () => {
    const mapRef = useRef(null)
    useEffect(() => {

        addJsonToMap(mapRef)

    }, [])


    return (
        <MapContainer ref={mapRef} center={disruptorBuildingLocation} zoom={13} scrollWheelZoom={true} style={{width: "800px", height: "800px"}}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={disruptorBuildingLocation}>
                <Popup>
                    A pretty CSS3 popup. <br/> Easily customizable.
                </Popup>
            </Marker>
        </MapContainer>
    )
}
