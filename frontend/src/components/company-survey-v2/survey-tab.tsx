import React, {useState} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {Address} from './address'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {GridConnection} from './grid-connection'

type SurveyTabProps = {
    form: UseFormReturn
    isFirst: boolean
    prefix: string
}

export const SurveyTab = ({form, prefix, isFirst}: SurveyTabProps) => {
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
            {!isSameAddress && <Address form={form} prefix={`${prefix}.adress`} />}
            <GridConnection form={form} prefix={`${prefix}.gridConnection`} />
        </>
    )
}