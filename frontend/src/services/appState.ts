import {Bag2DPand, getBag2dPanden} from "./bag2d";
import {useState} from "react";
import {LatLngBounds} from "leaflet";
import {Bag3DFeature, getBag3dFeatures} from "./3dbag_old";
import {Bag2DVerblijfsobject, getBagVerblijfsobjecten} from "./bag-verblijfsobject";

export interface AppState {
    bag2dPanden: Bag2DPand[]
    bag3dFeatures: Bag3DFeature[]
    verblijfsobjecten: Bag2DVerblijfsobject[]
}

const initialState: AppState = {
    bag2dPanden: [],
    bag3dFeatures: [],
    verblijfsobjecten: [],
}

export type PandData = {
    bag2dPand?: Bag2DPand,
    bag3dPand?: Bag3DFeature,
    verblijfsobjecten: Bag2DVerblijfsobject[],
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
        const bag2dPanden = await getBag2dPanden(boundingBox)
        setAppState(appState => ({
            ...appState,
            bag2dPanden,
        }))
        const bag3dFeatures = await getBag3dFeatures(boundingBox)
        setAppState(appState => ({
            ...appState,
            bag3dFeatures,
        }))
        const verblijfsobjecten = await getBagVerblijfsobjecten(boundingBox)
        setAppState(appState => ({
            ...appState,
            verblijfsobjecten,
        }))
    }

    const getPandData = (pandId: string): PandData => {
        validatePandId(pandId)
        const bag2dPand = appState.bag2dPanden
            .find(bag2dpand => bag2dpand.properties.identificatie === pandId)
        const bag3dPand = appState.bag3dFeatures
            .find(bag3dPand => bag3dPand.properties.identificatie === `NL.IMBAG.Pand.${pandId}`)
        const verblijfsobjecten = appState.verblijfsobjecten
            .filter(bagVerblijfsobject => bagVerblijfsobject.properties.pandidentificatie === pandId)

        return {
            bag2dPand,
            bag3dPand,
            verblijfsobjecten,
        }
    }

    return {
        appState,
        setBoundingBox,
        getPandData,
    }
}
