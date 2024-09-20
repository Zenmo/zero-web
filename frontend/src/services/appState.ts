import booleanContains from '@turf/boolean-contains'
import {Geometries, GeometryCollection as TurfGeometryCollection} from '@turf/helpers'
import {geomEach, geomReduce} from '@turf/meta'
import {GeoJsonObject, GeometryCollection} from 'geojson'
import {LatLngBounds} from 'leaflet'
import {uniq} from 'lodash'
import memoize from 'memoize-immutable'
import {useState} from 'react'
import {Bag3DFeature, fetchBag3dPanden} from './3dbag_old'
import {fetchBagVerblijfsobjecten, Verblijfsobject} from './bag-verblijfsobject'
import {Bag2DPand, fetchBag2dPanden} from './bag2d'
import {filter, map, mergeMaps, toIterable} from './iterable'
import {fetchEnexisKleinverbruik} from './kleinverbruik/enexis'
import {getElectricityUsage, getGasUsage} from './kleinverbruik/kleinverbruik'
import {fetchLianderAndStedinKleinverbruik} from './kleinverbruik/liander-stedin'
import {
    PostcodeElektriciteitKleinverbruik,
    PostcodeGasKleinverbruik,
    PostcodeKleinverbruik,
} from './kleinverbruik/types'
import {
    assertDefined,
} from './util'
import {
    geometryCollectionContains,
    geometryToBoundingBox,
} from "./geometry"

export interface AppHook {
    setGeometry: SetGeometryFn
    // we can probably merge these two to use a single "Pand" model
    bag2dPanden: Iterable<Bag2DPand>
    bag3dPanden: Iterable<Bag3DFeature>
    verblijfsobjecten: Iterable<Verblijfsobject>
    getPandData(pandId: string): PandData | undefined
    getPostcodeKleinverbruik(): PostcodeKleinverbruik[]
}

// internal state
interface AppState {
    geometry: GeometryCollection
    bag2dPanden: Map<BigInt, Bag2DPand>
    bag3dFeatures: Map<BigInt, Bag3DFeature>
    verblijfsobjecten: Map<BigInt, Verblijfsobject>
    postcodeKleinverbruik: PostcodeKleinverbruik[]
}

const createEmptyGeometryCollection = (): GeometryCollection => ({
    type: 'GeometryCollection',
    geometries: [],
})

const initialState: AppState = {
    geometry: createEmptyGeometryCollection(),
    bag2dPanden: new Map(),
    bag3dFeatures: new Map(),
    verblijfsobjecten: new Map(),
    postcodeKleinverbruik: [],
}

export type PandData = {
    bag2dPand?: Bag2DPand,
    bag3dPand?: Bag3DFeature,
    verblijfsobjecten: Iterable<Verblijfsobject>,
    kleinverbruik: {
        [postcode: string]: KleinVerbruikPerPostcode
    }
}

export type KleinVerbruikPerPostcode = {
    gas?: PostcodeGasKleinverbruik
    elektriciteit?: PostcodeElektriciteitKleinverbruik
}

export type SetBoundingBoxFn = (boundingBox: LatLngBounds) => void

export type SetGeometryFn = (geometry: GeoJsonObject) => void

function validatePandId(pandId: string): void {
    if (!/^\d{16}$/.test(pandId)) {
        throw Error(`Not a pand id: ${pandId}`)
    }
}

const toGeometryCollection = (geometry: GeoJsonObject): GeometryCollection => {
    // @ts-ignore incompatibility between @types/geojson and @turf
    return geomReduce(geometry, (previousValue, currentValue) => {
        previousValue.geometries.push(currentValue)
        return previousValue
    }, createEmptyGeometryCollection())
}

