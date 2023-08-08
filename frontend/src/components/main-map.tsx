
import {Icon, LatLng, PM} from 'leaflet'
import markerIcon2xPng from 'leaflet/dist/images/marker-icon-2x.png'

import markerIconPng from 'leaflet/dist/images/marker-icon.png'
import markerShadowPng from 'leaflet/dist/images/marker-shadow.png'
import 'leaflet/dist/leaflet.css'
// geoman must be imported after leaflet
import '@geoman-io/leaflet-geoman-free'
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css'
import {useEffect, useRef} from 'react'
import {GeoJSON, LayerGroup, MapContainer, TileLayer, useMap} from 'react-leaflet'
import {SetBoundingBoxFn} from '../services/appState'
import {Bag2DPand} from '../services/bag2d'
import {useOnce} from '../services/use-once'
import {Buurt, getBuurtCenter} from '../services/wijken-buurten'

const disruptorBuildingLocation = new LatLng(51.44971831403754, 5.4947035381928035)

// fix marker image (not sure we're ever gonna use this)
// alternatively assign to Marker.prototype.options.icon.options
Icon.Default.mergeOptions({
    iconUrl: markerIconPng,
    iconRetinaUrl: markerIcon2xPng,
    shadowUrl: markerShadowPng,
})

export const MainMap = ({setCurrentPandId, bag2dPanden, setBoundingBox, buurt}: {
    setCurrentPandId: (pandId: string) => void,
    bag2dPanden: Bag2DPand[],
    setBoundingBox: SetBoundingBoxFn,
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
            <Geoman setBoundingBox={setBoundingBox}/>
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
            bag2dPanden: Bag2DPand[],
            setCurrentPandId: (pandId: string) => void
        },
) => {
    const geoJsons = bag2dPanden.map(feature => (
        <GeoJSON key={feature.id} data={feature.geometry} eventHandlers={{
            click: () => {
                setCurrentPandId(feature.properties.identificatie)
            },
        }}/>
    ))

    return <>{geoJsons}</>
}

// add paint controls
const Geoman = ({setBoundingBox}: { setBoundingBox: SetBoundingBoxFn }) => {
    const map = useMap()
    const layerGroupRef = useRef(null)

    useOnce(() => {
        map.pm.setGlobalOptions({
            pathOptions: {
                color: 'purple',
            },
        })
        map.pm.addControls({
            position: 'topleft',
            drawMarker: false,
            drawCircleMarker: false,
            drawPolyline: false,
        })
    })

    useEffect(() => {
        map.on('pm:create', ((event) => {
            const layer = event.layer
            // @ts-ignore
            layer.addTo(layerGroupRef.current)
            // @ts-ignore
            const featureGroup = layer._map.pm.getGeomanLayers(true)
            setBoundingBox(featureGroup.getBounds())
        }) as PM.CreateEventHandler)
    }, [layerGroupRef.current])

    return <LayerGroup ref={layerGroupRef}/>
}
