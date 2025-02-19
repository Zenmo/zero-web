import {targetYear} from "./time-series-util"
import {TimeSeriesHookFormAdapter, TimeSeriesTextareaAdapter} from "./time-series-textarea-adapter"
import {FunctionComponent} from "react"
import {UseFormReturn} from "react-hook-form"
import {TimeSeriesType} from "zero-zummon"

export const TimeSeriesElectricity: FunctionComponent<{form: UseFormReturn, prefix: string}> = ({form, prefix}) => {
    return (
        <>
            <h2>2. Kwartierwaarden electriciteit</h2>

            <p>Richtlijnen</p>

            <ul>
                <li>Geef waarden op van het gehele jaar {targetYear}.</li>
                <li>De eerste waarde betreft het verbruik in het kwartier van 1 januari {targetYear} van 00:00
                    tot 00:15 CET.
                </li>
                <li>De laatste waarde betreft het verbruik in het kwartier van 31 december {targetYear} 23:45
                    tot 1 januari {targetYear + 1} om 00:00 CET.
                </li>
                <li>Een langere periode mag.</li>
            </ul>
            <h3>Kwartierwaarden levering</h3>
            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.quarterHourlyDelivery_kWh`}
                timeSeriesType={TimeSeriesType.ELECTRICITY_DELIVERY} />
            <h3>Kwartierwaarden teruglevering</h3>
            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.quarterHourlyFeedIn_kWh`}
                timeSeriesType={TimeSeriesType.ELECTRICITY_FEED_IN} />
            <h3>Kwartierwaarden brutoproductiemeter</h3>
            <TimeSeriesHookFormAdapter
                form={form}
                field={`${prefix}.quarterHourlyProduction_kWh`}
                timeSeriesType={TimeSeriesType.ELECTRICITY_PRODUCTION} />
        </>
    )
}
