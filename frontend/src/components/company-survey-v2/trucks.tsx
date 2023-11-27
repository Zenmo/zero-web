import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Trucks = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numTrucks = watch('numTrucks')

    return (
        <>
            <h3>Vrachtwagens</h3>
            <LabelRow label="Hoeveel vrachtwagens hebben jullie?">
                <NumberInput {...register('numTrucks')} />
            </LabelRow>
            {numTrucks > 0 && (
                <>
                    <LabelRow label="Hoeveel elektrische vrachtwagens hebben jullie?">
                        <NumberInput {...register('numElectricTrucks')} />
                    </LabelRow>
                    <LabelRow label="Hoeveel laadpunten voor elektrische vrachtwagens hebben jullie?">
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen van deze laadpunten?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden jullie vrachtwagens gemiddeld per jaar?">
                        <NumberInput {...register('annualTravelDistancePerTruckKm')} /> km
                    </LabelRow>
                    <LabelRow label="Zijn jullie van plan de bedrijfsvloot (deels) te elektrificeren de komende jaren?
                        Wat is het geplande aantal elektrische vrachtwagens?">
                        <NumberInput {...register('numPlannedElectricTrucks')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}