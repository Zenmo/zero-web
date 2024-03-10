import {css} from '@emotion/react'
import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {TextAreaRow} from './generic/text-area-row'

export const Storage = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch} = form

    const hasBattery = watch(`${prefix}.hasBattery`)
    const hasPlannedBattery = watch(`${prefix}.hasPlannedBattery`)

    return (
        <>
            <h2>5. Opslag</h2>
            <FormRow
                label="Heeft u batterijopslag?"
                name={`${prefix}.hasBattery`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasBattery && (
                <div css={css`
                            animation: flash 1s;
`}>
                    <NumberRow
                        label="Wat is de totale opslagcapaciteit?"
                        name={`${prefix}.batteryCapacityKwh`}
                        form={form}
                        suffix="kWh" />
                    <NumberRow
                        label="Wat is het totale batterijvermogen?"
                        name={`${prefix}.plannedBatteryPowerKw`}
                        form={form}
                        suffix="kW" />
                    <TextAreaRow
                        label="Hoe en waarvoor wordt de batterijopslag ingezet?"
                        form={form}
                        name={`${prefix}.batterySchedule`} />
                </div>
            )}
            <FormRow
                label="Heeft u plannen om batterijopslag toe te voegen de komende jaren?"
                name={`${prefix}.hasPlannedBattery`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasPlannedBattery && (
                <>
                    <NumberRow
                        label="Wat is de capaciteit in kWh van de toekomstige batterijen (inschatting is voldoende)?"
                        name={`${prefix}.plannedBatteryCapacityKwh`}
                        form={form}
                        suffix="kWh" />
                    <NumberRow
                        label="Wat is het vermogen in kW van de toekomstige batterijen?"
                        name={`${prefix}.plannedBatteryCapacityKwh`}
                        form={form}
                        suffix="kW" />
                    <TextAreaRow
                        label="Hoe en waarvoor gaat u de batterijopslag waarschijnlijk inzetten?"
                        form={form}
                        name={`${prefix}.plannedBatterySchedule`} />
                </>
            )}
            <FormRow
                label="Maakt u gebruik van opslag voor warmte of koude?"
                name={`${prefix}.hasThermalStorage`}
                form={form}
                WrappedInput={BooleanInput} />
            {watch(`${prefix}.hasThermalStorage`) && (
                <NumberRow
                    label="Wat is het vermogen van de warmte- of koude opslag?"
                    name={`${prefix}.thermalStorageKw`}
                    form={form}
                    suffix="kW" />
            )}
        </>
    )
}
