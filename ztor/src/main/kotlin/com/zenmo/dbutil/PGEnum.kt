package com.zenmo.dbutil

import org.postgresql.util.PGobject

/**
 * Boilerplate of https://github.com/JetBrains/Exposed/wiki/DataTypes#how-to-use-database-enum-types
 */
class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

fun createEnumTypeSql(enumClass: Class<*>): String {
    return "CREATE TYPE ${enumClass.simpleName} AS ENUM (${enumClass.enumConstants.joinToString { "'${it}'" }});"
}