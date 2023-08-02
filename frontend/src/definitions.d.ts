// silences typescript complaining about importing images
declare module '*.png'

// https://parceljs.org/features/node-emulation/#environment-variables
declare var process: {
    env: {
        [key: string]: string | undefined
    }
}
