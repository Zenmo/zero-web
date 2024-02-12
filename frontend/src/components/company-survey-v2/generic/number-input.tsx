import {WrappedInputProps} from './form-row'

export const NumberInput = ({form, name, options}: WrappedInputProps) => {
    options = options || {}
    options.valueAsNumber = true
    if (!options.min) {
        options.min = 0
    }

    // TODO: I think I should just override form.register to handle this.
    let min = undefined;
    if (typeof options.min === 'number') {
        min = options.min
    }
    if (typeof options.min === 'object') {
        min = options.min.value
    }

    let max = undefined;
    if (typeof options.max === 'number') {
        max = options.max
    }
    if (typeof options.max === 'object') {
        max = options.max.value
    }

    return (
        <input type="number" {...form.register(name, options)} css={{width: '4rem'}} min={min} max={max} />
    )
}
