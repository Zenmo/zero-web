import https from 'https'
import fs from 'fs'
import csv2json from 'csvjson-csv2json'
import lodash from 'lodash'
import {PassThrough} from 'stream'
import ReadableStream = NodeJS.ReadableStream
import unzipper, {Entry} from 'unzipper'
import {text} from 'stream/consumers'

const {mapKeys, mapValues} = lodash

const jsonName = 'dataset.json'

export async function initializeDataSet(includeEnexis = false) {
    if (fs.existsSync(jsonName)) {
        return JSON.parse(
            fs.readFileSync(jsonName).toString(),
        )
    }

    const stedin = fetchStedinRecords()
    const liander = fetchLianderRecords()
    let enexis = Promise.resolve([])
    if (includeEnexis) {
        enexis = fetchEnexisRecords()
    }

    const [stedinRecords, lianderRecords, enexisRecords] = await Promise.all([stedin, liander, enexis])
    console.log(`Fetched ${stedinRecords.length} records from Stedin`)
    console.log(`Fetched ${lianderRecords.length} records from Liander`)
    console.log(`Fetched ${enexisRecords.length} records from Enexis`)
    const allRecords = [...stedinRecords, ...lianderRecords, ...enexisRecords]

    fs.writeFileSync(jsonName, JSON.stringify(allRecords))

    return Promise.resolve(allRecords)
}

async function fetchEnexisRecords() {
    const csvStream = download("https://s3-eu-west-1.amazonaws.com/enxp433-oda01/kv/Enexis_kleinverbruiksgegevens_01012023.csv")
    const csvString = await text(csvStream)

    return recordsFromCsvString(csvString)
}

async function fetchStedinRecords(): Promise<any[]> {
    const csvStream = download('https://www.stedin.net/-/media/project/online/files/zakelijk/open-data/stedin_kleinverbruikgegevens_2023-01-01.csv')
    const csvString = await text(csvStream)

    return recordsFromCsvString(csvString)
}

async function fetchLianderRecords(): Promise<any[]> {
    const zip = download('https://www.liander.nl/sites/default/files/opendata_2023_v3.zip')
    const iterator: AsyncIterator<Entry> = zip.pipe(unzipper.Parse({forceStream: true}))[Symbol.asyncIterator]()
    const result1 = await iterator.next()
    if (result1.done) {
        throw new Error('Unexpected end of zip file')
    }

    const zipEntry: Entry = result1.value
    if (!zipEntry.path.endsWith('.csv')) {
        throw new Error(`Unexpected zip entry: ${zipEntry.path}`)
    }

    const string = await text(zipEntry)

    return recordsFromCsvString(string)
}

function recordsFromCsvString(csv: string) {
    const rawRecords = csv2json(csv)
    const records = rawRecords.map(record => {
        record = mapKeys(record, (value, key) => upperSnakeCaseToDromedaryCase(key))

        record = mapValues(record, tryConvertToNumber)

        return record
    })

    return records
}

// convert SOORT_AANSLUITING_PERC to soortAansluitingPerc
function upperSnakeCaseToDromedaryCase(str) {
    return str
        .toLowerCase()
        .replaceAll(/_\w/g, t => t[1].toUpperCase())
}

function tryConvertToNumber(str) {
    if (/^\d+,?\d+?$/.test(str)) {
        return Number(str.replace(',', '.'))
    } else {
        return str
    }
}

function download(url): ReadableStream {
    let stream = new PassThrough()

    const request = https.get(
        url,
        (response) => {
            if (response.statusCode !== 200) {
                stream.destroy(new Error(`GET ${url} status code: ${response.statusCode}`))
            }

            response.pipe(stream)
        },
    )
    request.on('error', e => stream.destroy(e))

    return stream
}
