import bbox from '@turf/bbox'
import booleanContains from '@turf/boolean-contains'
import {Geometries, GeometryCollection} from '@turf/helpers'
import {geomReduce} from '@turf/meta'
import {BBox, GeoJSON, Geometry, Position} from "geojson"
import {LatLng, LatLngBounds} from 'leaflet'

export const geoJsonBoundingBoxToLeaflet = (bbox: BBox): LatLngBounds =>
    new LatLngBounds(
        new LatLng(bbox[1], bbox[0]),
        new LatLng(bbox[3], bbox[2]),
    )

export const geoJsonPositionToLeaflet = (position: Position): LatLng =>
    new LatLng(position[1], position[0])

export const geometryToBoundingBox = (geometry: GeoJSON): LatLngBounds =>
    geoJsonBoundingBoxToLeaflet(bbox(geometry))

export const geometryCollectionContains = (geometryCollection: GeometryCollection, geometry: Geometries): boolean => {
    return geomReduce(geometryCollection, (contains: boolean, geometryObject: Geometries) => {
        return contains || booleanContains(geometryObject, geometry)
    }, false)
}
