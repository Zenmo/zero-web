import {Radio} from 'antd'
import {useState} from 'react'
import {UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {ConsumptionProfileSelect} from './consumption-profile-select'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {KleinverbruikCapacityInput} from './kleinverbruik-capacity-input'
import {LabelRow} from './generic/label-row'
import {ConsumptionSpec, ElectricityConsumptionRadios} from "./electricity-consumption-radios";

enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const Electricity = ({form, prefix, hasSupplyName}: {
    form: UseFormReturn,
    prefix: string,
    hasSupplyName: string
}) => {
    const hasSupply = form.watch(hasSupplyName)

    const [connectionType, setConnectionType] = useState<ConnectionType>()

    return (
        <>
            {/*<FormRow label="EAN" name={`${prefix}.ean`} form={form} />*/}
            <LabelRow label="Type aansluiting">
                <Radio.Group onChange={e => setConnectionType(e.target.value)} value={connectionType}>
                    <Radio value={ConnectionType.GROOTVERBRUIK} css={{display: 'block'}}>Grootverbruik</Radio>
                    <Radio value={ConnectionType.KLEINVERBRUIK} css={{display: 'block'}}>Kleinverbruik</Radio>
                </Radio.Group>
            </LabelRow>
            {connectionType === ConnectionType.KLEINVERBRUIK && (
                <>
                    <FormRow
                        label="Aansluitwaarde"
                        name={`${prefix}.kleinverbruik.connectionCapacity`}
                        WrappedInput={KleinverbruikCapacityInput}
                        form={form} />
                    {/*{consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (*/}
                    {/*    <FormRow*/}
                    {/*        label="Profiel verbruik"*/}
                    {/*        name={`${prefix}.kleinverbruik.consumptionProfile`}*/}
                    {/*        WrappedInput={ConsumptionProfileSelect}*/}
                    {/*        form={form} />*/}
                    {/*)}*/}
                </>
            )}
            {connectionType === ConnectionType.GROOTVERBRUIK && (
                <>
                    <NumberRow
                        label="Wat is uw gecontracteerde vermogen voor elektriciteitsafname?"
                        name={`${prefix}.grootverbruik.contractedConnectionDemandCapacityKw`}
                        form={form}
                        suffix="kW" />
                    {hasSupply && (
                        <NumberRow
                            label="Wat is uw gecontracteerde vermogen voor elektriciteitsteruglevering?"
                            name={`${prefix}.grootverbruik.contractedConnectionSupplyCapacityKw`}
                            form={form}
                            suffix="kW" />
                    )}
                </>
            )}
        </>
    )
}
