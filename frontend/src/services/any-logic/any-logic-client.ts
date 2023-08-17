import {AppHook} from '../appState'
import {appStateToScenarioInput} from './scenario-input'

// this is imported old-school via <script> tag
// see https://anylogic.help/cloud/api/js.html
declare var CloudClient: any

export const startSimulation = async (divId: string, appHook: AppHook): Promise<Animation> => {
    // API key is of user "publiek@zenmo.com"
    const apiKey = '17e0722f-25c4-4549-85c3-d36509f5c710'
    const modelId = 'f5586290-2a06-4a7c-8c59-bd77840932c3'

    const cloudClient = CloudClient.create(apiKey, 'https://engine.holontool.nl')
    const model = await cloudClient.getModelById(modelId)
    const latestVersion = await cloudClient.getModelVersionByNumber(model, model.modelVersions.length)
    const inputs = cloudClient.createDefaultInputs(latestVersion)
    inputs.setInput('P scenario JSON', appStateToScenarioInput(appHook))
    inputs.setInput('P import local config jsons', false)
    const animation = await cloudClient.startAnimation(inputs, divId)
    animation.setSpeed(1)

    // Example how to call into AnyLogic
    // see https://anylogic.help/cloud/api/js.html#animation-class
    // animation.callFunction('experiment.root.API_function_setPVslider', [6])

    return animation
}
