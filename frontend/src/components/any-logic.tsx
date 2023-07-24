
import {useRef, useState} from "react";

// this is imported old-school via <script> tag
declare var CloudClient: any

export const AnyLogic = () => {

    const [visible, setVisible] = useState(false)

    const onClick = async () => {
        // API key is of user "public@zenmo.com"
        const cloudClient = CloudClient.create('17e0722f-25c4-4549-85c3-d36509f5c710', 'https://engine.holontool.nl')
        const model = await cloudClient.getModelById('d5048a0b-55df-4ee4-9938-b3cf9046b43e')
        const latestVersion = await cloudClient.getModelVersionByNumber(model, model.modelVersions.length)
        const inputs = cloudClient.createDefaultInputs(latestVersion);
        // inputs.setInput( "Contact Rate", 30 );
        const animation = await cloudClient.startAnimation( inputs, "any-logic" );
        setVisible(true)
    }
    const ref = useRef(null)

    return (
        <>
            <div>
                <button onClick={onClick}>Start simulatie</button>
            </div>
            <div id="any-logic" ref={ref} style={{flexGrow: 1, display: visible ? 'block': 'none'}}></div>
        </>
    )
}