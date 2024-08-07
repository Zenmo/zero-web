import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {TextAreaRow} from './generic/text-area-row'
import {HeatingType, HeatingTypeCheckboxes} from './heating-type-checkboxes'

export const Heat = ({form, prefix}: { form: UseFormReturn, prefix: string }) => {
    const {watch, register} = form

    const heatingTypes: HeatingType[] = watch(`${prefix}.heatingTypes`, [])

    return (
        <>
            <h2>3. Warmte</h2>
            <HeatingTypeCheckboxes form={form} prefix={prefix}/>

            {heatingTypes.includes(HeatingType.GAS_BOILER) && (
                <NumberRow
                    label="Wat is het totaal opgeteld vermogen van uw gasketels?"
                    name={`${prefix}.sumGasBoilerKw`}
                    form={form}
                    suffix="kW" />
            )}
            {heatingTypes.includes(HeatingType.ELECTRIC_HEATPUMP) && (
                <NumberRow
                    label="Wat is het totaal opgeteld vermogen van uw elektrische warmtepompen?"
                    name={`${prefix}.sumHeatPumpKw`}
                    form={form}
                    suffix="kW" />
            )}
            {heatingTypes.includes(HeatingType.HYBRID_HEATPUMP) && (
                <NumberRow
                    label="Wat is het totaal opgeteld elektrisch vermogen van uw hybride warmtepompen?"
                    name={`${prefix}.sumHybridHeatPumpElectricKw`}
                    form={form}
                    suffix="kW" />
            )}
            {heatingTypes.includes(HeatingType.DISTRICT_HEATING) && (
                <NumberRow
                    label="Wat is het jaarlijkse warmteverbruik van het warmtenet?"
                    name={`${prefix}.annualDistrictHeatingDelivery_GJ`}
                    form={form}
                    suffix="GJ" />
            )}
            <TextAreaRow
                label="Wisselt u lokaal warmte uit (bijv. met naastgelegen bedrijven)? Zo ja, hoe?"
                form={form}
                name={`${prefix}.localHeatExchangeDescription`} />
            <FormRow
                label="Heeft u een significante hoeveelheid ongebruikte restwarmte?"
                name={`${prefix}.hasUnusedResidualHeat`}
                form={form}
                WrappedInput={BooleanInput} />
        </>
    )
}
