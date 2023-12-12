import {UseFormReturn} from 'react-hook-form'
import {NumberRow} from './generic/number-row'
import {ProjectName} from './project'

export const Trucks = ({form, prefix, project}: { form: UseFormReturn, prefix: string, project: ProjectName}) => {
    const {register, watch} = form

    const numTrucks = watch(`${prefix}.numTrucks`)

    return (
        <>
            <h3>Vrachtwagens</h3>
            <NumberRow
                label="Hoeveel vrachtwagens hebben jullie in gebruik?"
                name={`${prefix}.numTrucks`}
                form={form} />
            {numTrucks > 0 && (
                <>
                    <NumberRow
                        label="Hoeveel van die vrachtwagens zijn elektrisch?"
                        name={`${prefix}.numElectricTrucks`}
                        form={form} />
                    <NumberRow
                        label="Hoeveel laadpunten voor elektrische vrachtwagens hebben jullie?"
                        name={`${prefix}.numChargePoints`}
                        form={form} />
                    <NumberRow
                        label="Wat is het maximale laadvermogen van per laadpunt?"
                        name={`${prefix}.powerPerChargePointKw`}
                        form={form}
                        suffix="kW" />
                    <NumberRow
                        label="Hoeveel rijden jullie vrachtwagens gemiddeld per jaar (grove inschatting)?"
                        name={`${prefix}.annualTravelDistancePerTruckKm`}
                        form={form}
                        suffix="km" />
                    <NumberRow
                        label="Hoeveel van de brandstof vrachtwagens zijn jullie van plan te elektrificeren de komende 5 jaar?"
                        name={`${prefix}.numPlannedElectricTrucks`}
                        form={form} />
                    {project === 'De Wieken' && (
                        <NumberRow
                            label="Hoeveel van de brandstof vrachtwagens zijn jullie van plan aan te drijven met waterstof de komende 5 jaar?"
                            name={`${prefix}.numPlannedHydrogenTrucks`}
                            form={form} />
                    )}
                </>
            )}
        </>
    )
}