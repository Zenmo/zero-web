package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.*
import java.util.UUID

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
    fun testGetAllProjects() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val projectName = "Test Project"
        //val energiekeRegioId = 123

        transaction(db) {
            val projectId = repo.saveNewProject(projectName)
            val projects = repo.getProjects()
            assertEquals(4, projects.size)
            assertEquals(projectId, projects.last().id)
            assertEquals(projectName, projects.last().name)
//          assertEquals(energiekeRegioId, projects[0].energiekeRegioId)
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
    fun testGetProjectsByUserId() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val userRepo = UserRepository(db)

        val userId = UUID.randomUUID()
        val projectName1 = "User's Project 1"
        val projectName2 = "User's Project 2"

        transaction(db) {
            val projectId1 = repo.saveNewProject(projectName1)
            val projectId2 = repo.saveNewProject(projectName2)
            userRepo.saveUser(userId, listOf(projectId1, projectId2))

            val projects = repo.getProjectsByUserId(userId)
            assertEquals(2, projects.size)
            assertTrue(projects.any { it.name == projectName1 })
            assertTrue(projects.any { it.name == projectName2 })
        }
    }

    @Test
    fun testUserCantAccessOtherProjects() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val ownProjectName = "Middelkaap"
        val otherProjectName = "Bovenkaap"
        val projectId = repo.saveNewProject(ownProjectName)
        val projectId2 = repo.saveNewProject(otherProjectName)

        val userRepo = UserRepository(db)
        val userId = UUID.randomUUID()
        userRepo.saveUser(userId, listOf(projectId))
        val userId2 = UUID.randomUUID()
        userRepo.saveUser(userId2, listOf(projectId2))

        val projects = repo.getProjectsByUserId(userId)
        assertEquals(1, projects.size)
        assertEquals("Middelkaap", projects[0].name)
    }
}
