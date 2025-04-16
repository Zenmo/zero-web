import {targetYear} from "./time-series-util"
import {TimeSeriesHookFormAdapter, TimeSeriesTextareaAdapter} from "./time-series-textarea-adapter"
import {FunctionComponent} from "react"
import {UseFormReturn} from "react-hook-form"
import {TimeSeriesType} from "zero-zummon"

export const TimeSeriesNaturalGas: FunctionComponent<{form: UseFormReturn, prefix: string}> = ({form, prefix}) => {
    return (
        <>
            <h3>Uurwaarden aardgas</h3>

            <p>Richtlijnen</p>

            <ul>
                <li>Geef waarden op van het gehele jaar {targetYear}.</li>
                <li>De eenheid is m3.</li>
                <li>De eerste waarde betreft het verbruik in het uur van 1 januari {targetYear} van 00:00
                    tot 01:00 CET.
                </li>
                <li>De laatste waarde betreft het verbruik in het uur van 31 december {targetYear} 23:00
                    tot 1 januari {targetYear + 1} om 00:00 CET.
                </li>
                <li>Een langere periode mag.</li>
            </ul>
            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.hourlyDelivery_m3`}
                timeSeriesType={TimeSeriesType.GAS_DELIVERY} />
        </>
    )
}
