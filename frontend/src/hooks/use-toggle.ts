import {useState} from "react"

export const useToggle = (initialValue: boolean = false): [boolean, () => void] => {
    const [value, setValue] = useState(initialValue)
    return [value, () => setValue(!value)]
}