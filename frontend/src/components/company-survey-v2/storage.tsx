import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {NumberRow} from './generic/number-row'
import {OldNumberInput} from './generic/old-number-input'
import {TextInput} from './generic/text-input'
import {TextAreaRow} from "./generic/text-area-row";

export const Storage = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch} = form

    const hasBattery = watch(`${prefix}.hasBattery`)
    const hasPlannedBattery = watch(`${prefix}.hasPlannedBattery`)

    return (
        <>
            <h2>5. Opslag</h2>
            <FormRow
                label="Heeft u een batterij voor elektriciteitsopslag in het bedrijf?"
                name={`${prefix}.hasBattery`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasBattery && (
                <>
                    <NumberRow
                        label="Wat is de capaciteit van deze batterij?"
                        name={`${prefix}.batteryCapacityKwh`}
                        form={form}
                        suffix="kWh" />
                    <NumberRow
                        label="Wat is het vermogen van deze batterij?"
                        name={`${prefix}.batteryPowerKw`}
                        form={form}
                        suffix="kW" />
                    <TextAreaRow
                        label="Hoe gebruik je deze batterij of hoe wordt die aangestuurd?"
                        form={form}
                        name={`${prefix}.batterySchedule`} />
                </>
            )}
            <FormRow
                label="Ben je geïnteresseerd in een batterij in de toekomst?"
                name={`${prefix}.hasPlannedBattery`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasPlannedBattery && (
                <>
                    <NumberRow
                        label="Wat is de capaciteit in kWh van deze toekomstige batterij?"
                        name={`${prefix}.plannedBatteryCapacityKwh`}
                        form={form}
                        suffix="kWh" />
                    <NumberRow
                        label="Wat is het vermogen in kW van deze toekomstige batterij?"
                        name={`${prefix}.plannedBatteryCapacityKwh`}
                        form={form}
                        suffix="kW" />
                    <TextAreaRow
                        label="Hoe gebruik je deze batterij of hoe wordt die aangestuurd?"
                        form={form}
                        name={`${prefix}.plannedBatterySchedule`} />
                </>
            )}
            <FormRow
                label="Maak je gebruik van opslag voor warmte of koude?"
                name={`${prefix}.hasThermalStorage`}
                form={form}
                WrappedInput={BooleanInput} />
        </>
    )
}
