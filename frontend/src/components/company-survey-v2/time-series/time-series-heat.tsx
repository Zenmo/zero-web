
import {TimeSeriesHookFormAdapter} from "./time-series-textarea-adapter"
import {FunctionComponent} from "react"
import {UseFormReturn} from "react-hook-form"
import {TimeSeriesType} from "zero-zummon"

export const TimeSeriesHeatPump: FunctionComponent<{form: UseFormReturn, prefix: string}> = ({form, prefix}) => {
    return (
        <>
            <h4>Kwartierwaarden electriciteitsverbruik warmtepomp</h4>

            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.heatPumpElectricityConsumptionTimeSeries_kWh`}
                timeSeriesType={TimeSeriesType.HEAT_PUMP_ELECTRICITY_CONSUMPTION} />
        </>
    )
}

export const TimeSeriesHeatDelivery: FunctionComponent<{form: UseFormReturn, prefix: string}> = ({form, prefix}) => {
    return (
        <>
            <h4>Kwartierwaarden warmteafname</h4>

            <p>Afname van warmtenet of productie van warmtepomp</p>

            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.heatDeliveryTimeSeries_kWh`}
                timeSeriesType={TimeSeriesType.HEAT_DELIVERY} />
        </>
    )
}