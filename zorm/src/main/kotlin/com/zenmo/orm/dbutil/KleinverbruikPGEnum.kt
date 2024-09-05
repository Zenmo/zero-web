package com.zenmo.orm.dbutil

import com.zenmo.zummon.companysurvey.KleinverbruikElectricityConnectionCapacity
import org.postgresql.util.PGobject

/**
 * Postgres can handle string enum values which start with a number but many other languages can't.
 */
class KleinverbruikPGEnum(enumValue: KleinverbruikElectricityConnectionCapacity?) : PGobject() {
    init {
        value = enumValue?.toDisplayName()
        type = KleinverbruikElectricityConnectionCapacity::class.simpleName
    }
}

fun createKleinverbruikEnumTypeSql(): String {
    return """
        CREATE TYPE ${KleinverbruikElectricityConnectionCapacity::class.simpleName} 
        AS ENUM (${KleinverbruikElectricityConnectionCapacity.entries.joinToString { "'${it.toDisplayName()}'" }})
        """.trimIndent()
}
