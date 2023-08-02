import http from 'http'
import https from 'https'
import fs from 'fs'
import csv2json from 'csvjson-csv2json'
import lodash from 'lodash'

const {mapKeys, mapValues} = lodash

const csvName = 'stedin_kleinverbruikgegevens_2023-01-01.csv'

async function initializeDataSet() {
    if (!fs.existsSync(csvName)) {
        await downloadCsv(csvName)
    }

    const csv = fs.readFileSync(csvName).toString()
    const rawRecords = csv2json(csv)
    const r = new RegExp(/_\w/g)
    const records = rawRecords.map(record => {
        record = mapKeys(record, (value, key) => upperSnakeCaseToDromedaryCase(key))

        record = mapValues(record, tryConvertToNumber)

        return record
    })

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
    const csvFile = fs.openSync(csvName, 'w')
    try {
        return await new Promise((resolve, reject) => {
            const request = https.get(
                `https://www.stedin.net/-/media/project/online/files/zakelijk/open-data/${csvName}`,
                (response) => {
                    if (response.statusCode !== 200) {
                        reject(new Error(`Stedin.net http status code: ${response.statusCode}`))
                    }

                    response.on('error', reject)

                    response.on('data', chunk => {
                        fs.writeSync(csvFile, chunk)
                    })

                    response.on('end', () => {
                        fs.close(csvFile)
                        resolve()
                    })
                },
            )
            request.on('error', reject)
        })
    } finally {
        fs.closeSync(csvFile)
    }
}

try {
    const records = await initializeDataSet()

    const server = http.createServer((req, res) => {
        res.setHeader('Content-Type', 'application/json')

        const url = new URL(req.url, 'https://example.com')
        const postalCodes = url.searchParams.get('postalcodes')
        if (postalCodes === null) {
            res.statusCode = 400
            res.end(JSON.stringify({
                'error': 'parameter postalcode is missing or empty',
            }))
            return
        }

        const postalCodeList = postalCodes.split(',')
        for (const postalCode of postalCodeList) {
            if (!postalCode.match(/^\d{4}[A-Z]{2}$/)) {
                res.statusCode = 400
                res.end(JSON.stringify({
                    'error': 'invalid postalcode, expected format 1111AA',
                }))
                return
            }
        }

        const matches = records.filter(record => {
            for (const postalCode of postalCodeList) {
                if (postalCode >= record.postcodeVan && postalCode <= record.postcodeTot) {
                    return true
                }
            }

            return false
        })

        res.statusCode = 200
        res.end(JSON.stringify(matches))
    })

    const hostname = '127.0.0.1'
    const port = 3000

    server.listen(port, hostname, () => {
        console.log(`Server running at http://${hostname}:${port}/`)
    })
} catch (e) {
    console.log('Error intializing: ' + e)
    process.exit(1)
}
