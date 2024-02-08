import React, {useState} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {Address} from './address'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {GridConnection} from './grid-connection'
import {ProjectName} from './project'

type SurveyTabProps = {
    form: UseFormReturn
    isFirst: boolean
    prefix: string
    project: ProjectName
}

export const SurveyTab = ({form, prefix, isFirst, project}: SurveyTabProps) => {
    const isSameAddressName = `${prefix}.address.isSameAddress`
    const isSameAddress = form.watch(isSameAddressName)

    return (
        <>
            {!isFirst && (
                <FormRow
                    label="Is deze netaansluiting op hetzelfde adres als de voorgaande netaansluiting?"
                    name={isSameAddressName}
                    form={form}
                    WrappedInput={BooleanInput} />
            )}
            {!isSameAddress && <Address form={form} prefix={`${prefix}.address`} />}
            <GridConnection form={form} prefix={`${prefix}.gridConnection`} project={project} />
        </>
    )
}