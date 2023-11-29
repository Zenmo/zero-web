import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './generic/label-row'
import {NumberRow} from './generic/number-row'
import {OldNumberInput} from './generic/old-number-input'

export const Cars = ({form, prefix}: { form: UseFormReturn, prefix: string}) => {
    const {register, watch} = form

    const numCars = watch(`${prefix}.numCars`)

    return (
        <>
            <h3>Bedrijfsauto's</h3>
            <NumberRow
                label="Hoeveel bedrijfsauto's hebben jullie in gebruik?"
                name={`${prefix}.numCars`}
                form={form} />
            {numCars > 0 && (
                <>
                    <NumberRow
                        label="Hoeveel van die bedrijfsauto's zijn elektrisch?"
                        name={`${prefix}.numElectricCars`}
                        form={form} />
                    <NumberRow
                        label={<span>Hoeveel <b>laadpunten</b> voor elektrische bedrijfsauto's hebben jullie?</span>}
                        name={`${prefix}.numChargePoints`}
                        form={form} />
                    <NumberRow
                        label="Wat is het maximale laadvermogen per laadpunt?"
                        name={`${prefix}.powerPerChargePointKw`}
                        suffix="kW"
                        form={form} />
                    <NumberRow
                        label="Hoeveel rijden de bedrijfsauto's gemiddeld per jaar (grove inschatting)?"
                        name={`${prefix}.annualTravelDistancePerCarKm`}
                        suffix="km"
                        form={form} />
                    <NumberRow
                        label="Hoeveel van de brandstof bedrijfsauto's zijn jullie van plan te elektrificeren de komende 5 jaar?"
                        name={`${prefix}.numPlannedElectricCars`}
                        form={form} />
                </>
            )}
        </>
    )
}