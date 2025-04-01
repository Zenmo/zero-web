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
    positiveSeverity?: "secondary" | "success" | "info" | "warning" | "danger" | "help" | undefined
    negativeSeverity?: "secondary" | "success" | "info" | "warning" | "danger" | "help" | undefined
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
            <Button className={`rounded rounded-3 ${positiveClassName}`} label={positiveText}
                    icon={`pi pi-${positiveIcon}`}
                    loading={positiveLoading}
                    onClick={positiveAction} size={size} disabled={positiveDisabled}
                    severity={positiveSeverity} unstyled type={positiveButtonType}
            />

            {showNegative &&
                <Button className={`rounded rounded-3 ${negativeClassName}`} label={negativeText}
                        icon={`pi pi-${negativeIcon}`}
                        loading={negativeLoading}
                        onClick={negativeAction} size={size} disabled={negativeDisabled}
                        severity={negativeSeverity} type={negativeButtonType}
                />
            }

        </div>
    )
};

