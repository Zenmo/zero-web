import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Storage = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch} = form

    const hasBattery = watch(`${prefix}.hasBattery`)
    const hasPlannedBattery = watch(`${prefix}.hasPlannedBattery`)

    return (
        <>
            <h2>5. Opslag</h2>
            <LabelRow label="Heeft u een batterij voor elektriciteitsopslag in het bedrijf?">
                <BooleanInput form={form} name={`${prefix}.hasBattery`}/>
            </LabelRow>
            {hasBattery && (
                <>
                    <LabelRow label="Wat is de capaciteit van deze batterij?">
                        <NumberInput {...form.register(`${prefix}.batteryCapacityKWh`)} /> kWh
                    </LabelRow>
                    <LabelRow label="Wat is het vermogen van deze batterij?">
                        <NumberInput {...form.register(`${prefix}.batteryPowerKw`)} /> kW
                    </LabelRow>
                    <LabelRow label="Hoe gebruik je deze batterij of hoe wordt die aangestuurd?">
                        <input type="text" {...form.register(`${prefix}.batterySchedule`)} />
                    </LabelRow>
                </>
            )}
            <LabelRow label="Ben je geïnteresseerd in een batterij in de toekomst?">
                <BooleanInput form={form} name={`${prefix}.hasPlannedBattery`}/>
            </LabelRow>
            {hasPlannedBattery && (
                <>
                    <LabelRow label="Wat is de capaciteit in kWh van deze toekomstige batterij?">
                        <NumberInput {...form.register(`${prefix}.plannedBatteryCapacityKWh`)} /> kWh
                    </LabelRow>
                    <LabelRow label="Wat is het vermogen in kW van deze toekomstige batterij?">
                        <NumberInput {...form.register(`${prefix}.plannedBatteryPowerKw`)} /> kW
                    </LabelRow>
                    <LabelRow label="Hoe gebruik je deze batterij of hoe wordt die aangestuurd?">
                        <input type="text" {...form.register(`${prefix}.batterySchedule`)} />
                    </LabelRow>
                </>
            )}
            <LabelRow label="Maak je gebruik van opslag voor warmte of koude?">
                <BooleanInput form={form} name={`${prefix}.hasThermalStorage`}/>
            </LabelRow>
        </>
    )
}
