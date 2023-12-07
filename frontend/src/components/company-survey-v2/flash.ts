import {css} from '@emotion/react'

const lightYellow = '#ffb'

// need to pass this at least once to a component on the page
export const defineFlash = css`
    @keyframes flash {
        0% {
            background-color: ${lightYellow};
        }
        50% {
            background-color: ${lightYellow};
        }
        100% {
            background-color: transparent;
        }
    }`

// use this to apply the flash animation
export const flash = css`
    animation: flash 1s;
`