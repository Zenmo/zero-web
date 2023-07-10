import {GeoJSON, MapContainer, Marker, Popup, TileLayer} from "react-leaflet";
import {geoJson, LatLng, LatLngBounds, Icon, polygon} from "leaflet";
import "leaflet/dist/leaflet.css";
import {useEffect, useLayoutEffect, useRef, useState} from "react";
import proj4 from "proj4";

import markerIconPng from 'leaflet/dist/images/marker-icon.png';
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png';
import {Bag2DFeature, getBag2dFeatures} from "./services/bag2d";
import {useAppState} from "./services/appState";
import {Simulate} from "react-dom/test-utils";
import click = Simulate.click;

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

// Add pand_id to geometry so we can look up data after an onclick event
function bagGeoWithPandId(features: Bag2DFeature[]) {
    return features.map(feature => ({
        ...feature.geometry,
        pand_id: feature.properties.identificatie,
    }))
}

export const MainMap = () => {
    const mapRef = useRef(null)
    const {appState, setBoundingBox} = useAppState()
    const geoJsons = bagGeoWithPandId(appState.bag2dData)
    useEffect(() => {
        setBoundingBox(disruptorBuildingLocation.toBounds(500))
    }, [])

    const [currentPandData, setCurrentPandData] = useState({})
    console.log(currentPandData)

    return (
        <div>
            <MapContainer ref={mapRef} center={disruptorBuildingLocation} zoom={13} scrollWheelZoom={true} style={{width: "800px", height: "800px"}}>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    // TODO: option to use BAG WMS tiles
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={disruptorBuildingLocation}>
                </Marker>
                {appState.bag2dData.map(feature => (
                    <GeoJSON key={feature.id} data={feature.geometry} eventHandlers={{
                        click: () => {
                            setCurrentPandData(feature)
                        }
                    }}/>
                ))}
            </MapContainer>
            <PandDataDisplay pandData={currentPandData} />
        </div>
    )
}

const PandDataDisplay = ({pandData}: any) => (
    <div>
        <pre>
            {JSON.stringify(pandData, undefined, 4)}
        </pre>
    </div>
)
