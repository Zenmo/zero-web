
import {Icon, LatLng, PM, LayerGroup as NativeLayerGroup} from 'leaflet'
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png'

import markerIconPng from 'leaflet/dist/images/marker-icon.png'
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png'
import 'leaflet/dist/leaflet.css'
// geoman must be imported after leaflet
import '@geoman-io/leaflet-geoman-free'
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css'
import {useEffect, useLayoutEffect, useRef, useState} from 'react'
import {GeoJSON, MapContainer, TileLayer, useMap, LayerGroup} from 'react-leaflet'
import {useLeafletContext} from '@react-leaflet/core'
import {SetGeometryFn} from '../services/appState'
import {Bag2DPand} from '../services/bag2d'
import {map} from '../services/iterable'
import {useOnce} from '../hooks/use-once'
import {Buurt, getBuurtCenter} from '../services/wijken-buurten'

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

export const MainMap = ({setCurrentPandId, bag2dPanden, setGeometry, buurt}: {
    setCurrentPandId: (pandId: string) => void,
    bag2dPanden: Iterable<Bag2DPand>,
    setGeometry: SetGeometryFn,
    buurt?: Buurt,
}) => {
    return (
        <MapContainer center={disruptorBuildingLocation}
                      zoom={13}
                      scrollWheelZoom={true}
                      style={{flexGrow: 1}}>
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                // TODO: option to use BAG WMS tiles
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {buurt && <Center center={getBuurtCenter(buurt)}/>}
            {buurt && <GeoJSON key={buurt.properties.buurtnaam} data={buurt.geometry} pathOptions={{color: 'red'}}/>}
            {/*<LayersControl position="topright">*/}
            {/*    <LayersControl.Overlay name="Panden">*/}
            <LayerGroup>
                <Panden bag2dPanden={bag2dPanden}
                        setCurrentPandId={setCurrentPandId}/>
            </LayerGroup>
            {/*</LayersControl.Overlay>*/}
            {/*<LayersControl.Overlay name="Tekenen">*/}
            <LayerGroup>
                <Geoman setGeometry={setGeometry}/>
            </LayerGroup>
            {/*</LayersControl.Overlay>*/}
            {/*</LayersControl>*/}

        </MapContainer>
    )
}

// Component used to change the center after initial render.
// Changing properties like this is not supported by react-leaflet.
const Center = ({center}: { center?: LatLng }) => {
    const map = useMap()
    useEffect(() => {
        if (center) {
            map.setView(center)
        }
    }, [center])
    return null
}

const Panden = (
    {bag2dPanden, setCurrentPandId}:
        {
            bag2dPanden: Iterable<Bag2DPand>,
            setCurrentPandId: (pandId: string) => void
        },
) => {
    const geoJsons = map(bag2dPanden, pand => (
        <GeoJSON key={pand.id} data={pand.geometry} eventHandlers={{
            click: () => {
                setCurrentPandId(pand.properties.identificatie)
            },
        }}/>
    ))

    return <>{[...geoJsons]}</>
}

// add paint controls
const Geoman = ({setGeometry}: { setGeometry: SetGeometryFn }) => {
    const map = useMap()
    const {layerContainer } = useLeafletContext() as {layerContainer: NativeLayerGroup}

    useOnce(() => {
        map.pm.setGlobalOptions({
            pathOptions: {
                color: 'purple',
            },
            layerGroup: layerContainer,
        })
        map.pm.addControls({
            position: 'topleft',
            drawMarker: false,
            drawCircleMarker: false,
            // Circles don't have a GeoJSON representation,
            // plus they don't seem an obvious tool to select an area.
            drawCircle: false,
            drawPolyline: false,
        })
        map.on('pm:create', ((event) => {
            // TODO: add id to every feature.
            setGeometry(layerContainer.toGeoJSON())
        }) as PM.CreateEventHandler)
    })

    return null
}
