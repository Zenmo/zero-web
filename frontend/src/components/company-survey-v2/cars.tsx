import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Cars = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numCars = watch('numCars')

    return (
        <>
            <h3>Bedrijfsauto's</h3>
            <LabelRow label="Hoeveel bedrijfsauto's hebben jullie in gebruik?">
                <NumberInput {...register('numCars')} />
            </LabelRow>
            {numCars > 0 && (
                <>
                    <LabelRow label="Hoeveel van die bedrijfsauto's zijn elektrisch?">
                        <NumberInput {...register('numElectricCars')} />
                    </LabelRow>
                    <LabelRow label={<span>Hoeveel <b>laadpunten</b> voor elektrische bedrijfsauto's hebben jullie?</span>}>
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen per laadpunt?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden de bedrijfsauto's gemiddeld per jaar (grove inschatting)?">
                        <NumberInput {...register('annualTravelDistancePerCarKm')} /> km
                    </LabelRow>
                    <LabelRow label="Hoeveel van de brandstof bedrijfsauto's zijn jullie van plan te elektrificeren de komende 5 jaar?">
                        <NumberInput {...register('numPlannedElectricCars')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}