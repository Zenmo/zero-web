import {GeometryCollection, Polygon} from 'geojson'
import {geometryCollectionContains} from './geometry'

describe(geometryCollectionContains.name, () => {
    const smallSquare: Polygon = {
        type: 'Polygon',
        coordinates: [
            [
                [4, 4],
                [4, 5],
                [5, 5],
                [5, 4],
                [4, 4],
            ]
        ]
    }

    const largeSquare: Polygon = {
        type: 'Polygon',
        coordinates: [
            [
                [3, 3],
                [3, 6],
                [6, 6],
                [6, 3],
                [3, 3],
            ]
        ]
    }

    test("large square contains smaller square", () => {
        const collection: GeometryCollection<Polygon> = {
            type: 'GeometryCollection',
            geometries: [
                largeSquare
            ],
        }

        expect(geometryCollectionContains(collection, smallSquare)).toBe(true)
    })

    test("small square does not contain larger square", () => {
        const collection: GeometryCollection<Polygon> = {
            type: 'GeometryCollection',
            geometries: [
                smallSquare
            ],
        }

        expect(geometryCollectionContains(collection, largeSquare)).toBe(false)
    })
})
