import {EnergyCarrierType} from "./grid-node";

export type Contract = DeliveryContract | ConnectionContract | TransportContract | TaxContract

interface BaseContract {
    contractType: ContractType,
    contractScope: string, // actor id of the supplier
    energyCarrier: EnergyCarrierType,
    // TODO: if this has no decimals AnyLogic throws error "class java.lang.Integer cannot be cast to class java.lang.Double"
    annualFee_eur: number,
}

type ContractType = 'DELIVERY' | 'CONNECTION' | 'TRANSPORT' | 'TAX'

type DeliveryContractType =
    'ELECTRICITY_VARIABLE' |
    'ELECTRICITY_FIXED' |
    'METHANE_FIXED' |
    'HYDROGEN_FIXED' |
    'HEAT_FIXED' |
    'DIESEL_FIXED'

export interface DeliveryContract extends BaseContract {
    contractType: 'DELIVERY',
    deliveryContractType: DeliveryContractType,
    deliveryPrice_eurpkWh: number,
    feedinPrice_eurpkWh: number,
}

type ConnectionContractType = 'DEFAULT' | 'NFATO'

export interface ConnectionContract extends BaseContract {
    contractType: 'CONNECTION',
    connectionContractType: ConnectionContractType
    nfATO_starttime_h?: number,
    nfATO_endtime_h?: number,
    nfATO_capacity_kW?: number,
}

type TransportContractType = 'DEFAULT' | 'NODALPRICING' | 'PEAK' | 'BANDWIDTH'

export interface TransportContract extends BaseContract {
    contractType: 'TRANSPORT',
    transportContractType: TransportContractType,
    bandwidthTreshold_kW?: number,
    bandwidthTariff_eurpkWh?: number,
}

export interface TaxContract extends BaseContract {
    contractType: 'TAX',
    taxDelivery_eurpkWh: number,
    taxFeedin_eurpkWh: number,
    proportionalTax_pct: number,
}
