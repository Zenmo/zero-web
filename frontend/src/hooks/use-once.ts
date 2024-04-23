import {useState} from 'react'

export const useOnce = <T>(f: () => T) => {
    useState(f)
}