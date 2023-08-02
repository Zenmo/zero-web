import {initializeDataSet} from './dataset.mjs'

// Put JSON on disk so it can be put inside the container.
// This speeds up the start-up time of the service.
await initializeDataSet()
