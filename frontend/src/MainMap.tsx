import {GeoJSON, MapContainer, Marker, Popup, TileLayer, useMap} from "react-leaflet";
import {LatLng, Icon, LeafletEvent} from "leaflet";
import "leaflet/dist/leaflet.css";
import {useEffect, useRef, useState} from "react";
import '@geoman-io/leaflet-geoman-free';
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css';

import markerIconPng from 'leaflet/dist/images/marker-icon.png';
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png';
import {Bag2DPand} from "./services/bag2d";
import {PandData, useAppState} from "./services/appState";
import {useOnce} from "./services/use-once";

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

// Add pand_id to geometry so we can look up data after an onclick event
function bagGeoWithPandId(features: Bag2DPand[]) {
    return features.map(feature => ({
        ...feature.geometry,
        pand_id: feature.properties.identificatie,
    }))
}

export const MainMap = () => {
    const mapRef = useRef(null)
    const {appState, setBoundingBox, getPandData} = useAppState()
    const geoJsons = bagGeoWithPandId(appState.bag2dPanden)
    useEffect(() => {
        setBoundingBox(disruptorBuildingLocation.toBounds(500))
    }, [])

    const [currentPandId, setCurrentPandId] = useState("")

    return (
        <div>
            <MapContainer ref={mapRef} center={disruptorBuildingLocation} zoom={13} scrollWheelZoom={true} style={{width: "800px", height: "800px"}}>
                <Geoman/>
                <TileLayer
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    // TODO: option to use BAG WMS tiles
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={disruptorBuildingLocation}>
                </Marker>
                <Panden bag2dPanden={appState.bag2dPanden} setCurrentPandId={setCurrentPandId} />
            </MapContainer>
            {currentPandId && <PandDataDisplay pandData={getPandData(currentPandId)} />}
        </div>
    )
}

const Panden = (
    {bag2dPanden, setCurrentPandId}:
        {bag2dPanden: Bag2DPand[], setCurrentPandId: (pandId: string) => void}
) => {
    const geoJsons = bag2dPanden.map(feature => (
        <GeoJSON key={feature.id} data={feature.geometry} eventHandlers={{
            click: () => {
                setCurrentPandId(feature.properties.identificatie)
            }
        }}/>
    ))

    return <>{geoJsons}</>
}

// add paint controls
const Geoman = () => {
    const map = useMap()
    useOnce(() => {
        //@ts-ignore
        map.pm.addControls({
            position: 'topleft',
            drawMarker: false,
            drawCircleMarker: false,
            drawPolyline: false,
        });

        map.on("pm:create", (event: LeafletEvent) => {
            console.log(event)
        })
    })

    return null
}

const PandDataDisplay = ({pandData}: {pandData: PandData}) => (
    <div>
        <pre>
            {JSON.stringify(pandData, undefined, 4)}
        </pre>
    </div>
)
