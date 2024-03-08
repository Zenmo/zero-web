import {FunctionComponent} from 'react'
import {FormRow, FormRowProps} from './form-row'
import {NumberInput} from './number-input'

export type NumberRowProps = Omit<FormRowProps, "InputComponent" | "WrappedInput">

export const NumberRow: FunctionComponent<NumberRowProps> = (props) => {
    return (
        <FormRow {...props} WrappedInput={NumberInput} />
    )
}
