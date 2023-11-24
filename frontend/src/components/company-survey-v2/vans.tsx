import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Vans = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numVans = watch('numVans')

    return (
        <>
            <h3>Busjes</h3>
            <LabelRow label="Hoeveel busjes hebben jullie?">
                <NumberInput {...register('numVans')} />
            </LabelRow>
            {numVans > 0 && (
                <>
                    <LabelRow label="Hoeveel elektrische busjes hebben jullie?">
                        <NumberInput {...register('numElectricVans')} />
                    </LabelRow>
                    <LabelRow label={<span>Hoeveel <b>laadpunten</b> voor elektrische busjes hebben jullie?</span>}>
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen van deze laadpunten?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden jullie busjes gemiddeld per jaar?">
                        <NumberInput {...register('annualTravelDistancePerVanKm')} /> km
                    </LabelRow>
                    <LabelRow label="Zijn jullie van plan de bedrijfsvloot (deels) te elektrificeren de komende jaren?
                        Wat is het geplande aantal busjes?">
                        <NumberInput {...register('numPlannedElectricVans')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}