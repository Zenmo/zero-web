import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Address = ({form, prefix = ""}: { form: UseFormReturn , prefix: string }) => {
    const {register} = form

    return (
        <>
            <LabelRow label="Straat">
                <input type="text" {...register(`${prefix}street`, {required: true})} />
            </LabelRow>
            <LabelRow label="Huisnummer">
                <NumberInput {...register(`${prefix}houseNumber`, {required: true})} />
            </LabelRow>
            <LabelRow label="Huisletter">
                <input type="text" {...register(`${prefix}houseLetter`, {required: false, maxLength: 1})} />
            </LabelRow>
            <LabelRow label="Toevoeging">
                <input type="text" {...register(`${prefix}houseNumberSuffix`, {required: false})} />
            </LabelRow>
            <LabelRow label="Postcode">
                <input type="text" {...register(`${prefix}postalCode`, {required: false})} />
            </LabelRow>
            <LabelRow label="Plaats">
                <input type="text" {...register(`${prefix}city`, {required: true})} />
            </LabelRow>
        </>
    )
}
