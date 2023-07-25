import {EnergyAsset} from "./energy-asset";
import {defaultGridOperator} from "./actor";

type GridNodeType =
    'HSMS' | // transformer high <> medium voltage
    'MSLS' |
    'HT' | // High temperature district heating
    'MT' |
    'LT' |
    'LT5thgen'

export type EnergyCarrierType = 'ELECTRICITY' | 'METHANE' | 'HEAT' | 'HYDROGEN' | 'DIESEL'

export interface GridNode {
    id: string
    type: GridNodeType,
    category: EnergyCarrierType,
    parent?: string,
    // A common value for MSLS simulation is 1.6 times the number of households
    capacity_kw: number,
    owner_actor: string,
    assets: EnergyAsset[],
}

export const defaultHsMsTransformer: GridNode = {
    id: 'defaultHsMsTransformer',
    type: 'HSMS',
    category: 'ELECTRICITY',
    capacity_kw: 1000,
    owner_actor: defaultGridOperator.id,
    assets: [],
}

export const defaultMsLsTransformer: GridNode = {
    id: 'defaultMsLsTransformer',
    type: 'MSLS',
    category: 'ELECTRICITY',
    parent: defaultHsMsTransformer.id,
    capacity_kw: 630.1, // 630 kVA seems a common value
    owner_actor: defaultGridOperator.id,
    assets: [],
}
