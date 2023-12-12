import {LabelRow} from "./generic/label-row";
import {ConsumptionSpec, ElectricityConsumptionRadios} from "./electricity-consumption-radios";
import {FormRow} from "./generic/form-row";
import {BooleanInput} from "./generic/boolean-input";
import {useState} from "react";
import {UseFormReturn} from "react-hook-form";
import {Purpose, Upload} from './generic/upload'
import {ProjectName} from './project'


export const ElectricityData = ({form, prefix, hasSupplyName, project}: {
    form: UseFormReturn,
    prefix: string,
    hasSupplyName: string
    project: ProjectName
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
                    <Upload
                        multiple={true}
                        setFormValue={files => form.setValue(`${prefix}.quarterHourlyValuesFiles`, files)}
                        company={form.watch('companyName')}
                        project={project}
                        purpose={Purpose.ELECTRICITY_VALUES} />
                </LabelRow>
            )}
            {consumptionSpec === ConsumptionSpec.SPECTRAL_AUTHORIZATION && (
                <LabelRow label={<div>Download <a href="/spectral-machtiging.pdf" target="_blank">dit formulier</a> en scan het in</div>}>
                    <Upload
                        multiple={false}
                        setFormValue={file => form.setValue(`${prefix}.authorizationFile`, file)}
                        company={form.watch('companyName')}
                        project={project}
                        purpose={Purpose.ELECTRICITY_AUTHORIZATION} />
                </LabelRow>
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