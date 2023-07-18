import {GeoJSON, LayerGroup, LayersControl, MapContainer, Marker, Popup, TileLayer, useMap} from "react-leaflet";
import {LatLng, Icon, LeafletEvent, Layer, PM, featureGroup} from "leaflet";
import "leaflet/dist/leaflet.css";
import {Component, FunctionComponent, PropsWithChildren, useEffect, useRef, useState} from "react";
import '@geoman-io/leaflet-geoman-free';
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css';

import markerIconPng from 'leaflet/dist/images/marker-icon.png';
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png';
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png';
import {Bag2DPand, Bag2DPandProperties} from "../services/bag2d";
import {PandData, SetBoundingBoxFn, useAppState} from "../services/appState";
import {useOnce} from "../services/use-once";
import {PandDataDisplay} from "./pand-display";

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

export const MainMap = ({setCurrentPandId, bag2dPanden, setBoundingBox}: {
    setCurrentPandId: (pandId: string) => void,
    bag2dPanden: Bag2DPand[],
    setBoundingBox: SetBoundingBoxFn,
}) => {
    return (
        <MapContainer center={disruptorBuildingLocation}
                      zoom={13}
                      scrollWheelZoom={true}
                      style={{height: "100vh", flexGrow: 1}}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                // TODO: option to use BAG WMS tiles
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {/*<LayersControl position="topright">*/}
            {/*    <LayersControl.Overlay name="Panden">*/}
                    <LayerGroup>
                        <Panden bag2dPanden={bag2dPanden} setCurrentPandId={setCurrentPandId} />
                    </LayerGroup>
                {/*</LayersControl.Overlay>*/}
                {/*<LayersControl.Overlay name="Tekenen">*/}
                    <Geoman setBoundingBox={setBoundingBox} />
                {/*</LayersControl.Overlay>*/}
            {/*</LayersControl>*/}
        </MapContainer>
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
const Geoman = ({setBoundingBox}: {setBoundingBox: SetBoundingBoxFn}) => {
    const map = useMap()
    const layerGroupRef = useRef(null);

    useOnce(() => {
        map.pm.setGlobalOptions({
            pathOptions: {
                color: "purple"
            }
        })
        map.pm.addControls({
            position: 'topleft',
            drawMarker: false,
            drawCircleMarker: false,
            drawPolyline: false,
        });
    })

    useEffect(() => {
        map.on("pm:create", ((event) => {
            const layer = event.layer
            // @ts-ignore
            layer.addTo(layerGroupRef.current)
            // @ts-ignore
            const featureGroup = layer._map.pm.getGeomanLayers(true)
            setBoundingBox(featureGroup.getBounds())
        }) as PM.CreateEventHandler)
    }, [layerGroupRef.current])


    return <LayerGroup ref={layerGroupRef} />
}
