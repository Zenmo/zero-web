
import {EnergyAsset} from "./energy-asset";

export type GridConnectionCategory =
    'HOUSE' |
    'INDUSTRY' |
    'DISTRICTHEATING' |
    'WINDFARM' |
    'SOLARFARM' |
    'GRIDBATTERY' |
    'RESIDUALHEAT'

export type GridConnectionType =
    'DETACHED' |
    'SEMIDETACHED' |
    'TERRACED' |
    'APPARTMENT' |
    'HIGHRISE' |
    'STORE' |
    'OFFICE' |
    'SCHOOL' |
    'MT' |
    'HT' |
    'NONE' | // GridConnectionType is already optional so in which cases do we use NONE?
    'STEEL' |
    'INDUSTRY_OTHER' |
    'LOGISTICS' |
    'FARM' |
    'AGRO_ENERGYHUB' |
    'VILLAGE'

type EVChargingMode =
    "MAX_POWER" |
    "MAX_SPREAD" |
    "CHEAP" |
    "SIMPLE"

// Seems unused in the model
type HeatingType =
    "GASBURNER" |
    "HEATPUMP_AIR" |
    "DISTRICTHEAT" |
    "NONE" |
    "HEATPUMP_GASPEAK" |
    "GASFIRED" |
    "HYDROGENFIRED" |
    "HEATPUMP_BOILERPEAK" |
    "GASFIRED_CHPPEAK" |
    "LT_RESIDUAL_HEATPUMP_GASPEAK" |
    "DISTRICTHEATDECENTRAL" |
    "DISTRICT_EBOILER_CHP"

export type GridConnection = {
    id: string
    owner_actor: string // id
    category: GridConnectionCategory
    assets: EnergyAsset[]

    type?: GridConnectionCategory
    parent_electric?: string // gride node id
    capacity_kw?: number
    charging_mode?: EVChargingMode

    // TODO: create union types
    battery_mode?: string,
    electrolyser_mode?: string,
    insulation_label?: string,

    heating_type?: HeatingType, // mandatory for house
    smart_assets?: boolean,
    tempSetpointNight_degC?: number,
    tempSetpointNight_start_hr?: number,
    tempSetpointDay_degC?: number,
    tempSetpointDay_start_hr?: number,
    pricelevel_low_dif_from_avg_eurpkWh?: number,
    pricelevel_high_dif_from_avg_eurpkWh?: number,
    irradiance_recieving_surface?: number,
}
