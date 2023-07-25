import https from 'https'
import fs from 'fs'
import csv2json from 'csvjson-csv2json'
import lodash from 'lodash'

const {mapKeys, mapValues} = lodash

const name = 'stedin_kleinverbruikgegevens_2023-01-01'
const csvName = `${name}.csv`
const jsonName = `${name}.json`

export async function initializeDataSet() {
    if (fs.existsSync(jsonName)) {
        return JSON.parse(
            fs.readFileSync(jsonName).toString(),
        )
    }

    const csv = await downloadCsv(csvName)
    const rawRecords = csv2json(csv)
    const records = rawRecords.map(record => {
        record = mapKeys(record, (value, key) => upperSnakeCaseToDromedaryCase(key))

        record = mapValues(record, tryConvertToNumber)

        return record
    })

    fs.writeFileSync(jsonName, JSON.stringify(records))

    return Promise.resolve(records)
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

async function downloadCsv(csvName) {
    let csv = ''

    return await new Promise((resolve, reject) => {
        const request = https.get(
            `https://www.stedin.net/-/media/project/online/files/zakelijk/open-data/${csvName}`,
            (response) => {
                if (response.statusCode !== 200) {
                    reject(new Error(`Stedin.net http status code: ${response.statusCode}`))
                }

                response.on('error', reject)

                response.on('data', chunk => {
                    csv += chunk
                })

                response.on('end', () => {
                    resolve(csv)
                })
            },
        )
        request.on('error', reject)
    })
}
