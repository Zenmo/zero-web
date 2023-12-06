import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {LabelRow} from './generic/label-row'
import {NumberInput} from './generic/number-input'
import {NumberRow} from './generic/number-row'
import {OldNumberInput} from './generic/old-number-input'
import {Purpose, Upload} from './generic/upload'

export const NaturalGas =  ({form, prefix, project}: { form: UseFormReturn , prefix: string, project: string }) => {
    const {watch, register} = form

    const hasConnection = watch(`${prefix}.hasConnection`)

    return (
        <>
            <h2>4. Aardgas</h2>
            <FormRow
                label="Heeft u een gasaansluiting op ditzelfde adres?"
                name={`${prefix}.hasConnection`}
                form={form}
                WrappedInput={BooleanInput}/>
            {hasConnection && (
                <>
                    <NumberRow
                        label="Wat is het jaarlijkse verbruik?"
                        name={`${prefix}.annualDemandM3`}
                        form={form}
                        suffix="m3" />
                    <LabelRow label="Kun je een excel of csv met het uurverbruik van gas uploaden?">
                        <Upload
                            multiple={true}
                            setFormValue={files => form.setValue(`${prefix}.hourlyValuesFiles`, files)}
                            company={form.watch('companyName')}
                            project={project}
                            purpose={Purpose.ELECTRICITY_VALUES} />
                    </LabelRow>
                    <NumberRow
                        label="Welk deel wordt gebruikt voor verwarming van ruimtes? (De rest voor andere processen)"
                        name={`${prefix}.percentageUsedForHeating`}
                        form={form}
                        options={{min: 0, max: 100}}
                        suffix="%" />
                </>
            )}
        </>
    )
}
