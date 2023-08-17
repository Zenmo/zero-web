export const mergeMaps = <K, V>(...maps: Map<K, V>[]): Map<K, V> => {
    return maps.reduce((acc, map) => {
        for (const [key, value] of map) {
            acc.set(key, value)
        }
        return acc
    }, new Map<K, V>())
}

// we want to be able to rewind
export function toIterable<T>(generator: () => Generator<T>): Iterable<T> {
    return {
        [Symbol.iterator]: generator,
    }
}

export function* filter<T>(
    iterable: Iterable<T>,
    predicate: (value: T) => boolean,
): Generator<T> {
    for (const value of iterable) {
        if (predicate(value)) {
            yield value
        }
    }
}

export function* map<T, U>(
    iterable: Iterable<T>,
    mapper: (value: T) => U,
): Generator<U> {
    for (const value of iterable) {
        yield mapper(value)
    }
}

export function* flatMap<T, U>(
    iterable: Iterable<T>,
    mapper: (value: T) => Iterable<U>,
): Generator<U, void, undefined> {
    for (const value of iterable) {
        yield* mapper(value)
    }
}

export function reduce<T, U>(
    iterable: Iterable<T>,
    reducer: (acc: U, value: T) => U,
    initialValue: U,
): U {
    let acc = initialValue
    for (const value of iterable) {
        acc = reducer(acc, value)
    }
    return acc
}

export const count = (iterable: Iterable<any>): number => {
    if (Array.isArray(iterable)) {
        return iterable.length
    }

    let count = 0
    for (const _ of iterable) {
        count++
    }
    return count
}
export const empty = (iterable: Iterable<any>): boolean =>
    iterable[Symbol.iterator]().next().done === true