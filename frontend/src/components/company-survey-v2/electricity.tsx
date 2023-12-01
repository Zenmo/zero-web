import {Radio} from 'antd'
import {useState} from 'react'
import {Controller, UseFormReturn} from 'react-hook-form'
import {BooleanInput} from './generic/boolean-input'
import {ConsumptionProfileSelect} from './consumption-profile-select'
import {FormRow} from './generic/form-row'
import {NumberRow} from './generic/number-row'
import {KleinverbruikCapacityInput} from './kleinverbruik-capacity-input'
import {LabelRow} from './generic/label-row'
import {OldNumberInput} from './generic/old-number-input'
import {Supply} from './supply'

enum ConsumptionSpec {
    SPECTRAL_AUTHORIZATION = "SPECTRAL_AUTHORIZATION",
    UPLOAD_QUARTER_HOURLY_VALUES = "UPLOAD_QUARTER_HOURLY_VALUES",
    ANNUAL_VALUES = "ANNUAL_VALUES",
}

enum ConnectionType {
    GROOTVERBRUIK = "GROOTVERBRUIK",
    KLEINVERBRUIK = "KLEINVERBRUIK",
}

export const Electricity = ({form, prefix, hasSupplyName}: {
    form: UseFormReturn,
    prefix: string,
    hasSupplyName: string
}) => {
    const {register} = form

    const hasSupply = form.watch(hasSupplyName)

    const [consumptionSpec, setConsumptionSpec] = useState<ConsumptionSpec>()
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
            <LabelRow label="Hoe wilt u het elektriciteitsprofiel van deze netaansluiting doorgeven?">
                <Radio.Group onChange={e => setConsumptionSpec(e.target.value)} value={consumptionSpec}>
                    <Radio value={ConsumptionSpec.SPECTRAL_AUTHORIZATION} css={{display: 'block'}}>Machting voor het ophalen van de meetdata bij Spectral</Radio>
                    <Radio value={ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES} css={{display: 'block'}}>Kwartierwaarden uploaden</Radio>
                    <Radio value={ConsumptionSpec.ANNUAL_VALUES} css={{display: 'block'}}>Jaarverbruik invullen</Radio>
                </Radio.Group>
            </LabelRow>
            <FormRow
                label="Is er ook elektriciteitsopwek op deze netaansluiting?"
                name={hasSupplyName}
                form={form}
                WrappedInput={BooleanInput} />
            {consumptionSpec === ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES && (
                <LabelRow label="Kwartierwaarden">
                    <input type="file" {...register(`${prefix}.quarterHourlyValuesFiles`)} />
                </LabelRow>
            )}
            {consumptionSpec === ConsumptionSpec.SPECTRAL_AUTHORIZATION && (
                <p>Spectral machtiging TODO</p>
            )}
            {consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (
                <>
                    <NumberRow
                        label="Jaarverbruik"
                        name={`${prefix}.annualElectricityDemandKwh`}
                        form={form}
                        suffix="kWh" />
                    {hasSupply && (
                        <NumberRow
                            label="Jaaropwek"
                            name={`${prefix}.annualElectricityProductionKwh`}
                            form={form}
                            suffix="kWh" />
                    )}
                </>
            )}
            {connectionType === ConnectionType.KLEINVERBRUIK && (
                <>
                    <FormRow
                        label="Aansluitwaarde"
                        name={`${prefix}.kleinverbruik.connectionCapacity`}
                        WrappedInput={KleinverbruikCapacityInput}
                        form={form} />
                    {consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (
                        <FormRow
                            label="Profiel verbruik"
                            name={`${prefix}.kleinverbruik.consumptionProfile`}
                            WrappedInput={ConsumptionProfileSelect}
                            form={form} />
                    )}
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
