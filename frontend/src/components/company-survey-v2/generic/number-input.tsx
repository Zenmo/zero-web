
import {WrappedInputProps} from './form-row'

export const NumberInput = ({form, name, options}: WrappedInputProps) => {
    options = options || {}
    options.valueAsNumber = true
    if (!options.min) {
        options.min = 0
    }

    return (
        <input type="number" {...form.register(name, options)} css={{width: '4rem'}}/>
    )
}
