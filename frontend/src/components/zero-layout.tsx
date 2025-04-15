import React, {FunctionComponent, PropsWithChildren, ReactNode} from "react"

export const ZeroLayout: FunctionComponent<PropsWithChildren & {
    subtitle?: string,
    trailingContent?: ReactNode,
}> = ({
          children,
          subtitle,
          trailingContent,
      }) => (
    <div className='content flex-row-fluid align-items-center'>
        <div className={'d-flex justify-content-between align-items-center'}>
            <div>
                {subtitle && <span className={'fs-5 fw-bold text-gray-600 mt-3'}>
            {subtitle}
        </span>}
            </div>
            {trailingContent && <div>{trailingContent}</div>}
        </div>
        <div className={'mt-5'}>
            {children}
        </div>
    </div>
)
