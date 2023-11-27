import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'

export enum HeatingType {
    GAS_BOILER = "GAS_BOILER",
    ELECTRIC_HEATPUMP = "ELECTRIC_HEATPUMP",
    HYBRID_HEATPUMP = "HYBRID_HEATPUMP",
    DISTRICT_HEATING = "DISTRICT_HEATING",
    OTHER = "OTHER",
}

const labels = {
    [HeatingType.GAS_BOILER]: "Gasketel",
    [HeatingType.ELECTRIC_HEATPUMP]: "Elektrische warmtepomp",
    [HeatingType.HYBRID_HEATPUMP]: "Hybride warmtepomp",
    [HeatingType.DISTRICT_HEATING]: "Warmtenet",
    [HeatingType.OTHER]: "Anders",
}

export const HeatingTypeCheckboxes = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    return (
        <LabelRow label="Waar komt uw warmte vandaan?">
            {Object.entries(labels).map(([value, label]) => (
                <label key={value} css={{display: 'block'}}>
                    <input
                        type="checkbox"
                        {...form.register(`${prefix}.heatingTypes`, {value: []})}
                        value={value} />
                    {label}
                </label>
            ))}
        </LabelRow>
    )
}
