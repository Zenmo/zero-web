import {forwardRef} from 'react'

export const OldNumberInput = forwardRef(
    (props: any, ref) =>
        <input type="number" ref={ref} {...props} css={{width: '5rem'}}/>
)
