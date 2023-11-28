import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Vans = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numVans = watch('numVans')

    return (
        <>
            <h3>Busjes</h3>
            <LabelRow label="Hoeveel bestelbusjes hebben jullie in gebruik?">
                <NumberInput {...register('numVans')} />
            </LabelRow>
            {numVans > 0 && (
                <>
                    <LabelRow label="Hoeveel van die bestelbusjes zijn elektrisch?">
                        <NumberInput {...register('numElectricVans')} />
                    </LabelRow>
                    <LabelRow label={<span>Hoeveel <b>laadpunten</b> voor elektrische busjes hebben jullie?</span>}>
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen per laadpunt?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden jullie busjes gemiddeld per jaar (grove inschatting)?">
                        <NumberInput {...register('annualTravelDistancePerVanKm')} /> km
                    </LabelRow>
                    <LabelRow label="Hoeveel van de brandstof busjes zijn jullie van plan te elektrificeren de komende 5 jaar?">
                        <NumberInput {...register('numPlannedElectricVans')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}