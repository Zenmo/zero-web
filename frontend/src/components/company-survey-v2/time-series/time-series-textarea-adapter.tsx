import {FunctionComponent} from "react"
import {targetYear} from "./time-series-util"
import {TimeSeries, TimeSeriesType, timeSeriesFromJson, createEmptyTimeSeriesForYear} from "zero-zummon"
import {TimeSeriesTextarea} from "./time-series-textarea"
import {noop} from "lodash"
import {UseFormReturn} from "react-hook-form"

export const TimeSeriesHookFormAdapter: FunctionComponent<{
    form: UseFormReturn,
    field: string,
    timeSeriesType?: TimeSeriesType,
    label?: string,
}> = ({
    form,
    field,
    ...rest
}) => (
    <TimeSeriesTextareaAdapter
        timeSeries={form.watch(field)}
        setTimeSeries={(obj) => form.setValue(field, obj)}
        removeTimeSeries={() => form.setValue(field, null)}
        {...rest}
    />
)

/**
 * Has a plain JS object as input and output.
 * Converts that objects to a {@link TimeSeries} domain object for use in {@link TimeSeriesTextarea}
 */
export const TimeSeriesTextareaAdapter: FunctionComponent<{
    timeSeries?: any,
    timeSeriesType?: TimeSeriesType,
    setTimeSeries?: (obj: any) => void,
    removeTimeSeries?: () => void,
    label?: string,
}> = ({
    timeSeries,
    timeSeriesType = TimeSeriesType.ELECTRICITY_DELIVERY,
    setTimeSeries = noop,
    removeTimeSeries = noop,
    label,
}) => {
    const timeSeriesDomainObject = timeSeries ? timeSeriesFromJson(JSON.stringify(timeSeries)) : createEmptyTimeSeriesForYear(timeSeriesType, targetYear)

    return (
        <TimeSeriesTextarea
            timeSeries={timeSeriesDomainObject}
            setTimeSeries={(timeSeries: TimeSeries) => {
                setTimeSeries(JSON.parse(timeSeries.toJson()))
            }}
            removeTimeSeries={removeTimeSeries}
            label={label} />
    )
}
