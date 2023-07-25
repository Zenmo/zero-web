import {Bag2DPand, getBag2dPanden} from "./bag2d";
import {useState} from "react";
import {LatLngBounds} from "leaflet";
import {Bag3DFeature, getBag3dFeatures} from "./3dbag_old";
import {getBagVerblijfsobjecten, Verblijfsobject} from "./bag-verblijfsobject";
import {
    getPostcodeKleinverbruik,
    PostcodeKleinverbruikFeature,
    PostcodeKleinverbruikProperties,
    Verbruiktype
} from "./enexis";

export interface AppState {
    bag2dPanden: Bag2DPand[]
    bag3dFeatures: Bag3DFeature[]
    verblijfsobjecten: Verblijfsobject[]
    postcodeKleinverbruik: {
        elektricity: PostcodeKleinverbruikFeature[]
        gas: PostcodeKleinverbruikFeature[]
    }
}

const initialState: AppState = {
    bag2dPanden: [],
    bag3dFeatures: [],
    verblijfsobjecten: [],
    postcodeKleinverbruik: {
        elektricity: [],
        gas: [],
    },
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
    gas?: {
        gemiddeldeStandaardjaarafname: number // m3
        soortAansluiting: string
        soortAansluitingPercentage: number
    },
    elektriciteit?: {
        gemiddeldeStandaardjaarafname: number // kWh
        soortAansluiting: string
        soortAansluitingPercentage: number
        leveringsRichtingPercentage: number // percentage aansluitingen met netto verbruik
    }
}

export type SetBoundingBoxFn = (boundingBox: LatLngBounds) => void

function validatePandId(pandId: string): void {
    if (!/^\d{16}$/.test(pandId)) {
        throw Error(`Not a pand id: ${pandId}`)
    }
}

export const useAppState = () => {
    const [appState, setAppState] = useState(initialState)

    const setBoundingBox: SetBoundingBoxFn = async (boundingBox: LatLngBounds) => {
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

        getBagVerblijfsobjecten(boundingBox)
            .then(verblijfsobjecten => {
                setAppState(appState => ({
                    ...appState,
                    verblijfsobjecten,
                }))
            })
            .catch(alert)

        getPostcodeKleinverbruik(boundingBox, Verbruiktype.ELEKTRICITEIT)
            .then(data=> {
                setAppState(appState => ({
                    ...appState,
                    postcodeKleinverbruik: {
                        ...appState.postcodeKleinverbruik,
                        elektricity: data,
                    }
                }))
            })
            .catch(alert)

        getPostcodeKleinverbruik(boundingBox, Verbruiktype.GAS)
            .then(data=> {
                setAppState(appState => ({
                    ...appState,
                    postcodeKleinverbruik: {
                        ...appState.postcodeKleinverbruik,
                        gas: data,
                    }
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
        )

        const kleinverbruik: {[postcode: string]: KleinVerbruikPerPostcode} = {}

        for (const postcode of postcodes) {
            const electriciteitProperties = appState.postcodeKleinverbruik.elektricity
                .map(feature => feature.properties)
                .find(postcodeKleinverbruik =>
                    postcodeKleinverbruik.postcodevan.replace(" ", "") >= postcode
                    &&
                    postcodeKleinverbruik.postcodetot.replace(" ", "") <= postcode
                )

            kleinverbruik[postcode] = {}

            if (electriciteitProperties) {
                kleinverbruik[postcode].elektriciteit = {
                    gemiddeldeStandaardjaarafname: electriciteitProperties.sjvgemiddeld,
                    soortAansluiting: electriciteitProperties.soortaansluiting,
                    soortAansluitingPercentage: electriciteitProperties.soortaansluitingperc,
                    leveringsRichtingPercentage: electriciteitProperties.leveringsrichtingperc,
                }
            }

            const gasProperties = appState.postcodeKleinverbruik.gas
                .map(feature => feature.properties)
                .find(postcodeKleinverbruik =>
                    postcodeKleinverbruik.postcodevan.replace(" ", "") >= postcode
                    &&
                    postcodeKleinverbruik.postcodetot.replace(" ", "") <= postcode
                )

            if (gasProperties) {
                kleinverbruik[postcode].gas = {
                    gemiddeldeStandaardjaarafname: gasProperties.sjvgemiddeld,
                    soortAansluiting: gasProperties.soortaansluiting,
                    soortAansluitingPercentage: gasProperties.soortaansluitingperc,
                }
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
