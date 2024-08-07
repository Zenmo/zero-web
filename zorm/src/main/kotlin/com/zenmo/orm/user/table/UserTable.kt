package com.zenmo.orm.user.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.VarCharColumnType

object UserTable: Table("user") {
    /**
     * ID from Keycloak.
     * This can be a human user or a service account associated with OAuth client credentials.
     */
    val id = uuid("id")
    override val primaryKey = PrimaryKey(id)

    /**
     * Project the user has full access to.
     * A future extension would be to specify finegrained privileges like readonly.
     */
    val projects = array<String>("projects", VarCharColumnType(50))

    /**
     * Set the users name here for convenience.
     * This is mostly because there is no GUI yet and no logic to display the name from Keycloak.
     */
    val note = varchar("note", 255).default("")
}
