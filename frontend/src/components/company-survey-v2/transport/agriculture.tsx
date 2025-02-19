import {FunctionComponent} from "react"
import {UseFormReturn} from "react-hook-form"
import {NumberRow} from "../generic/number-row"
import {TimeSeriesTextareaAdapter} from "../time-series/time-series-textarea-adapter"
import {TimeSeriesType} from "zero-zummon"

export const Agriculture: FunctionComponent<{
    form: UseFormReturn,
    prefix: string
}> = ({form, prefix}) => {
    const {watch} = form

    return (
        <>
            <h3>Landbouwvoertuigen</h3>
            <NumberRow
                label="Aantal trekkers?"
                name={`${prefix}.numTractors`}
                form={form} />

            {watch(`${prefix}.numTractors`) && (
                <>
                    <NumberRow
                        label="Gezamelijk dieselverbruik per jaar?"
                        name={`${prefix}.annualDieselUsage_L`}
                        form={form}
                        suffix="liter" />
                    <h4>Verbruiksprofiel diesel</h4>
                    <p>Vul in 52 waarden in van het dieselverbruik op weekbasis.</p>
                    <TimeSeriesTextareaAdapter
                        timeSeries={form.watch(`${prefix}.dieselUsageTimeSeries`)}
                        timeSeriesType={TimeSeriesType.AGRICULTURE_DIESEL_CONSUMPTION}
                        setTimeSeries={timeSeries => form.setValue(`${prefix}.dieselUsageTimeSeries`, timeSeries)}
                        label="dieselprofiel" />
                </>
            )}
        </>
    )
}
