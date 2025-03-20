package com.zenmo.orm.companysurvey

import com.zenmo.orm.cleanDb
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.user.UserRepository
import com.zenmo.zummon.User
import com.zenmo.zummon.companysurvey.Project
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.toJavaUuid


class IndexSurveyRepositoryTest {
    val db = connectToPostgres()
    val indexSurveyRepository = IndexSurveyRepository(db)
    val surveyRepository = SurveyRepository(db)
    val projectRepository = ProjectRepository(db)
    val userRepository = UserRepository(db)
    val testUser = User(note = "Test User")

    @BeforeTest
    fun cleanUp() {
        cleanDb(db)
        userRepository.save(testUser)
    }

    @Test
    fun testAllSurveysIsEmpty() {
        assertEquals(0, indexSurveyRepository.getAllSurveys(testUser.id).size)
    }

    @Test
    fun testTwoSurveys() {
        projectRepository.save(Project(name = "Project",))
        surveyRepository.save(createMockSurvey(), testUser.id.toJavaUuid())
        surveyRepository.save(createMockSurvey(), testUser.id.toJavaUuid())

        assertEquals(2, indexSurveyRepository.getAllSurveys(testUser.id).size)
    }
}