export const useApp = (): AppHook => {
    const [appState, setAppState] = useState(initialState)

    const addPanden = (panden: Map<BigInt, Bag2DPand>) => {
        setAppState(appState => ({
            ...appState,
            bag2dPanden: mergeMaps(appState.bag2dPanden, panden),
        }))
    }

    const add3dPanden = (panden: Bag3DFeature[]) => {
        setAppState(appState => ({
            ...appState,
            bag3dFeatures: panden.reduce((map, pand) => {
                return map.set(
                    BigInt(
                        (pand.properties.identificatie.match(/^NL\.IMBAG\.Pand\.(\d{16})$/) as [string, string])
                        [1]
                    ),
                    pand
                )
            }, new Map(appState.bag3dFeatures)),
        }))
    }

    const addVerblijfsobjecten = (verblijfsobjecten: Map<BigInt, Verblijfsobject>) => {
        setAppState(appState => ({
            ...appState,
            verblijfsobjecten: mergeMaps(appState.verblijfsobjecten, verblijfsobjecten),
        }))
    }

    const addKleinverbruik = (kleinverbruik: PostcodeKleinverbruik[]) => {
        setAppState(appState => ({
            ...appState,
            postcodeKleinverbruik: [
                ...appState.postcodeKleinverbruik,
                ...kleinverbruik,
            ],
        }))
    }

    const addGeometry = (geometry: Geometries) => {
        const boundingBox = geometryToBoundingBox(geometry)

        setAppState(appState => ({
            ...appState,
            geometry: {
                ...appState.geometry,
                geometries: [
                    ...appState.geometry.geometries,
                    geometry,
                ]
            }
        }))

        fetchBag2dPanden(boundingBox)
            .then(addPanden)
            .catch(alert)

        fetchBag3dPanden(boundingBox)
            .then(add3dPanden)
            .catch(alert)

        fetchBagVerblijfsobjecten(boundingBox)
            .then(verblijfsobjecten => {
                addVerblijfsobjecten(verblijfsobjecten)

                const postalCodes = uniq(
                    Array.from(verblijfsobjecten.values())
                        .map(verblijfsobject => verblijfsobject.postcode)
                        .filter(postcode => postcode)
                )

                return fetchLianderAndStedinKleinverbruik(postalCodes)
            })
            .then(addKleinverbruik)
            .catch(alert)

        fetchEnexisKleinverbruik(boundingBox)
            .then(addKleinverbruik)
            .catch(alert)
    }

    const setGeometry: SetGeometryFn = (geometry: GeoJsonObject) => {
        const geometryCollection = toGeometryCollection(geometry)
        // @ts-ignore incompatibility between @types/geojson and @turf
        geomEach(geometryCollection, (currentGeometry) => {
            addGeometry(assertDefined(currentGeometry))
        })
    }

    const bag2dPanden = getPandenWithinGeometry(appState.bag2dPanden, appState.geometry)

    const bag3dPanden: Iterable<Bag3DFeature> = toIterable(() =>
        filter(
            appState.bag3dFeatures.values(),
            pand => geometryCollectionContains(appState.geometry as TurfGeometryCollection, pand.geometry)
        )
    )

    // Get verblijfsobjecten that are within the current geometry
    const verblijfsobjecten: Iterable<Verblijfsobject> = toIterable(() => {
        const pandIds = Array.from(map(bag2dPanden, p => p.properties.identificatie))

        // O(n2)
        return filter(
            appState.verblijfsobjecten.values(),
            verblijfsobject => pandIds.includes(verblijfsobject.pandidentificatie)
        )
    })

    const getPostcodeKleinverbruik = () => appState.postcodeKleinverbruik

    const getPandData = (pandId: string): PandData|undefined => {
        validatePandId(pandId)
        const bag2dPand = appState.bag2dPanden.get(BigInt(pandId))
        if (!bag2dPand) {
            return undefined
        }
        const bag3dPand = appState.bag3dFeatures.get(BigInt(pandId))
        const verblijfsobjecten = toIterable(() => filter(
            appState.verblijfsobjecten.values(),
            (verblijfsobject: Verblijfsobject) => verblijfsobject.pandidentificatie === pandId))

        const postcodes = new Set(
            filter(
                map(verblijfsobjecten, verblijfsobject => verblijfsobject.postcode),
                // in rare cases a verblijfsobject has no postal code
                postalCode => Boolean(postalCode),
            )
        )

        const kleinverbruik: {
            [postcode: string]: KleinVerbruikPerPostcode
        } = {}

        for (const postcode of postcodes) {
            kleinverbruik[postcode] = {
                gas: getGasUsage(postcode, appState.postcodeKleinverbruik),
                elektriciteit: getElectricityUsage(postcode, appState.postcodeKleinverbruik),
            }
        }

        return {
            bag2dPand,
            bag3dPand,
            verblijfsobjecten,
            kleinverbruik,
        }
    }

    const getAggregateData = (): void => {

    }

    return {
        setGeometry,
        getPandData,
        bag2dPanden,
        bag3dPanden,
        verblijfsobjecten,
        getPostcodeKleinverbruik,
    }
}

const getPandenWithinGeometry = memoize(
    (panden: Map<BigInt, Bag2DPand>, geometry: GeometryCollection): Bag2DPand[] => {
        return Array.from(panden.values()).filter(
            pand => geometryCollectionContains(geometry as TurfGeometryCollection, pand.geometry)
        )
    }
)