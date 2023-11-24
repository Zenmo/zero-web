import {forwardRef} from 'react'

export const NumberInput = forwardRef(
    (props: any, ref) => <input type="number" ref={ref} {...props} css={{width: '5rem'}}/>
)
