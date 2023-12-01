import {forwardRef, FunctionComponent} from 'react'
import {FormRow, FormRowProps} from './form-row'
import {NumberInput} from './number-input'

type TextAreaRowProps = Omit<FormRowProps, "InputComponent" | "WrappedInput">

const TextArea = forwardRef(
    (props: any) => <textarea rows={3} {...props} css={{width: '100%'}}/>
)

export const TextAreaRow: FunctionComponent<TextAreaRowProps> = (props) => {
    return (
        <FormRow {...props} InputComponent={TextArea} />
    )
}
