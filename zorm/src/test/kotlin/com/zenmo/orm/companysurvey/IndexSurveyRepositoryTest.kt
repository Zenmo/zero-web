package com.zenmo.orm.companysurvey

import com.zenmo.orm.cleanDb
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.user.UserRepository
import com.zenmo.zummon.User
import com.zenmo.zummon.companysurvey.Project
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid


class IndexSurveyRepositoryTest {
    val db = connectToPostgres()
    val indexSurveyRepository = IndexSurveyRepository(db)
    val surveyRepository = SurveyRepository(db)
    val projectRepository = ProjectRepository(db)
    val userRepository = UserRepository(db)
    val userId = UUID.randomUUID()

    @BeforeTest
    fun cleanUp() {
        cleanDb(db)
    }

    @Test
    fun testAllSurveysIsEmpty() {
        assertEquals(0, indexSurveyRepository.getAllSurveys(userId.toKotlinUuid()).size)
    }

    @Test
    fun testTwoSurveys() {
        val projectName = "Project"
        val projectId = projectRepository.saveNewProject(projectName)
        userRepository.saveUser(userId, listOf(projectId))
        surveyRepository.save(createMockSurvey(projectName),userId)
        surveyRepository.save(createMockSurvey(projectName), userId)

        assertEquals(2, indexSurveyRepository.getAllSurveys(userId.toKotlinUuid()).size)
    }
}
