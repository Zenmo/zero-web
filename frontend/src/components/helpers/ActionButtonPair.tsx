import {Button} from "primereact/button";

type ButtonPairProps = {
    showNegative?: boolean
    positiveText?: string
    negativeText?: string
    positiveAction: () => void
    negativeAction?: () => void
    positiveIcon?: string
    negativeIcon?: string
    positiveDisabled?: boolean
    negativeDisabled?: boolean
    positiveLoading?: boolean
    negativeLoading?: boolean
    positiveClassName?: string
    negativeClassName?: string
    className?: string
    positiveButtonType?: 'button' | 'submit' | 'reset'
    negativeButtonType?: 'button' | 'submit' | 'reset'
    size?: 'small' | 'large' | undefined
    positiveSeverity?: "secondary" | "success" | "info" | "warning" | "danger" | "help" | null
    negativeSeverity?: "secondary" | "success" | "info" | "warning" | "danger" | "help" | null
}

export const ActionButtonPair = ({
                                     positiveText = undefined,
                                     negativeText = undefined,
                                     positiveAction,
                                     negativeAction,
                                     positiveIcon,
                                     negativeIcon,
                                     positiveDisabled,
                                     negativeDisabled,
                                     positiveLoading = false,
                                     negativeLoading = false,
                                     positiveClassName = '',
                                     negativeClassName = '',
                                     className = 'd-flex flex-row gap-3 align-items-center',
                                     showNegative = true,
                                     size = 'small',
                                     positiveSeverity = 'secondary',
                                     negativeSeverity = 'danger',
                                     positiveButtonType = 'button',
                                     negativeButtonType = 'button',
                                 }: ButtonPairProps) => {

    return (
        <div className={className}>
            <Button className={`rounded rounded-3 ${positiveClassName}`}
                    label={positiveText ? positiveText : undefined}
                    icon={positiveIcon ? `pi pi-${positiveIcon}` : undefined}
                    loading={positiveLoading}
                    onClick={positiveAction} size={size} disabled={positiveDisabled}
                    severity={positiveSeverity ? positiveSeverity : undefined}
                    unstyled type={positiveButtonType}
            />

            {showNegative &&
                <Button className={`rounded rounded-3 ${negativeClassName}`}
                        label={negativeText ? negativeText : undefined}
                        icon={negativeIcon ? `pi pi-${negativeIcon}` : undefined}
                        loading={negativeLoading}
                        onClick={negativeAction} size={size} disabled={negativeDisabled}
                        severity={negativeSeverity ? negativeSeverity : undefined}
                        type={negativeButtonType}
                />
            }

        </div>
    )
};

