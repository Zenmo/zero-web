package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.*
import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.zummon.companysurvey.Project
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

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

    fun getProjectById(id: UUID): Project? {
        return getProjects(
            (ProjectTable.id eq id)
        ).firstOrNull()
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

    fun deleteProject(projectId: UUID): Boolean {
        return transaction(db) {
            ProjectTable.deleteWhere { ProjectTable.id eq projectId } > 0
        }
    }

    fun save(project: Project): Project {
        return transaction(db) {
           ProjectTable.upsertReturning() {
                it[id] = project.id
                it[name] = project.name
                it[energiekeRegioId] = project.energiekeRegioId
                it[buurtCodes] = project.buurtCodes
            }.map {
               hydrateProject(it)
            }.first()
        }
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
            getProjects(
                ProjectTable.energiekeRegioId eq energiekeRegioId
            )
        }.first()

    protected fun hydrateProject(row: ResultRow): Project {
        return Project(
            id = row[ProjectTable.id],
            name = row[ProjectTable.name],
            energiekeRegioId = row[ProjectTable.energiekeRegioId],
            buurtCodes = row[ProjectTable.buurtCodes]
        )
    }
}
