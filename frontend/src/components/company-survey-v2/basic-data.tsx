import {forwardRef} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {FormRow} from './generic/form-row'
import {TextInput} from './generic/text-input'

export const BasicData = ({form}: { form: UseFormReturn }) => (
    <>
        <h2>1. Bedrijfsgegevens</h2>
        <FormRow
            label="Naam bedrijf"
            InputComponent={TextInput}
            name="companyName"
            form={form}
            options={{required: true}} />
        <FormRow
            label="Naam contactpersoon"
            InputComponent={TextInput}
            name="personName"
            form={form}
            options={{required: true}} />
        <FormRow
            label="E-mailadres"
            name="email"
            form={form}
            InputComponent={forwardRef((props: any, ref) =>
                <input type="email" {...props} />)}
        />
    </>
)