import {css} from '@emotion/react'
import {FunctionComponent, PropsWithChildren, ReactElement, ReactNode} from 'react'
import {flash} from '../flash'

export const LabelRow = ({label, children}: PropsWithChildren<{label: any}>) => {
    return (
        <label css={[css`
            display: flex;
        `, flash]}>
            <div css={css`
                text-align: right;
                width: 50%;
                padding: 0.3rem;

                display: flex;
                flex-direction: column;
                justify-content: center;
            `}>
                {label}
            </div>
            <div css={css`
                width: 50%;
                padding: 0.3rem;

                display: flex;
                flex-direction: column;
                justify-content: center;
            `}>
                {children}
            </div>
        </label>
    )
}