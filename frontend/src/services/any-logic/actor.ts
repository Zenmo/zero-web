import {Contract} from './contract';

export type ActorType =
    'OPERATORGRID' |
    'GOVHOLON' |
    'HOLONENERGY' |
    'CONNECTIONOWNER' | // like a household or private company
    'SUPPLIERENERGY'

export interface Actor {
    id: string
    category: ActorType
    group?: string
    subgroup?: string
    contracts?: Contract[]
}

interface HolonActor extends Actor {
    category: 'GOVHOLON'
    node?: string // grid node id of energy hub
}

export const defaultGridOperator: Actor = {
    id: 'defaultGridOperator',
    category: 'OPERATORGRID',
}

export const defaultElectrictySupplier: Actor = {
    id: 'defaultElectrictySupplier',
    category: 'SUPPLIERENERGY',
}

export const defaultGovernment: Actor = {
    id: 'defaultGovernment',
    category: 'GOVHOLON',
}
