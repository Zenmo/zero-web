import React, {FunctionComponent, PropsWithChildren} from "react"
import {ZeroHeader} from "./zero-header"

export const ZeroLayout: FunctionComponent<PropsWithChildren> = ({children}) => (
    <div>
        <div>
            <img
                src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png"
                style={{height: '1em', verticalAlign: 'sub'}}/>
            &nbsp;
            Zenmo Zero
        </div>
        <div>
            {children}
        </div>
    </div>
)
