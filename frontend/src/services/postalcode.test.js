
import {postalCodeToNumber} from './postalcode';

describe(postalCodeToNumber.name, () => {
    test.each([
        ['0000AA', 0],
        ['0000AB', 1],
        ['0000BA', 26],
        ['0000BB', 27],
        ['0000ZZ', 26 * 26 - 1],
        ['0001AA', 26 * 26],
        ['9999ZZ', 10_000 * 26 * 26 -1],
    ])('%s => %i', (postalCode, value) => {
        expect(postalCodeToNumber(postalCode)).toBe(value);
    })
})
