import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './generic/label-row'

export const OpenQuestions = ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    return (
        <>
        <h2>7. Open vragen</h2>
            <LabelRow label="Welk process of welke activiteit omslaat de primaire energiebehoefte in uw bedrijf?">
                <input type="text" {...form.register(`${prefix}.mainConsumptionProcess`)} />
            </LabelRow>
            <LabelRow label="Wat voor flexibiliteit is er in het verschuiven in de tijd van jullie primaire energiebehoefte? M.a.w. kan het snachts, kan het altijd een uur later of eerder, etc.">
                <input type="text" {...form.register(`${prefix}.consumptionFlexibility`)} />
            </LabelRow>
            <LabelRow label="Bent u van plan bepaalde bedrijfsprocessen te elektrificeren in de komende jaren?">
                <input type="text" {...form.register(`${prefix}.electrificationPlans`)} />
            </LabelRow>
            <LabelRow label="Zijn er relevante energiedynamieken niet aan bod gekomen in dit formulier, zo ja, welke?">
                <input type="text" {...form.register(`${prefix}.electrificationPlans`)} />
            </LabelRow>
        </>
    )
}
