import {BBox, Position} from 'geojson'
import {LatLng, LatLngBounds} from 'leaflet'

export const geoJsonBoundingBoxToLeaflet = (bbox: BBox): LatLngBounds =>
    new LatLngBounds(
        new LatLng(bbox[1], bbox[0]),
        new LatLng(bbox[3], bbox[2]),
    )

export const geoJsonPositionToLeaflet = (position: Position): LatLng =>
    new LatLng(position[1], position[0])

export const assertDefined = <T>(value: T | undefined): T => {
    if (value === undefined) {
        throw new Error('value is undefined')
    }
    return value
}
