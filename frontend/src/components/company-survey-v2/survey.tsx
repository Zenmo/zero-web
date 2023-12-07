import {css} from '@emotion/react'
import {Alert} from 'antd'
import {createElement, forwardRef, FunctionComponent, useState} from 'react'
import {useForm, UseFormReturn} from 'react-hook-form'
import {Address} from './address'
import {BasicData} from './basic-data'
import {FormRow} from './generic/form-row'
import {TextInput} from './generic/text-input'
import {GridConnection} from './grid-connection'
import {HasMultipleConnections} from './has-multiple-connections'
import {Intro} from './intro'
import {LabelRow} from './generic/label-row'
import {Project} from './project'
import {Transport} from './transport'
import {SurveyTabs} from "./survey-tabs";
import {cloneDeep} from "lodash";

export const Survey: FunctionComponent<{project: Project}> = ({project}) => {
    // @ts-ignore
    const form: UseFormReturn = useForm({
        shouldUseNativeValidation: true,
        defaultValues: {
            tabs: [
                {
                    address: {},
                    gridConnection: {},
                }
            ],
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

    const onSubmit = async (surveyData: any) => {
        console.log('submit', surveyData)
        surveyData = cloneDeep(surveyData)
        setSubmissionError("")

        surveyData.zenmoProject = project.name
        surveyData.addresses = []

        for (const tab of surveyData.tabs) {
            if (tab.address.isSameAddress) {
                surveyData.addresses[surveyData.address.length - 1].gridConnections.push(tab.gridConnection)
            } else {
                surveyData.addresses.push({
                    ...tab.address,
                    gridConnections: [tab.gridConnection]
                })
            }
        }

        delete surveyData.tabs

        const url = process.env.ZTOR_URL + '/company-survey'
        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                body: JSON.stringify(surveyData)
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

    const [hasMultipleConnections, setMultipleConnections] = useState()

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
                boxShadow: '0 0 .15rem .15em white',
                '& input:invalid': {
                    backgroundColor: '#fcc',
                    borderColor: 'red',
                },
                '& h1, & h2, & h3': {
                    padding: '.2rem 1rem',
                    borderBottom: '1px solid #ccc',
                },
                'input[type="text"], input[type="email"], input[type="number"], textarea': {
                    padding: '.3rem',
                }
            }}>
                <Intro project={project} />
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
                <BasicData form={form} />
                <br />
                <HasMultipleConnections hasMultipleConnections={hasMultipleConnections} setMultipleConnections={setMultipleConnections} />
                <br />
                {hasMultipleConnections === false && (
                    <>
                        <Address form={form} prefix="tabs.0.address" />
                        <GridConnection form={form} prefix="tabs.0.gridConnection" project={project.name} />
                    </>
                )}
                {hasMultipleConnections === true && (
                    <>
                        <SurveyTabs form={form} project={project.name} />
                    </>
                )}

                <div css={{textAlign: 'right'}}>
                    <button type="submit">Verstuur</button>
                </div>
            </form>
        </div>
    )
}
