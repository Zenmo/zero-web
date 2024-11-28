package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.companysurvey.table.ProjectTable.select
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.UserRepository
import com.zenmo.orm.user.table.UserProjectTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import kotlin.test.*
import org.junit.Test
import java.util.UUID
import kotlin.uuid.toKotlinUuid

class ProjectRepositoryTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun setupClass() {
            val db = connectToPostgres()
            val schema = Schema(db.connector().schema)
            transaction(db) {
                SchemaUtils.dropSchema(schema, cascade = true)
                SchemaUtils.createSchema(schema)
            }
            createSchema(db)
        }
    }

    @Test
    fun testSaveNewProject() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val projectName = "New Test Project"

        transaction(db) {
            repo.saveNewProject(projectName)
        }

        transaction(db) {
            val result = ProjectTable.selectAll().where{ ProjectTable.name eq projectName }.singleOrNull()
            assertNotNull(result)
            assertEquals(projectName, result[ProjectTable.name])
        }
    }

//    @Test
//    fun testGetProjectByEnergiekeRegioId() {
//        val db = connectToPostgres()
//        val repo = ProjectRepository(db)
//        val projectName = "Test Project"
//        val energiekeRegioId = 123
//
//        transaction(db) {
//            repo.saveNewProject(projectName)
//            val project = repo.getProjectByEnergiekeRegioId(energiekeRegioId)
//            assertNotNull(project)
//            assertEquals(projectName, project.name)
//            assertEquals(energiekeRegioId, project.energiekeRegioId)
//        }
//    }

    @Test
    fun testGetProjects() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val userId = UUID.randomUUID()
        val userRepo = UserRepository(db)
        val projectName = "Test Project"
        //val energiekeRegioId = 123

        transaction(db) {
            val projectId = repo.saveNewProject(projectName)
            // just to follow the current logic
            userRepo.saveUser(userId, listOf(projectId))

            val projects = repo.getProjects(userId)
            assertEquals(1, projects.size)
            assertEquals(projectId, projects[0].id)
            assertEquals(projectName, projects[0].name)
//          assertEquals(energiekeRegioId, projects[0].energiekeRegioId)
        }
    }
}
