import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {Cars} from './cars'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {NumberInput} from './generic/number-input'
import {OldNumberInput} from './generic/old-number-input'
import {TextInput} from './generic/text-input'
import {Trucks} from './trucks'
import {Vans} from './vans'

export const Transport = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {register, watch} = form

    const hasVehicles = watch(`${prefix}.hasVehicles`)

    return (
        <>
            <h2>6. Mobiliteit</h2>
            <FormRow
                label="Hebben jullie bedrijfsauto's, -busjes, of -vrachtwagens?"
                WrappedInput={BooleanInput}
                name={`${prefix}.hasVehicles`}
                form={form} />
            {hasVehicles && (
                <>
                    <Trucks form={form} prefix={`${prefix}.trucks`} />
                    <Vans form={form} prefix={`${prefix}.vans`} />
                    <Cars form={form} prefix={`${prefix}.cars`} />
                </>
            )}
            <FormRow
                label="Aantal personenauto's voor woon-werk verkeer per dag?"
                WrappedInput={NumberInput}
                name={`${prefix}.numDailyCarCommuters`}
                form={form} />
        </>
    )
}