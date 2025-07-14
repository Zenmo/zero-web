
import {TimeSeriesHookFormAdapter} from "./time-series-textarea-adapter"
import {FunctionComponent} from "react"
import {UseFormReturn} from "react-hook-form"
import {TimeSeriesType} from "zero-zummon"

export const TimeSeriesHeatPump: FunctionComponent<{form: UseFormReturn, prefix: string}> = ({form, prefix}) => {
    return (
        <>
            <h3>Kwartierwaarden warmte</h3>

            <h4>Electriciteitsverbruik warmtepomp</h4>

            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.heatPumpElectricityConsumptionTimeSeries_kWh`}
                timeSeriesType={TimeSeriesType.HEAT_PUMP_ELECTRICITY_CONSUMPTION} />

            <h4>Warmteproductie warmtepomp</h4>

            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.heatPumpHeatProductionTimeSeries_kWh`}
                timeSeriesType={TimeSeriesType.HEAT_PUMP_HEAT_PRODUCTION} />
        </>
    )
}
