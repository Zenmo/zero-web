import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'
import {NumberInput} from './number-input'

export const Cars = ({form}: { form: UseFormReturn }) => {
    const {register, watch} = form

    const numCars = watch('numCars')

    return (
        <>
            <h3>Bedrijfsauto's</h3>
            <LabelRow label="Hoeveel auto's hebben jullie?">
                <NumberInput {...register('numCars')} />
            </LabelRow>
            {numCars > 0 && (
                <>
                    <LabelRow label="Hoeveel elektrische auto's hebben jullie?">
                        <NumberInput {...register('numElectricCars')} />
                    </LabelRow>
                    <LabelRow label={<span>Hoeveel <b>laadpunten</b> voor elektrische auto's hebben jullie?</span>}>
                        <NumberInput {...register('numChargePoints')} />
                    </LabelRow>
                    <LabelRow label="Wat is het maximale laadvermogen van deze laadpunten?">
                        <NumberInput {...register('powerPerChargePointKw')} /> kW
                    </LabelRow>
                    <LabelRow label="Hoeveel rijden jullie auto's gemiddeld per jaar?">
                        <NumberInput {...register('annualTravelDistancePerCarKm')} /> km
                    </LabelRow>
                    <LabelRow label="Zijn jullie van plan de bedrijfsvloot (deels) te elektrificeren de komende jaren?
                        Wat is het geplande aantal auto's?">
                        <NumberInput {...register('numPlannedElectricCars')} />
                    </LabelRow>
                </>
            )}
        </>
    )
}