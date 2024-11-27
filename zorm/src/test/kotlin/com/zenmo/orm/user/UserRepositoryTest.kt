package com.zenmo.orm.user

import com.zenmo.orm.companysurvey.ProjectRepository
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.createSchema
import com.zenmo.orm.user.table.UserProjectTable
import org.jetbrains.exposed.sql.*
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
        repo.saveUser(db, userId, note = note)
        val result = repo.getUserById(db, userId)
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

        repo.saveUser(db, userId)
        val result = repo.getUserById(db, userId)
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

        repo.saveUser(db, userId, listOf(projectId))
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
            repo.saveUser(db, userId)
        }

        transaction(db) {
            val results = UserProjectTable.selectAll().where { UserProjectTable.userId eq userId }.toList()
            assertTrue(results.isEmpty())
        }
    }
}