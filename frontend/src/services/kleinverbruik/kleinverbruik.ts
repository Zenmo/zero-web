import {KleinVerbruikPerPostcode} from '../appState'
import {postalCodeRangeDistance} from '../postalcode'
import {
    PostcodeElektriciteitKleinverbruik,
    PostcodeGasKleinverbruik,
    PostcodeKleinverbruik,
    Productsoort,
} from './types'

export const getElectricityUsage = (
    postcode: string,
    records: PostcodeKleinverbruik[],
): (undefined | PostcodeElektriciteitKleinverbruik) =>
    getUsage(postcode, records, 'ELK') as PostcodeElektriciteitKleinverbruik

export const getGasUsage = (
    postcode: string,
    records: PostcodeKleinverbruik[],
): (undefined | PostcodeGasKleinverbruik) =>
    getUsage(postcode, records, 'GAS') as PostcodeGasKleinverbruik

const getUsage = (
    postcode: string,
    records: PostcodeKleinverbruik[],
    productSoort: Productsoort,
): (undefined | PostcodeKleinverbruik) => {
    return records.filter(kleinverbruik =>
        kleinverbruik.productsoort === productSoort
        &&
        kleinverbruik.postcodeVan >= postcode
        &&
        kleinverbruik.postcodeTot <= postcode
        &&
        // remove inaccurate records
        postalCodeRangeDistance(postcode, kleinverbruik.postcodeVan, kleinverbruik.postcodeTot)
        < 3,
    )
        .sort((a, b) => {
            const distanceToA = postalCodeRangeDistance(postcode, a.postcodeVan, a.postcodeTot)
            const distanceToB = postalCodeRangeDistance(postcode, b.postcodeVan, b.postcodeTot)
            return distanceToA - distanceToB
        })[0]
}