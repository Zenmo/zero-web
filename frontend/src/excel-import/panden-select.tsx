import {FunctionComponent} from "react"
import {BuurtFeatureCollection, getBuurtCenter} from "../services/wijkenbuurten/buurten"
import {Bag2DPand} from "../services/bag2d"
import {GeoJSON, MapContainer, TileLayer} from "react-leaflet"
import {featureCollection} from "@turf/helpers"
import {geoJsonPositionToLeaflet} from "../services/geometry"
import center from "@turf/center"
import {LeafletMouseEventHandlerFn} from "leaflet"

/**
 * Component which renders a map from data already loaded from the network.
 */
export const PandenSelect: FunctionComponent<{
    buurten: BuurtFeatureCollection,
    panden: Map<BigInt, Bag2DPand>,
    otherCompaniesPandIds: string[],
    thisCompanyPandIds: string[],
    setThisCompanyPandIds: (pandIds: string[]) => void,
}> = ({ buurten, panden, otherCompaniesPandIds, thisCompanyPandIds, setThisCompanyPandIds }) => {

    const buurtCenter = geoJsonPositionToLeaflet(center(buurten).geometry.coordinates)

    const onClickPand: LeafletMouseEventHandlerFn = (event) => {
        const pandFeature: Bag2DPand = event.propagatedFrom.feature
    }

    return (
        <MapContainer
            center={buurtCenter}
            zoom={15}
            scrollWheelZoom={true}
            style={{
                minHeight: "30rem",
                flexGrow: 1,
            }}
        >
            <TileLayer
                attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                // TODO: option to use BAG WMS tiles
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <GeoJSON data={buurten} pathOptions={{color: 'salmon'}} />
            <GeoJSON
                data={featureCollection(Array.from(panden.values()))}
                pathOptions={{color: 'blue'}}
                eventHandlers={{click: console.log}}/>
        </MapContainer>
    )
}