import http from 'http'
import {initializeDataSet} from './dataset.mjs'

try {
    const records = await initializeDataSet()

    const server = http.createServer((req, res) => {
        res.setHeader('Content-Type', 'application/json')

        const url = new URL(req.url, 'https://example.com')
        const postalCodes = url.searchParams.get('postalcodes')
        if (postalCodes === null) {
            res.statusCode = 400
            res.end(JSON.stringify({
                'error': 'parameter postalcodes is missing or empty',
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

    const hostname = '0.0.0.0'
    const port = process.env.PORT ?? 3000

    server.listen(port, hostname, () => {
        console.log(`Server running at http://${hostname}:${port}/`)
    })
} catch (e) {
    console.log('Error intializing: ' + e)
    process.exit(1)
}
