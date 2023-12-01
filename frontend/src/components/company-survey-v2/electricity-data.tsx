import {LabelRow} from "./generic/label-row";
import {ConsumptionSpec, ElectricityConsumptionRadios} from "./electricity-consumption-radios";
import {FormRow} from "./generic/form-row";
import {BooleanInput} from "./generic/boolean-input";
import {useState} from "react";
import {UseFormReturn} from "react-hook-form";


export const ElectricityData = ({form, prefix, hasSupplyName}: {
    form: UseFormReturn,
    prefix: string,
    hasSupplyName: string
}) => {
    const {register} = form

    const [consumptionSpec, setConsumptionSpec] = useState<ConsumptionSpec | null | undefined>()

    return (
        <>
            <h3>Data</h3>
            <LabelRow label="Hoe wilt u het elektriciteitsprofiel van deze netaansluiting doorgeven?">
                <ElectricityConsumptionRadios onChange={setConsumptionSpec} consumptionSpec={consumptionSpec}/>
            </LabelRow>
            {consumptionSpec === ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES && (
                <LabelRow label="Kwartierwaarden">
                    <input type="file" {...register(`${prefix}.quarterHourlyValuesFiles`)} />
                </LabelRow>
            )}
            {consumptionSpec === ConsumptionSpec.SPECTRAL_AUTHORIZATION && (
                <p>Spectral machtiging TODO</p>
            )}
            {/*{consumptionSpec === ConsumptionSpec.ANNUAL_VALUES && (*/}
            {/*    <>*/}
            {/*        <NumberRow*/}
            {/*            label="Jaarverbruik"*/}
            {/*            name={`${prefix}.annualElectricityDemandKwh`}*/}
            {/*            form={form}*/}
            {/*            suffix="kWh" />*/}
            {/*        {hasSupply && (*/}
            {/*            <NumberRow*/}
            {/*                label="Jaaropwek"*/}
            {/*                name={`${prefix}.annualElectricityProductionKwh`}*/}
            {/*                form={form}*/}
            {/*                suffix="kWh" />*/}
            {/*        )}*/}
            {/*    </>*/}
            {/*)}*/}
        </>
    )
}