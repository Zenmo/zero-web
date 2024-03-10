import {UseFormReturn} from 'react-hook-form'
import {FormRow} from './generic/form-row'
import {TextAreaRow} from './generic/text-area-row'
import {TextInput} from './generic/text-input'

export const OpenQuestions = ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    return (
        <div>
            <h2>7. Open vragen</h2>
            <TextAreaRow
                label="Welk process of welke activiteit bepaalt de primaire energiebehoefte in uw bedrijf?"
                form={form}
                name={`${prefix}.mainConsumptionProcess`} />
            <TextAreaRow
                label="Bent u van plan de komende jaren uw energiebehoefte uit te breiden? Zo ja, hoe/in welke mate ongeveer?"
                form={form}
                name={`${prefix}.expansionPlans`} />
            <TextAreaRow
                label="Kunt u kort iets vertellen of u energieprocessen heeft die potientieel flexibiliteit kunnen bieden door bijv. uitstelling of slimme inzet?"
                form={form}
                name={`${prefix}.consumptionFlexibility`} />
            <TextAreaRow
                label="Bent u van plan bepaalde bedrijfsprocessen te elektrificeren de komende jaren?"
                form={form}
                name={`${prefix}.electrificationPlans`} />
             <FormRow
                label="Indien van toepassing, wat is de leverancier van uw energie- of gebouwmanagementsysteem (EMS of BMS)?"
                name={`${prefix}.energyOrBuildingManagementSystemSupplier`}
                form={form}
                InputComponent={TextInput} />
            <TextAreaRow
                label="Zijn er relevante energiedynamieken niet aan bod gekomen in dit formulier, zo ja, welke?"
                form={form}
                name={`${prefix}.surveyFeedback`} />
        </div>
    )
}
