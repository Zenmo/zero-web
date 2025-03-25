import React, {FunctionComponent, PropsWithChildren, ReactNode} from "react"

export const ZeroLayout: FunctionComponent<PropsWithChildren & {
    subtitle?: string,
    trailingContent?: ReactNode,
}> = ({
    children,
    subtitle,
    trailingContent,
}) => (
    <div style={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
    }}>
        <div style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            paddingLeft: "1em",
        }}>
            <h1>
                <img
                    alt="Zenmo logo"
                    src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png"
                    style={{height: "1em", verticalAlign: "sub"}}/>
                &nbsp;
                Zenmo Zero
            </h1>
        {trailingContent && <div style={{ paddingRight: "1em" }}>{trailingContent}</div>}
        </div>
        {subtitle && <h3 style={{
            padding: "0 1em 1em 1rem",
            margin: 0,
        }}>
            {subtitle}
        </h3>}
        {children}
    </div>
)
