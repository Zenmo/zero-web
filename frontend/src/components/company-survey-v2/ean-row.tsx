import {forwardRef} from "react"
import {FormRow} from "./generic/form-row"

export const EanRow = ({form, name}: {
    form: any,
    name: string,
}) => {
    const min = 10**17
    const max = 10**18-1

    return (
        <FormRow label="EAN-18" name={name} form={form} InputComponent={
            forwardRef((props: any, ref) => (
                <input type="number" ref={ref} {...props} css={{width: '19ch'}} min={min} max={max} />
            ))
        } options={{min, max}}/>
    )
}
