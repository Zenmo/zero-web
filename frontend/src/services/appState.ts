import {LatLngBounds} from 'leaflet'
import {useState} from 'react'
import {Bag3DFeature, getBag3dFeatures} from './3dbag_old'
import {getBagVerblijfsobjecten, Verblijfsobject} from './bag-verblijfsobject'
import {Bag2DPand, getBag2dPanden} from './bag2d'
import {filter, uniq} from 'lodash'
import {
    fetchEnexisKleinverbruik,
} from './kleinverbruik/enexis'
import {getElectricityUsage, getGasUsage} from './kleinverbruik/kleinverbruik'
import {fetchLianderAndStedinKleinverbruik} from './kleinverbruik/liander-stedin'
import {
    PostcodeElektriciteitKleinverbruik, PostcodeGasKleinverbruik,
    PostcodeKleinverbruik,
} from './kleinverbruik/types'
import {postalCodeRangeDistance} from './postalcode'

export interface AppState {
    bag2dPanden: Bag2DPand[]
    bag3dFeatures: Bag3DFeature[]
    verblijfsobjecten: Verblijfsobject[]
    postcodeKleinverbruik: PostcodeKleinverbruik[]
}

const initialState: AppState = {
    bag2dPanden: [],
    bag3dFeatures: [],
    verblijfsobjecten: [],
    postcodeKleinverbruik: [],
}

export type PandData = {
    bag2dPand?: Bag2DPand,
    bag3dPand?: Bag3DFeature,
    verblijfsobjecten: Verblijfsobject[],
    kleinverbruik: {
        [postcode: string]: KleinVerbruikPerPostcode
    }
}

export type KleinVerbruikPerPostcode = {
    gas?: PostcodeGasKleinverbruik
    elektriciteit?: PostcodeElektriciteitKleinverbruik
}

export type SetBoundingBoxFn = (boundingBox: LatLngBounds) => void

function validatePandId(pandId: string): void {
    if (!/^\d{16}$/.test(pandId)) {
        throw Error(`Not a pand id: ${pandId}`)
    }
}

export const useAppState = () => {
    const [appState, setAppState] = useState(initialState)

    const setBoundingBox: SetBoundingBoxFn = (boundingBox: LatLngBounds) => {
        getBag2dPanden(boundingBox)
            .then(bag2dPanden => {
                setAppState(appState => ({
                    ...appState,
                    bag2dPanden,
                }))
            })
            .catch(alert)

        getBag3dFeatures(boundingBox)
            .then(bag3dFeatures => {
                setAppState(appState => ({
                    ...appState,
                    bag3dFeatures,
                }))
            })
            .catch(alert)

        setAppState(appState => ({
            ...appState,
            // reset value so it can be populated from two sources
            postcodeKleinverbruik: [],
        }))

        getBagVerblijfsobjecten(boundingBox)
            .then(verblijfsobjecten => {
                setAppState(appState => ({
                    ...appState,
                    verblijfsobjecten,
                }))
                const postalCodes = uniq(
                    verblijfsobjecten.map(verblijfsobject => verblijfsobject.postcode)
                        .filter(postcode => postcode)
                )

                return fetchLianderAndStedinKleinverbruik(postalCodes)
            })
            .then(data => {
                setAppState(appState => ({
                    ...appState,
                    postcodeKleinverbruik: [
                        ...appState.postcodeKleinverbruik,
                        ...data,
                    ]
                }))
            })
            .catch(alert)

        fetchEnexisKleinverbruik(boundingBox)
            .then(data => {
                setAppState(appState => ({
                    ...appState,
                    postcodeKleinverbruik: [
                        ...appState.postcodeKleinverbruik,
                        ...data,
                    ]
                }))
            })
            .catch(alert)
    }

    const getPandData = (pandId: string): PandData => {
        validatePandId(pandId)
        const bag2dPand = appState.bag2dPanden
            .find(bag2dpand => bag2dpand.properties.identificatie === pandId)
        const bag3dPand = appState.bag3dFeatures
            .find(bag3dPand => bag3dPand.properties.identificatie === `NL.IMBAG.Pand.${pandId}`)
        const verblijfsobjecten = appState.verblijfsobjecten
            .filter(verblijfsobject => verblijfsobject.pandidentificatie === pandId)

        const postcodes = new Set(
            verblijfsobjecten.map(verblijfsobject => verblijfsobject.postcode)
                // in rare cases a verblijfsobject has no postal code
                .filter(postalCode => postalCode),
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
        appState,
        setBoundingBox,
        getPandData,
    }
}
