export type EnergyAssetType =
    'ELECTRICITY_DEMAND' |
    'HEAT_DEMAND' |
    'WINDMILL' |
    'PHOTOVOLTAIC' |
    'PHOTOTHERMAL' |
    'GAS_BURNER' |
    'HEAT_PUMP_AIR' |
    'HEAT_PUMP_GROUND' |
    'STORAGE_HEAT' |
    'STORAGE_ELECTRIC' |
    'HOT_WATER_CONSUMPTION' |
    'HEAT_DELIVERY_SET' |
    'METHANE_FURNACE' |
    'HYDROGEN_FURNACE' |
    'ELECTRIC_HEATER' |
    'ELECTRIC_VEHICLE' |
    'ELECTRIC_HEAVY_GOODS_VEHICLE' |
    'DIESEL_VEHICLE' |
    'ELECTROLYSER' |
    'HYDROGEN_DEMAND' |
    'ELECTRIC_HOB' |
    'CURTAILER' |
    'CURTAILER_HEAT' |
    'RESIDUALHEATHT' |
    'RESIDUALHEATLT' |
    'METHANE_CHP' |
    'GAS_PIT' |
    'BUILDINGTHERMALS' |
    'HEAT_PUMP_WATER' |
    'DISTRICT_EBOILER_CHPPEAK' |
    'LIVESTOCK' |
    'STORAGE_GAS' |
    'DIESEL_DEMAND' |
    'BIOGAS_METHANE_CONVERTER' |
    'METHANE_DEMAND'

export interface EnergyAsset {
    type: EnergyAssetType

    // stub
    [key: string]: string | number | null
}

export const defaultEnergyAssets: EnergyAsset[] = [
    {
        'capacityElectricity_kW': null,
        'capacityHeat_kW': 8.0,
        'category': 'CONVERSION',
        'eta_r': 0.95,
        'id': 1441622,
        'name': 'TEMPLATE: gas-pit',
        'type': 'GAS_PIT',
    },
    {
        'category': 'CONSUMPTION',
        'energyConsumption_kWhpkm': 0.8,
        'id': 1441650,
        'name': 'TEMPLATE: Private diesel vehicle',
        'type': 'DIESEL_VEHICLE',
        'vehicleScaling': 1,
    },
    {
        'ambientTempType': 'AIR',
        'capacityHeat_kW': 100.0,
        'category': 'STORAGE',
        'heatCapacity_JpK': 10000000.0,
        'id': 1441678,
        'initialTemperature_degC': 21,
        'lossFactor_WpK': 80.0,
        'maxTemp_degC': 50,
        'minTemp_degC': -10,
        'name': 'TEMPLATE: House_heatmodel_D TVW',
        'setTemp_degC': 20,
        'type': 'BUILDINGTHERMALS',
    },
    {
        'category': 'CONSUMPTION',
        'id': 1441747,
        'name': 'TEMPLATE House other electricity demand - APARTMENT',
        'type': 'ELECTRICITY_DEMAND',
        'yearlyDemandElectricity_kWh': 2302.0,
    },
    {
        'category': 'CONSUMPTION',
        'id': 1441755,
        'name': 'TEMPLATE House hot water demand - APARTMENT',
        'type': 'HOT_WATER_CONSUMPTION',
        'yearlyDemandHeat_kWh': 2082.0,
    },
    {
        'capacityHeat_kW': 10.0,
        'category': 'CONVERSION',
        'deliveryTemp_degC': 90.0,
        'eta_r': 0.95,
        'id': 1441771,
        'name': 'TEMPLATE: Household gasburner',
        'type': 'GAS_BURNER',
    },
]
