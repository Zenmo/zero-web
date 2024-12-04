package com.zenmo.orm.companysurvey

import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.UserRepository
import com.zenmo.zummon.companysurvey.Project
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
    fun testSaveProject() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)

        val project = Project(
            name = "Updated Project",
            energiekeRegioId = 456,
            buurtCodes = listOf("B001", "B002")
        )
        val savedProject = repo.save(project)

        transaction(db) {
            val result = ProjectTable.selectAll()
                .where { ProjectTable.id eq savedProject.id }
                .singleOrNull()

            assertNotNull(result)
            assertEquals(savedProject.name, result[ProjectTable.name])
            assertEquals(savedProject.energiekeRegioId, result[ProjectTable.energiekeRegioId])
            assertEquals(savedProject.buurtCodes, result[ProjectTable.buurtCodes])
        }
    }

    @Test
    fun testGetAllProjects() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val projectName = "Testing Project"
        val energiekeRegioId = 459

        val project = Project(
            name = projectName,
            energiekeRegioId = energiekeRegioId,
        )
        val projectResult = repo.save(project)
        val projects = repo.getProjects()
        assertEquals(7, projects.size)
        assertEquals(projectResult.id, projects.last().id)
        assertEquals(projectResult.name, projects.last().name)
        assertEquals(energiekeRegioId, projects.last().energiekeRegioId)
    }

    @Test
    fun testSaveNewProject() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val projectName = "New Test Project"
        repo.saveNewProject(projectName)

        transaction(db) {
            val result = ProjectTable.selectAll().where{ ProjectTable.name eq projectName }.singleOrNull()
            assertNotNull(result)
            assertEquals(projectName, result[ProjectTable.name])
        }
    }

    @Test
    fun testGetProjectByEnergiekeRegioId() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val project = Project(
            name = "Test Project",
            energiekeRegioId = 123,
        )

        repo.save(project)
        val response = repo.getProjectByEnergiekeRegioId(123)
        assertNotNull(project)
        assertEquals(project.name, response.name)
        assertEquals(project.energiekeRegioId, response.energiekeRegioId)
    }

    @Test
    fun testGetProjectsByUserId() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)
        val userRepo = UserRepository(db)

        val userId = UUID.randomUUID()
        val projectName1 = "User's Project 1"
        val projectName2 = "User's Project 2"

        val projectId1 = repo.saveNewProject(projectName1)
        val projectId2 = repo.saveNewProject(projectName2)
        userRepo.saveUser(userId, listOf(projectId1, projectId2))

        val projects = repo.getProjectsByUserId(userId)
        assertEquals(2, projects.size)
        assertTrue(projects.any { it.name == projectName1 })
        assertTrue(projects.any { it.name == projectName2 })
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

    @Test
    fun testDeleteProject() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)

        val projectName = "Project to Delete"
        val projectId = repo.saveNewProject(projectName)
        val deleteResult = repo.deleteProject(projectId)

        // Verify that the project is successfully deleted
        assertTrue(deleteResult, "Project should be deleted successfully")

        // Verify that the project does not exist in the database anymore
        transaction(db) {
            val result = ProjectTable.selectAll().where { ProjectTable.id eq projectId }.singleOrNull()
            assertNull(result, "Deleted project should not be found in the database")
        }

        // Try to delete a non-existent project
        val nonExistentProjectId = UUID.randomUUID()
        val deleteNonExistentResult = repo.deleteProject(nonExistentProjectId)

        // Verify that deletion of a non-existent project returns false
        assertFalse(deleteNonExistentResult, "Deletion of non-existent project should return false")
    }

    @Test
    fun testUpdateProject() {
        val db = connectToPostgres()
        val repo = ProjectRepository(db)

        val originalProject = Project(
            name = "Initial Project",
            energiekeRegioId = 789,
            buurtCodes = listOf("B003")
        )
        val savedProject = repo.save(originalProject)

        // Update project details
        val updatedProject = savedProject.copy(
            name = "Updated Project Name",
            energiekeRegioId = 111,
            buurtCodes = listOf("B003", "B004")
        )
        var response = repo.save(updatedProject)
        var retrievedProject = repo.getProjectById(savedProject.id)

        assertNotNull(updatedProject)
        assertEquals(originalProject.id, response.id)
        assertEquals("Updated Project Name", response.name)
        assertEquals("Updated Project Name", retrievedProject?.name)
        assertEquals("Updated Project Name", updatedProject.name)
        assertEquals(111, updatedProject.energiekeRegioId)
        assertEquals(listOf("B003", "B004"), updatedProject.buurtCodes)
    }
}
