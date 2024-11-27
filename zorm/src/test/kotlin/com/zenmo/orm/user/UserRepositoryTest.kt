package com.zenmo.orm.user

import com.zenmo.orm.companysurvey.ProjectRepository
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.table.UserProjectTable
import com.zenmo.orm.user.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.uuid.ExperimentalUuidApi

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

        transaction(db) {
            repo.saveUser(userId)
        }

        transaction(db) {
            val results = UserProjectTable.selectAll().where { UserProjectTable.userId eq userId }.toList()
            assertTrue(results.isEmpty())
        }
    }

    @Test
    fun `test getUsers returns all users when no filter is provided`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        transaction(db) {
            for (i in 1..5) {
                repo.saveUser(UUID.randomUUID(), note = "User $i note")
            }
        }
        val users = repo.getUsers()
        assertEquals(12, users.size)
    }

    @Test
    fun `test getUsers filters users based on given filter`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val userId = UUID.randomUUID()
        transaction(db) {
            repo.saveUser(userId, note = "Specific user note")
            for (i in 1..4) {
                repo.saveUser(UUID.randomUUID(), note = "User $i note")
            }
        }
        val users = repo.getUsers((UserTable.note eq "Specific user note"))
        assertEquals(1, users.size)
        assertEquals(userId, users.first().id)
    }

    @Test
    fun `test deleteUserById removes user correctly`() {
        val db = connectToPostgres()
        val repo = UserRepository(db)
        val userId = UUID.randomUUID()
        transaction(db) {
            repo.saveUser(userId, note = "Delete Test User")
        }

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
        transaction(db) {
            repo.saveUser(userId, note = "Delete Test User with Projects")
        }
        val projectIds = listOf(ProjectRepository(db).saveNewProject("Test Project"))

        transaction(db) {
            repo.saveUser(userId, projectIds)
        }

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