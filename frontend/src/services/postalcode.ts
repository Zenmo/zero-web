
const regex = /^(?<digits>\d{4}) ?(?<letters>[A-Z]{2})$/

// Convert a postal code to a number so it can be used in a comparison
// to find the closes postal code.
// This is obviously an approximation because many postal codes further in the alphabet
// like ZZ are not in use.
export const postalCodeToNumber = (postalcode: string): number => {
    const match = postalcode.match(regex)
    if (!match) {
        throw Error(`Invalid postal code: ${postalcode}`)
    }

    // @ts-ignore
    const {digits, letters} = match.groups

    return Number(digits) * 26 * 26
        + (letters.charCodeAt(0) - 65) * 26
        + letters.charCodeAt(1) - 65
}

// Calculate the distance between two postal codes.
export const postalCodeDistance = (low: string, high: string): number => {
    const distance = postalCodeToNumber(low) - postalCodeToNumber(high)

    if (distance < 0) {
        throw Error(`Low postal code ${low} is higher than high postal code ${high}`)
    }

    return distance
}

export const postalCodeRangeDistance = (value: string, low: string, high: string): number =>
    Math.min(postalCodeDistance(low, value), postalCodeDistance(value, high))

export const printPostalCodeRange = (low: string, high: string): string => {
    if (low === high) {
        return insertSpace(low)
    }

    return `${insertSpace(low)} - ${insertSpace(high)}`
}

// Convert 1111AA to 1111 AA
export const insertSpace = (postalcode: string): string => {
    const match = postalcode.match(regex)
    if (!match) {
        throw Error(`Invalid postal code: ${postalcode}`)
    }

    // @ts-ignore
    const {digits, letters} = match.groups

    return `${digits} ${letters}`
}
