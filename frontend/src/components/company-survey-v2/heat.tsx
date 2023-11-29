import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './boolean-input'
import {HeatingType, HeatingTypeCheckboxes} from './heating-type-checkboxes'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Heat = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch, register} = form

    const heatingTypes: HeatingType[] = watch(`${prefix}.heatingTypes`, [])

    console.log('heatingTypes', heatingTypes)

    return (
        <>
            <h2>3. Warmte</h2>
            <HeatingTypeCheckboxes form={form} prefix={prefix}/>
            {heatingTypes.includes(HeatingType.GAS_BOILER) && (
                <LabelRow label="combinedGasBoilerKw">
                    <NumberInput {...register(`${prefix}.sumGasBoilerKw`)} /> kW
                </LabelRow>
            )}
            {heatingTypes.includes(HeatingType.ELECTRIC_HEATPUMP) && (
                <LabelRow label="Wat is het totaal opgeteld vermogen van jullie elektrische warmtepompen?">
                    <NumberInput {...register(`${prefix}.sumHeatPumpKw`)} /> kW
                </LabelRow>
            )}
            {heatingTypes.includes(HeatingType.HYBRID_HEATPUMP) && (
                <LabelRow label="Wat is het totaal opgeteld elektrisch vermogen van jullie hybride warmtepompen?">
                    <NumberInput {...register(`${prefix}.sumHybridHeatPumpElectricKw`)} /> kW
                </LabelRow>
            )}
            {heatingTypes.includes(HeatingType.DISTRICT_HEATING) && (
                <LabelRow label="Wat is het jaarlijkse warmteverbruik van het warmtenet?">
                    <NumberInput {...register(`${prefix}.annualDistrictHeatingDemandGj`)} /> GJ
                </LabelRow>
            )}
            <LabelRow
                label="Wisselen jullie op een andere manier lokaal warmte uit (bijv. met naastgelegen bedrijven)? Zo ja, hoe?">
                <input type="text" {...form.register(`${prefix}.localHeatExchangeDescription`)} />
            </LabelRow>
            <LabelRow label="Heeft u ongebruikte restwarmte?">
                <BooleanInput form={form} name={`${prefix}.hasUnusedResidualHeat`}/>
            </LabelRow>
        </>
    )
}
