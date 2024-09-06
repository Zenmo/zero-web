import bbox from '@turf/bbox'
import booleanContains from '@turf/boolean-contains'
import {Geometries, GeometryCollection} from '@turf/helpers'
import {geomReduce} from '@turf/meta'
import {BBox, Geometry, Position} from 'geojson'
import {LatLng, LatLngBounds} from 'leaflet'

export const geoJsonBoundingBoxToLeaflet = (bbox: BBox): LatLngBounds =>
    new LatLngBounds(
        new LatLng(bbox[1], bbox[0]),
        new LatLng(bbox[3], bbox[2]),
    )

export const geoJsonPositionToLeaflet = (position: Position): LatLng =>
    new LatLng(position[1], position[0])

export const geometryToBoundingBox = (geometry: Geometry): LatLngBounds =>
    geoJsonBoundingBoxToLeaflet(bbox(geometry))

export const geometryCollectionContains = (geometryCollection: GeometryCollection, geometry: Geometries): boolean => {
    return geomReduce(geometryCollection, (contains: boolean, geometryObject: Geometries) => {
        return contains || booleanContains(geometryObject, geometry)
    }, false)
}

export const assertDefined = <T>(value: T | undefined | null): T => {
    if (value === undefined || value === null) {
        throw new Error('value is undefined')
    }
    return value
}

export const mapOrElse = <T,R>(
    array: Array<T> | ReadonlyArray<T>,
    f: (value: T, index: number, array: Array<T> | ReadonlyArray<T>) => R,
    g: () => R,
): R[] => {
    if (array.length === 0) {
        return [g()]
    }
    return array.map(f)
}

