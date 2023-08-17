import {filter, toIterable} from './iterable'

describe(toIterable.name, () => {
    const g = () => filter([1, 2, 3], v => true)
    const it = toIterable(g)

    test('reuse', () => {
        expect([...it].length).toBe(3)
        expect([...it].length).toBe(3)
    })
})
