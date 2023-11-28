import {css} from '@emotion/react'
import {get} from 'lodash'
import {FunctionComponent} from 'react'
import {FieldError, UseFormReturn} from 'react-hook-form'
import {RegisterOptions} from 'react-hook-form/dist/types/validator'

type FormRowProps = {
    label: string,
    InputComponent: FunctionComponent | 'input' | 'textarea',
    suffix?: string,
    name: string,
    options?: RegisterOptions,
    form: UseFormReturn,
}

export const FormRow = ({label, InputComponent, suffix, name, options, form}: FormRowProps) => {
    const errors = form.formState.errors
    // @ts-ignore
    const error: FieldError | undefined = get(errors, name)
    const errorMessage = getErrorMessage(error)
    setValidationMessage(options)

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
            <div>{label}</div>
            <div>
                <div>
                    <InputComponent {...form.register(name, options)} />
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
function setValidationMessage(options?: RegisterOptions) {
    if (!options) {
        return
    }

    if (options.required === true) {
        options.required = defaultErrorMessages.required
    }

    for (const [key, message] of Object.entries(defaultErrorMessages)) {
        // @ts-ignore
        const value = options[key]

        if (value !== undefined) {
            // @ts-ignore
            options[key] = {
                value,
                message,
            }
        }
    }
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
