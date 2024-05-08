import React, {FunctionComponent, PropsWithChildren} from "react"

export const ZeroLayout: FunctionComponent<PropsWithChildren & {
    subtitle?: string,
}> = ({
    children,
    subtitle,
}) => (
    <div>
        <h1 style={{
            paddingLeft: "1em",
        }}>
            <img
                alt="Zenmo logo"
                src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png"
                style={{height: "1em", verticalAlign: "sub"}}/>
            &nbsp;
            Zenmo Zero
        </h1>
        {subtitle && <h3 style={{paddingLeft: "5rem"}}>{subtitle}</h3>}
        {children}
    </div>
)