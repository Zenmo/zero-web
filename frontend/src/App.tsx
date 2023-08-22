import 'leaflet/dist/leaflet.css'
import React, {createElement as h, useState} from 'react'
import './App.css'
import {AggregatedAreaData} from './components/aggregated-area-data'
import {AnyLogic} from './components/any-logic'
import {BuurtPicker} from './components/buurt-picker'
import {MainMap} from './components/main-map'
import {PandDataDisplay} from './components/pand-display'
import {useApp} from './services/appState'
import {assertDefined} from './services/util'
import {Buurt} from './services/wijken-buurten'

function App() {
    const appHook = useApp()
    const {setGeometry, getPandData, bag2dPanden} = appHook

    const [currentPandId, setCurrentPandId] = useState('')

    const [buurt, setBuurt] = useState<Buurt | undefined>()

    return (
        <>
            <h1 style={{
                position: 'absolute',
                left: 0,
                padding: '.5em 1em',
                margin: 0,
            }}>
                <img
                    src="https://zenmo.com/wp-content/uploads/elementor/thumbs/zenmo-logo-website-light-grey-square-o1piz2j6llwl7n0xd84ywkivuyf22xei68ewzwrvmc.png"
                    style={{height: '1em', verticalAlign: 'sub'}}/>
                &nbsp;
                Zenmo Zero
            </h1>
            {/* Three-column layout*/}
            <div style={{width: '20rem', padding: '1rem', paddingTop: '5rem'}}>
                {h(AggregatedAreaData, {appHook: appHook})}
                <BuurtPicker onSelectBuurt={buurt => {
                    setBuurt(buurt)
                    setGeometry(buurt.geometry)
                }}/>
            </div>
            <div style={{
                height: '100vh',
                flexGrow: 1,
                display: 'flex',
                flexDirection: 'column',
            }}>
                <MainMap bag2dPanden={bag2dPanden}
                         setGeometry={setGeometry}
                         setCurrentPandId={setCurrentPandId}
                         buurt={buurt} />
                <AnyLogic appHook={appHook}/>
            </div>
            <div style={{width: '20rem', padding: '1rem'}}>
                {currentPandId && getPandData(currentPandId) &&
                    <PandDataDisplay pandData={assertDefined(getPandData(currentPandId))}/>}
            </div>
        </>
    )
}

export default App
