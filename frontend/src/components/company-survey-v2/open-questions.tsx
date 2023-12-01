import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './generic/label-row'
import {TextAreaRow} from "./generic/text-area-row";

export const OpenQuestions = ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    return (
        <>
        <h2>7. Open vragen</h2>
            <TextAreaRow
                label="Welk process of welke activiteit omslaat de primaire energiebehoefte in uw bedrijf?"
                form={form}
                name={`${prefix}.mainConsumptionProcess`} />
            <TextAreaRow
                label="Wat voor flexibiliteit is er in het verschuiven in de tijd van jullie primaire energiebehoefte? M.a.w. kan het snachts, kan het altijd een uur later of eerder, etc."
                form={form}
                name={`${prefix}.consumptionFlexibility`} />
            <TextAreaRow
                label="Bent u van plan bepaalde bedrijfsprocessen te elektrificeren in de komende jaren?"
                form={form}
                name={`${prefix}.electrificationPlans`} />
            <TextAreaRow
                label="Zijn er relevante energiedynamieken niet aan bod gekomen in dit formulier, zo ja, welke?"
                form={form}
                name={`${prefix}.surveyFeedback`} />
        </>
    )
}
