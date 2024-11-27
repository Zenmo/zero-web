package com.zenmo.orm.user

import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.orm.user.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserRepository(
    private val db: Database,
) {
    fun getUsers(filter: Op<Boolean> = Op.TRUE): List<User> {
        return transaction(db) {
            UserTable
                .selectAll()
                .where{
                    filter
                }.mapNotNull {
                    hydrateUser(it)
                }
        }
    }

    fun getUserById(id: UUID): User? {
        return getUsers(
            (UserTable.id eq id)
        ).firstOrNull()
    }

    fun saveUser(
        userId: UUID,
        projectIds: List<UUID> = emptyList(),
        note: String = "",
    ) {
        transaction(db) {
            UserTable.upsert {
                it[id] = userId
                it[UserTable.note] = note
            }

            UserProjectTable.batchInsert(projectIds) {
                this[UserProjectTable.projectId] = it
                this[UserProjectTable.userId] = userId
            }
        }
    }

    fun deleteUserById(userId: UUID) {
        transaction(db) {
            UserProjectTable.deleteWhere {
                UserProjectTable.userId eq userId
            }
            UserTable.deleteWhere {
                UserTable.id eq userId
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
