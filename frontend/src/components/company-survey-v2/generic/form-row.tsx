import {css} from '@emotion/react'
import {get} from 'lodash'
import {FunctionComponent, ReactNode} from 'react'
import {FieldError, UseFormReturn} from 'react-hook-form'
import {RegisterOptions} from 'react-hook-form/dist/types/validator'

export type WrappedInputProps = {
    name: string,
    form: UseFormReturn,
    options?: RegisterOptions
}

export type FormRowProps = {
    label: string | ReactNode,
    name: string,
    form: UseFormReturn,

    // pick one of either InputComponent or WrappedInput
    InputComponent?: FunctionComponent | 'input' | 'textarea',
    WrappedInput?: FunctionComponent<WrappedInputProps>

    suffix?: string, // like a unit to put after a number input
    options?: RegisterOptions,
}

export const FormRow = ({label, InputComponent, WrappedInput, suffix, name, options, form}: FormRowProps) => {
    const errors = form.formState.errors
    // @ts-ignore
    const error: FieldError | undefined = get(errors, name)
    const errorMessage = getErrorMessage(error)
    options = setValidationMessage(options)

    return (
        <label css={css`
            display: flex;

            & > div:nth-of-type(1) {
                text-align: right;
                width: 50%;
                padding: 0.3rem;
            }

            & > div:nth-of-type(2) {
                width: 50%;
                padding: 0.3rem;
            }
        `}>
            <div>
                {options?.required && <span css={{color: 'red'}}>* </span>}
                {label}
            </div>
            <div>
                <div>

                    {InputComponent && <InputComponent {...form.register(name, options)} />}
                    {WrappedInput && <WrappedInput name={name} form={form} />}
                    {suffix}
                </div>
                {errorMessage && <div css={{color: 'red'}}>{errorMessage}</div>}
            </div>
        </label>
    )
}

/**
 * We need to do this to use the native validation of the browser.
 * See https://react-hook-form.com/docs/useform#shouldUseNativeValidation
 */
function setValidationMessage(options?: RegisterOptions): RegisterOptions | undefined {
    if (!options) {
        return options
    }

    if (options.required === true) {
        options = {
            ...options,
            required: defaultErrorMessages.required,
        }
    }

    for (const [key, message] of Object.entries(defaultErrorMessages)) {
        if (key === 'required') {
            continue
        }

        // @ts-ignore
        const value = options[key]
        if (value === undefined) {
            continue
        }

        if (typeof value === 'object' && value.message) {
            continue
        }

        options = {
            ...options,
            [key]: {
                value,
                message,
            }
        }
    }

    return options
}

function getErrorMessage(error: FieldError | undefined): string | undefined {
    if (!error) {
        return undefined
    }

    if (error.message) {
        return error.message
    }

    const defaultMessage = defaultErrorMessages[error.type]

    if (defaultMessage) {
        return defaultMessage
    }

    return 'Verkeerde waarde'
}

export const defaultErrorMessages: Partial<{ [key in (FieldError['type'])]: string }> = {
    required: 'Dit veld is verplicht',
    pattern: 'Verkeerd formaat',
    min: 'Te laag',
    max: 'Te hoog',
    minLength: 'Te kort',
    maxLength: 'Te lang',
}
