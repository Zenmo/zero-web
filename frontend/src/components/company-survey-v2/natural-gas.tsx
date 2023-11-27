import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const NaturalGas =  ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    const {watch, register} = form

    const hasConnection = watch(`${prefix}.hasConnection`)

    return (
        <>
            <h2>Aardgas</h2>
            <LabelRow label={"Heeft u een gasaansluiting op ditzelfde adres?"}>
                <BooleanInput form={form} name={`${prefix}.hasConnection`} />
            </LabelRow>
            {hasConnection && (
                <>
                    <LabelRow label={"Wat is het jaarlijkse verbruik?"}>
                        <input type="number" {...register(`${prefix}.annualDemandM3`)} /> m3
                    </LabelRow>
                    <LabelRow label={"Kun je een excel of csv met het uurverbruik van gas uploaden?"}>
                        <input type="file" />
                    </LabelRow>
                    <LabelRow label="Welk deel wordt gebruikt voor verwarming van ruimtes? (De rest voor andere processen)">
                        <NumberInput form={form} {...register(`${prefix}.percentageUsedForHeating`, {min: 0, max: 100})} /> %
                    </LabelRow>
                </>
            )}
        </>
    )
}
