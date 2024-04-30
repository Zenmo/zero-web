import {Children, FunctionComponent} from "react";
import {css} from "@emotion/react";

/**
 * Link in the style of a button
 */
export const ButtonLink: FunctionComponent<{href: string, children: any}> = ({href, children}) => (
    <a href={href} className="p-button" css={{
        textDecoration: 'none',
        whiteSpace: 'nowrap',
    }}>
        {children}
    </a>
)