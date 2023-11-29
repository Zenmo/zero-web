import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {TextInput} from './generic/text-input'
import {HeatingType, HeatingTypeCheckboxes} from './heating-type-checkboxes'
import {LabelRow} from './generic/label-row'
import {OldNumberInput} from './generic/old-number-input'

export const Heat = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch, register} = form

    const heatingTypes: HeatingType[] = watch(`${prefix}.heatingTypes`, [])

    return (
        <>
            <h2>3. Warmte</h2>
            <HeatingTypeCheckboxes form={form} prefix={prefix}/>

            {heatingTypes.includes(HeatingType.GAS_BOILER) && (
                <NumberRow
                    label="Wat is het totaal opgeteld vermogen van jullie gasketels?"
                    name={`${prefix}.sumGasBoilerKw`}
                    form={form}
                    suffix="kW" />
            )}
            {heatingTypes.includes(HeatingType.ELECTRIC_HEATPUMP) && (
                <NumberRow
                    label="Wat is het totaal opgeteld vermogen van jullie elektrische warmtepompen?"
                    name={`${prefix}.sumHeatPumpKw`}
                    form={form}
                    suffix="kW" />
            )}
            <NumberRow
                label="Wat is het totaal opgeteld elektrisch vermogen van jullie hybride warmtepompen?"
                name={`${prefix}.sumHybridHeatPumpElectricKw`}
                form={form}
                suffix="kW" />
            {heatingTypes.includes(HeatingType.HYBRID_HEATPUMP) && (
                <NumberRow
                    label="Wat is het totaal opgeteld elektrisch vermogen van jullie hybride warmtepompen?"
                    name={`${prefix}.sumHybridHeatPumpElectricKw`}
                    form={form}
                    suffix="kW" />
            )}
            {heatingTypes.includes(HeatingType.DISTRICT_HEATING) && (
                <NumberRow
                    label="Wat is het jaarlijkse warmteverbruik van het warmtenet?"
                    name={`${prefix}.annualDistrictHeatingDemandGj`}
                    form={form}
                    suffix="GJ" />
            )}
            <FormRow
                label="Wisselen jullie op een andere manier lokaal warmte uit (bijv. met naastgelegen bedrijven)? Zo ja, hoe?"
                name={`${prefix}.localHeatExchangeDescription`}
                form={form}
                InputComponent={TextInput} />
            <FormRow
                label="Heeft u ongebruikte restwarmte?"
                name={`${prefix}.hasUnusedResidualHeat`}
                form={form}
                WrappedInput={BooleanInput} />
        </>
    )
}
