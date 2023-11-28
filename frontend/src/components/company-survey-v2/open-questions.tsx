import {UseFormReturn} from 'react-hook-form'
import {LabelRow} from './label-row'

export const OpenQuestions =  ({form, prefix}: { form: UseFormReturn , prefix: string }) => {
    return (
        <>
        <h2>Open vragen</h2>
            <LabelRow label="Welk process verbruikt het meeste energie in het bedrijf? (bijvoorbeeld kantoor, gekoelde opslag, metaalbewerking)">
                <input type="text" {...form.register(`${prefix}.mainConsumptionProcess`)} />
            </LabelRow>
            <LabelRow label="Wat voor flexibiliteit is er in het verleggen in de tijd van de energie consumptie van het process?">
                <input type="text" {...form.register(`${prefix}.consumptionFlexibility`)} />
            </LabelRow>
            <LabelRow label="Bent u van plan bedrijfsprocessen te elektrificeren in de komende jaren?">
                <input type="text" {...form.register(`${prefix}.electrificationPlans`)} />
            </LabelRow>
        </>
    )
}
