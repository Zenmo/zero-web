
export const assertDefined = <T>(value: T | undefined | null): T => {
    if (value === undefined || value === null) {
        throw new Error('value is undefined')
    }
    return value
}

export const mapOrElse = <T,R>(
    array: Array<T> | ReadonlyArray<T>,
    f: (value: T, index: number, array: Array<T> | ReadonlyArray<T>) => R,
    g: () => R,
): R[] => {
    if (array.length === 0) {
        return [g()]
    }
    return array.map(f)
}

