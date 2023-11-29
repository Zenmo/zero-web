import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {Cars} from './cars'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'
import {Trucks} from './trucks'
import {Vans} from './vans'

export const Transport = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const hasVehicles = watch('hasVehicles')

    return (
        <>
            <h2>6. Mobiliteit</h2>

            <LabelRow label="Hebben jullie bedrijfsauto's, -busjes, of -vrachtwagens?">
                <BooleanInput form={form} name="hasVehicles"/>
            </LabelRow>
            {hasVehicles && (
                <>
                    <Trucks form={form}/>
                    <Vans form={form} />
                    <Cars form={form} />
                </>
            )}
            <LabelRow label="Aantal personenauto's voor woon-werk verkeer per dag?">
                <NumberInput {...register('numDailyCarCommuters')} />
            </LabelRow>
        </>
    )
}