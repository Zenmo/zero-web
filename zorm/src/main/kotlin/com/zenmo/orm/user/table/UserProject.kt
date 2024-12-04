package com.zenmo.orm.user.table

import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.dbutil.enumArray
import com.zenmo.orm.user.ProjectScope
import org.jetbrains.exposed.sql.Table

/**
 * Links a User to the Projects they have access to.
 */
object UserProjectTable: Table("user_project") {
    val userId = reference("user_id", UserTable.id)
    val projectId = reference("project_id", ProjectTable.id)
    override val primaryKey = PrimaryKey(userId, projectId)

    val scopes = enumArray("scopes", ProjectScope::class).default(emptyList())
}
