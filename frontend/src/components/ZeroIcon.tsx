import React from 'react'
import icons from '../components/icons-config/icons'

type Props = {
    className?: string
    iconType?: 'duotone' | 'solid' | 'outline'
    iconName: string
}

const ZeroIcon: React.FC<Props> = ({className = '', iconType, iconName}) => {

    return (
        <i className={`ki-${iconType} ki-${iconName}${className && ' ' + className}`}>
            {iconType === 'duotone' &&
                [...Array(icons[iconName])].map((e, i) => {
                    return (
                        <span
                            key={`${iconType}-${iconName}-${className}-path-${i + 1}`}
                            className={`path${i + 1}`}
                        ></span>
                    )
                })}
        </i>
    )
}

export {ZeroIcon}
