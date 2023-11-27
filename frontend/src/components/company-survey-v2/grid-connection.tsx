import {UseFormReturn} from 'react-hook-form'
import {Address} from './address'
import {Electricity} from './electricity'
import {Heat} from './heat'
import {LabelRow} from './label-row'
import {NaturalGas} from './natural-gas'
import {OpenQuestions} from './open-questions'
import {Storage} from './storage'

export const GridConnection = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    return (
        <>
            <Address form={form} prefix={prefix} />
            <Electricity form={form} prefix={prefix} />
            <Heat form={form} prefix={prefix} />
            <NaturalGas form={form} prefix={prefix} />
            <Storage form={form} prefix={prefix} />
            <OpenQuestions form={form} prefix={prefix} />
        </>
    )
}