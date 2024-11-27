package com.zenmo.orm.user

import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.orm.user.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository(
    private val db: Database,
) {
    fun getUserById(db: Database, id: UUID): User? {
        return transaction(db) {
            UserTable
                .selectAll()
                .where{
                    UserTable.id eq id
                }.map {
                    hydrateUser(it)
                }.firstOrNull()
        }
    }

    fun saveUser(
        db: Database,
        userId: UUID,
        projectIds: List<UUID> = emptyList(),
        note: String = "",
    ) {
        transaction(db) {
            UserTable.insert {
                it[id] = userId
                it[UserTable.note] = note
            }

            UserProjectTable.batchInsert(projectIds) {
                this[UserProjectTable.projectId] = it
                this[UserProjectTable.userId] = userId
            }
        }
    }

    protected fun hydrateUser(row: ResultRow): User {
        return User(
            id = row[UserTable.id],
            note = row[UserTable.note],
            projects = emptyList(), // data from different table
        )
    }

}
