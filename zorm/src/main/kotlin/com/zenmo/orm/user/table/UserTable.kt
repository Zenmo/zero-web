package com.zenmo.orm.user.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType

object UserTable: Table("user") {
    /**
     * ID from Keycloack
     */
    val id = uuid("id")

    /**
     * Project the user has full access to.
     * A future extension would be to specify finegrained privileges like readonly.
     */
    val projects = array<String>("projects", VarCharColumnType(50))
}
