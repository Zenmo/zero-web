import {useEffect, useState} from 'react'
import {useOnce} from '../services/use-once'
import {fetchGemeenteList, fetchBuurtList, fetchBuurt, Buurt} from '../services/wijken-buurten'
import {Select} from 'antd'

// First pick from a list of gemeenten,
// then pick from a list of buurten in that gemeente.
export const BuurtPicker = ({onSelectBuurt}: {
    onSelectBuurt: (buurt: Buurt) => void
}) => {
    const [selectedGemeente, setSelectedGemeente] = useState<string | undefined>()

    return (
        <div>
            <div>
                <GemeenteSelect onSelectGemeente={setSelectedGemeente} gemeente={selectedGemeente}/>
            </div>
            <div>
                <BuurtSelect onSelectBuurt={onSelectBuurt} gemeente={selectedGemeente}/>
            </div>
        </div>
    )
}

const GemeenteSelect = ({gemeente, onSelectGemeente}: {
    gemeente?: string,
    onSelectGemeente: (gemeente?: string) => void
}) => {
    const [gemeenteList, setGemeenteList] = useState([] as string[])
    useOnce(() => {
        fetchGemeenteList().then(setGemeenteList)
    })

    return (
        <Select
            style={{width: '100%'}}
            showSearch
            placeholder="Selecteer gemeente"
            onChange={(gemeente) => {
                onSelectGemeente(gemeente)
            }}
            value={gemeente}
            options={gemeenteList.map(g => ({value: g, label: g}))}
        />
    )
}

const BuurtSelect = ({gemeente, onSelectBuurt}: {
    gemeente?: string,
    onSelectBuurt: (buurt: Buurt) => void
}) => {
    const [buurten, setBuurten] = useState([] as string[])

    useEffect(() => {
        setBuurten([])
        if (gemeente) {
            fetchBuurtList(gemeente).then(setBuurten)
        }
    }, [gemeente])

    return (
        <Select
            style={{width: '100%'}}
            showSearch
            placeholder="Selecteer buurt"
            options={buurten.map(g => ({value: g, label: g}))}
            onChange={(buurtName) => {
                if (buurtName) {
                    // @ts-ignore
                    fetchBuurt(gemeente, buurtName).then(onSelectBuurt)
                }
            }}
        />
    )
}
