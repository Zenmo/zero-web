package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.user.User
import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.orm.user.table.UserTable
import com.zenmo.zummon.companysurvey.Project
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

class ProjectRepository(
    val db: Database
) {
    fun getProjects(filter: Op<Boolean> = Op.TRUE): List<Project> {
        return transaction(db) {
            ProjectTable
                .selectAll()
                .where{
                    filter
                }.mapNotNull {
                    hydrateProject(it)
                }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun getProjectsByUserId(userId: UUID): List<Project> =
        transaction(db) {
             getProjects(
                ( ProjectTable .id eq anyFrom(
                    UserProjectTable.select(UserProjectTable.projectId)
                        .where { UserProjectTable.userId eq userId }
                ))
            )
        }

    fun saveNewProject(name: String): UUID =
        transaction(db) {
           ProjectTable.insertReturning(listOf(ProjectTable.id)) {
                it[ProjectTable.name] = name
            }.first()[ProjectTable.id]
        }

    @OptIn(ExperimentalUuidApi::class)
    fun getProjectByEnergiekeRegioId(energiekeRegioId: Int): Project =
        transaction(db) {
            ProjectTable.selectAll()
                .where {
                    ProjectTable.energiekeRegioId eq energiekeRegioId
                }
                .map {
                    Project(
                        it[ProjectTable.id].toKotlinUuid(),
                        it[ProjectTable.name],
                        it[ProjectTable.energiekeRegioId],
                        it[ProjectTable.buurtCodes],
                    )
                }
                .single()
        }

    protected fun hydrateProject(row: ResultRow): Project {
        return Project(
            id = row[ProjectTable.id],
            name = row[ProjectTable.name],
            energiekeRegioId = row[ProjectTable.energiekeRegioId],
            buurtCodes = row[ProjectTable.buurtCodes]
        )
    }
}
