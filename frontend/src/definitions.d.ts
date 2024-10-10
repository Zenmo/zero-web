// silences typescript complaining about importing images
declare module '*.png'

declare var process: {
    env: {
        [key: string]: string | undefined
    }
}

declare module 'memoize-immutable' {
    export default function memoize<Fn extends Function>(f: Fn): Fn
}
