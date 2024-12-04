package com.zenmo.orm.user

import com.zenmo.orm.companysurvey.ProjectRepository
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.orm.companysurvey.table.ProjectTable
import com.zenmo.orm.user.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import java.util.UUID
import kotlin.test.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

class UserRepositoryTest {
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
    fun `test saveUser inserts user with given id and note`() {
        val db = connectToPostgres()
        val userId = UUID.randomUUID()
        val note = "This is a test note"
        val repo = UserRepository(db)
        repo.saveUser(userId, note = note)
        val result = repo.getUserById(userId)
        assertNotNull(result)
        assertEquals(userId, result?.id)
        assertEquals(note, result?.note)
    }

    @Test
    @OptIn(ExperimentalUuidApi::class)
    fun `test saveUser inserts user with empty note by default`() {
        val userId = UUID.randomUUID()
        val db = connectToPostgres()
        val repo = UserRepository(db)
        repo.saveUser(userId)

        val result = repo.getUserById(userId)
        assertNotNull(result)
        assertEquals(userId, result.id)
        assertEquals("", result.note)
    }

    @Test
    fun `test saveUser inserts projects for given user`() {
        val userId = UUID.randomUUID()
        val db = connectToPostgres()

        val tmpProjectName = "testTMP"
        val projectId = ProjectRepository(db).saveNewProject(tmpProjectName)
        val repo = UserRepository(db)
        repo.saveUser(userId, listOf(projectId))

        transaction(db) {
            val result = UserProjectTable.selectAll()
                .where { UserProjectTable.userId eq userId and (UserProjectTable.projectId eq projectId) }.singleOrNull()
            assertNotNull(result)
            assertEquals(userId, result[UserProjectTable.userId])
            assertEquals(projectId, result[UserProjectTable.projectId])
        }
    }

    @Test
    fun `test saveUser inserts no projects if no projectIds are given`() {
        val userId = UUID.randomUUID()
        val db = connectToPostgres()
        val repo = UserRepository(db)
        repo.saveUser(userId)

        transaction(db) {
            val results = UserProjectTable.selectAll().where { UserProjectTable.userId eq userId }.toList()
            assertTrue(results.isEmpty())
        }
    }

    @Test
    fun `test getUsers returns all users when no filter is provided`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        for (i in 1..5) {
            repo.saveUser(UUID.randomUUID(), note = "User $i note")
        }
        val users = repo.getUsers()
        assertEquals(14, users.size)
    }

    @Test
    fun `test getUsers filters users based on given filter`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val userId = UUID.randomUUID()
        repo.saveUser(userId, note = "Specific user note")
        for (i in 1..4) {
            repo.saveUser(UUID.randomUUID(), note = "User $i note")
        }
        val users = repo.getUsers((UserTable.note eq "Specific user note"))
        assertEquals(1, users.size)
        assertEquals(userId, users.first().id)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Test
    fun `test getUsersAndProjects loads projects`() {
        val db = connectToPostgres()
        val userRepository = UserRepository(db)
        val projectRepository = ProjectRepository(db)

        val userId = UUID.randomUUID()
        val project1Id = projectRepository.saveNewProject("Project 1")
        val project2Id = projectRepository.saveNewProject("Project 2")

        // Save user with projects
        userRepository.saveUser(userId, listOf(project1Id, project2Id), "Test Note")

        // Retrieve users
        val users = userRepository.getUsersAndProjects(( UserTable.id eq userId ))
        val user = users.firstOrNull()

        // Assertions
        assertNotNull(user)
        assertEquals(userId, user.id)
        assertEquals("Test Note", user.note)
        assertEquals(2, user.projects.size)

        user.projects.forEach { project ->
            when (project.id) {
                project1Id.toKotlinUuid() -> assertEquals("Project 1", project.name)
                project2Id.toKotlinUuid() -> assertEquals("Project 2", project.name)
                else -> fail("Unexpected project ID ${project.id}")
            }
        }
    }

    @Test
    fun `test getUsersAndProjects handles users without projects`() {
        val db = connectToPostgres()
        val userRepository = UserRepository(db)

        val userId = UUID.randomUUID()
        // Save user without projects
        userRepository.saveUser(userId, note = "User without projects")

        // Retrieve users
        val users = userRepository.getUsersAndProjects()
        val user = users.find { it.id == userId }

        // Assertions
        assertNotNull(user)
        assertEquals(userId, user?.id)
        assertEquals("User without projects", user?.note)
        assertTrue(user?.projects?.isEmpty() == true)
    }
    

    @Test
    fun `test deleteUserById removes user correctly`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val userId = UUID.randomUUID()
        repo.saveUser(userId, note = "Delete Test User")

        val userBeforeDelete = repo.getUserById(userId)
        assertNotNull(userBeforeDelete)

        repo.deleteUserById(userId)
        val userAfterDelete = repo.getUserById(userId)
        assertTrue(userAfterDelete == null)
    }

    @Test
    fun `test deleteUserById removes user projects as well`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val userId = UUID.randomUUID()
        repo.saveUser(userId, note = "Delete Test User with Projects")
        val projectIds = listOf(ProjectRepository(db).saveNewProject("Test Project"))
        repo.saveUser(userId, projectIds)

        val userProjectsBeforeDelete = transaction(db) {
            UserProjectTable.selectAll().where { UserProjectTable.userId eq userId }.toList()
        }
        assertTrue(userProjectsBeforeDelete.isNotEmpty())

        repo.deleteUserById(userId)

        val userProjectsAfterDelete = transaction(db) {
            UserProjectTable.selectAll().where { UserProjectTable.userId eq userId }.toList()
        }
        assertTrue(userProjectsAfterDelete.isEmpty())
    }

    @Test
    fun `test deleteUserById does nothing if user does not exist`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val nonExistentUserId = UUID.randomUUID()

        repo.deleteUserById(nonExistentUserId)
        // If no exceptions are thrown, the test passes as there's no user to delete
        assertTrue(true)
    }
}