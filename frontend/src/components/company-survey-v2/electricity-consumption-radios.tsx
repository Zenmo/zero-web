import {Radio} from 'antd'
import {ProjectConfiguration} from './project'

export enum ConsumptionSpec {
    PLACEHOLDER_AUTHORIZATION = "PLACEHOLDER_AUTHORIZATION",
    PDF_AUTHORIZATION = "PDF_AUTHORIZATION",
    UPLOAD_QUARTER_HOURLY_VALUES = "UPLOAD_QUARTER_HOURLY_VALUES",
    // Option removed as per feedback
    // ANNUAL_VALUES = "ANNUAL_VALUES",
}

const labels = {
    [ConsumptionSpec.PLACEHOLDER_AUTHORIZATION]: "Ik wil jullie machtigen voor het ophalen van de meetdata",
    [ConsumptionSpec.PDF_AUTHORIZATION]: "Ik wil jullie machtigen voor het ophalen van de meetdata",
    [ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES]: "Kwartierwaarden uploaden",
    // [ConsumptionSpec.ANNUAL_VALUES]: "Jaarverbruik invullen",
}

export const ElectricityConsumptionRadios = ({onChange, consumptionSpec, project}: {
    consumptionSpec: ConsumptionSpec | null | undefined
    onChange: (consumptionSpec: ConsumptionSpec | null | undefined) => void
    project: ProjectConfiguration
}) => {
    return (
        <Radio.Group onChange={e => onChange(e.target.value)} value={consumptionSpec}>
            {project.authorizationPdf ? (
                <Radio value={ConsumptionSpec.PDF_AUTHORIZATION}>
                    Ik wil jullie machtigen voor het ophalen van de meetdata
                </Radio>
            ) :
                <Radio value={ConsumptionSpec.PLACEHOLDER_AUTHORIZATION}>
                    Ik wil jullie machtigen voor het ophalen van de meetdata
                </Radio>
            }
            <Radio value={ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES}>
                Kwartierwaarden uploaden
            </Radio>
        </Radio.Group>
    )
}