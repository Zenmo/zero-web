import {css} from '@emotion/react'
import {Alert} from 'antd'
import {createElement, forwardRef, FunctionComponent, useState} from 'react'
import {useForm, UseFormReturn} from 'react-hook-form'
import {FormRow} from './generic/form-row'
import {TextInput} from './generic/text-input'
import {Intro} from './intro'
import {LabelRow} from './generic/label-row'
import {Transport} from './transport'
import {SurveyTabs} from "./survey-tabs";

export const Survey: FunctionComponent = () => {
    // @ts-ignore
    const form: UseFormReturn = useForm({
        shouldUseNativeValidation: true,
        defaultValues: {
            gridConnections: [{}],
        }
    })
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = form

    const [isSuccess, setSuccess] = useState(false)
    const [submissionError, setSubmissionError] = useState("")

    let errorMessage = submissionError
    if (Object.keys(errors).length > 0) {
        errorMessage = "Het formulier bevat fouten"
    }

    const onSubmit = async (data: any) => {
        console.log('submit', data)
        setSubmissionError("")

        data.zenmoProject = "Hessenpoort"

        const url = process.env.ZTOR_URL + '/company-survey'
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                body: JSON.stringify(data)
            })

            if (response.status !== 201) {
                let message = "Er is iets misgegaan."
                setSubmissionError(message)
                const body = await response.json()
                if (body?.error?.message) {
                    message += " Details: " + body.error.message
                    setSubmissionError(message)
                }
            }
        } catch (e) {
            let message = "Er is iets misgegaan."
            // @ts-ignore
            if ('message' in e) {
                message += " Details: " + e.message
            }
            setSubmissionError(message)
            return
        }
    }

    return (
        <div css={{
            width: '100%',
            minHeight: '100vh',
            backgroundColor: 'grey',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'flex-start',
        }}>
            <form onSubmit={handleSubmit(onSubmit)} css={{
                maxWidth: '50rem',
                backgroundColor: 'white',
                padding: '2rem',
                marginTop: '2rem',
                '& input:invalid': {
                    backgroundColor: '#fcc',
                    borderColor: 'red',
                },
                '& h2, & h3': {
                    padding: '.2rem 1rem',
                    backgroundColor: 'lightgrey',
                },
            }}>
                <Intro />
                {errorMessage && <Alert
                    message={errorMessage}
                    type="error"
                    showIcon
                />}
                {isSuccess && <Alert
                    message="Antwoorden opgeslagen. Bedankt voor het invullen."
                    type="success"
                    showIcon
                />}
                <FormRow
                    label="Naam bedrijf"
                    InputComponent={TextInput}
                    name="companyName"
                    form={form}
                    options={{required: true}} />
                <FormRow
                    label="Naam contactpersoon"
                    InputComponent={TextInput}
                    name="personName"
                    form={form}
                    options={{required: true}} />
                <FormRow
                    label="E-mailadres"
                    name="email"
                    form={form}
                    InputComponent={forwardRef((props: any, ref) =>
                        <input type="email" {...props} />)}
                />
                <SurveyTabs form={form} />
                <div css={{textAlign: 'right'}}>
                    <button type="submit">Verstuur</button>
                </div>
            </form>
        </div>
    )
}
