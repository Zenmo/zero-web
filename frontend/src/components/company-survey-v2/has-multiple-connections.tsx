import {Radio} from 'antd'
import {LabelRow} from './generic/label-row'

export const HasMultipleConnections = ({hasMultipleConnections, setMultipleConnections, businessParkname}: any) => {
    return (
        <>
            <LabelRow label={`Heeft uw bedrijf meerdere locaties of meerdere elektriciteitsaansluitingen op ${businessParkname}?`}>
                <Radio.Group onChange={e => setMultipleConnections(e.target.value)} value={hasMultipleConnections}>
                    <Radio value={true} css={{display: 'block'}}>Ja</Radio>
                    <Radio value={false} css={{display: 'block'}}>Nee</Radio>
                </Radio.Group>
            </LabelRow>
            {hasMultipleConnections &&
                <p>
                    Uw bedrijf heeft meerdere netaansluitingen,
                    we vragen daarom om de onderstaande vragenlijst per netaansluiting aan te leveren.
                    In sommige situaties is niet alles toe te kennen aan een specifieke netaansluiting,
                    dat kan het geval zijn bij bijv. mobiliteit of warmte.
                    Is dat het geval, dan kunt u zelf kiezen bij welke netaansluiting het aangeeft.
                    Heeft u meerdere netaansluiting op 1 adres,
                    behandel deze dan achteréénvolgend en geef aan dat het adres hetzelfde is als de voorgaande netaansluiting.
                </p>}
        </>
    )
}