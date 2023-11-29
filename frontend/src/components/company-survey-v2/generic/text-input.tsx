import {forwardRef} from 'react'

export const TextInput = forwardRef(
    (props: any, ref) => <input type="text" {...props} ref={ref} />
)