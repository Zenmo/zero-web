package com.zenmo.zummon

fun <E> MutableList<E>.addNotNull(value: E?) {
    if (value != null) {
        add(value)
    }
}
