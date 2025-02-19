import {FunctionComponent} from "react"
import {Dropdown} from "primereact/dropdown"
import {isoStringToDateTimeUnit, dateTimeUnitToIsoString} from "zero-zummon"
import {SelectItem, SelectItemOptionsType} from "primereact/selectitem"

const options: SelectItemOptionsType = [
    {
        value: "PT15M",
        label: "Kwartier",
    },
    {
        value: "PT1H",
        label: "Uur",
    },
    {
        value: "P1D",
        label: "Dag",
    },
    {
        value: "P1W",
        label: "Week",
    },
    {
        value: "P1M",
        label: "Maand",
    },
    {
        value: "P1Y",
        label: "Jaar",
    },
]

export const IntervalDropdown: FunctionComponent<{
    timeStep: any,
    setTimeStep: (timeStep: any) => void
}> = ({timeStep, setTimeStep}) => {

    return (
        <Dropdown options={options} value={dateTimeUnitToIsoString(timeStep)} onChange={event => setTimeStep(isoStringToDateTimeUnit(event.value))}/>
    )
}
