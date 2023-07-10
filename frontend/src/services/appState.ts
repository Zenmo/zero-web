import {Bag2DFeature, getBag2dFeatures} from "./bag2d";
import {useState} from "react";
import {LatLngBounds} from "leaflet";
import {getBag3dFeatures} from "./bag3d";

export interface AppState {
    bag2dData: Bag2DFeature[]
}

const initialState: AppState = {
    bag2dData: [],
}

export const useAppState = () => {
    const [appState, setAppState] = useState(initialState)

    const setBoundingBox = async (boundingBox: LatLngBounds) => {
        const bagResponse = await getBag2dFeatures(boundingBox)
        setAppState({
            ...appState,
            bag2dData: bagResponse.features,
        })
        getBag3dFeatures(boundingBox)
    }

    return {
        appState,
        setBoundingBox,
    }
}
