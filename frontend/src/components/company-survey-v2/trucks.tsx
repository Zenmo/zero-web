import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Trucks = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numTrucks = watch('numTrucks')

    return (
        <>
            <h3>Vrachtwagens</h3>
            <LabelRow label="Hoeveel vrachtwagens hebben jullie in gebruik?">
                <NumberInput {...register('numTrucks')} />
            </LabelRow>
            {numTrucks > 0 && (
                <>
                    <LabelRow label="Hoeveel van die vrachtwagens zijn elektrisch?">
                        <NumberInput {...register('numElectricTrucks')} />
                    </LabelRow>
                    <LabelRow label="Hoeveel laadpunten voor elektrische vrachtwagens hebben jullie?">
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen van per laadpunt?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden jullie vrachtwagens gemiddeld per jaar (grove inschatting)?">
                        <NumberInput {...register('annualTravelDistancePerTruckKm')} /> km
                    </LabelRow>
                    <LabelRow label="Hoeveel van de brandstof vrachtwagens zijn jullie van plan te elektrificeren de komende 5 jaar?">
                        <NumberInput {...register('numPlannedElectricTrucks')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}