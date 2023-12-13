import {Radio} from "antd";
import {ProjectName} from './project'

export enum ConsumptionSpec {
    PLACEHOLDER_AUTHORIZATION = "PLACEHOLDER_AUTHORIZATION",
    SPECTRAL_AUTHORIZATION = "SPECTRAL_AUTHORIZATION",
    UPLOAD_QUARTER_HOURLY_VALUES = "UPLOAD_QUARTER_HOURLY_VALUES",
    // Option removed as per feedback
    // ANNUAL_VALUES = "ANNUAL_VALUES",
}

const labels = {
    [ConsumptionSpec.PLACEHOLDER_AUTHORIZATION]: "Machting voor het ophalen van de meetdata",
    [ConsumptionSpec.SPECTRAL_AUTHORIZATION]: "Machting voor het ophalen van de meetdata",
    [ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES]: "Kwartierwaarden uploaden",
    // [ConsumptionSpec.ANNUAL_VALUES]: "Jaarverbruik invullen",
}

export const ElectricityConsumptionRadios = ({onChange, consumptionSpec, project}: {
    consumptionSpec: ConsumptionSpec | null | undefined
    onChange: (consumptionSpec: ConsumptionSpec | null | undefined) => void
    project: ProjectName
}) => {
    return (
        <Radio.Group onChange={e => onChange(e.target.value)} value={consumptionSpec}>
            {project === 'Hessenpoort' && (
                <Radio value={ConsumptionSpec.SPECTRAL_AUTHORIZATION}>
                    Machting voor het ophalen van de meetdata
                </Radio>
            )}
            {project !== 'Hessenpoort' && (
                <Radio value={ConsumptionSpec.PLACEHOLDER_AUTHORIZATION}>
                    Machting voor het ophalen van de meetdata
                </Radio>
            )}
            <Radio value={ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES}>
                Kwartierwaarden uploaden
            </Radio>
        </Radio.Group>
    )
}