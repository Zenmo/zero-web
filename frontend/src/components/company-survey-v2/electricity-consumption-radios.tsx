import {Radio} from "antd";

export enum ConsumptionSpec {
    SPECTRAL_AUTHORIZATION = "SPECTRAL_AUTHORIZATION",
    UPLOAD_QUARTER_HOURLY_VALUES = "UPLOAD_QUARTER_HOURLY_VALUES",
    // Option removed as per feedback
    // ANNUAL_VALUES = "ANNUAL_VALUES",
}

const labels = {
    [ConsumptionSpec.SPECTRAL_AUTHORIZATION]: "Machting voor het ophalen van de meetdata bij Spectral",
    [ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES]: "Kwartierwaarden uploaden",
    // [ConsumptionSpec.ANNUAL_VALUES]: "Jaarverbruik invullen",
}

export const ElectricityConsumptionRadios = ({onChange, consumptionSpec}: {
    consumptionSpec: ConsumptionSpec | null | undefined,
    onChange: (consumptionSpec: ConsumptionSpec | null | undefined) => void
}) => {
    return (
        <Radio.Group onChange={e => onChange(e.target.value)} value={consumptionSpec}>
            <Radio value={ConsumptionSpec.SPECTRAL_AUTHORIZATION}>
                Machting voor het ophalen van de meetdata bij Spectral
            </Radio>
            <Radio value={ConsumptionSpec.UPLOAD_QUARTER_HOURLY_VALUES}>
                Kwartierwaarden uploaden
            </Radio>
        </Radio.Group>
    )
}