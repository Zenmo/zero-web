package com.zenmo.orm.companysurvey

import com.zenmo.orm.cleanDb
import com.zenmo.orm.connectToPostgres
import com.zenmo.orm.user.UserRepository
import com.zenmo.zummon.User
import com.zenmo.zummon.companysurvey.Project
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class IndexSurveyRepositoryTest {
    val db = connectToPostgres()
    val indexSurveyRepository = IndexSurveyRepository(db)
    val surveyRepository = SurveyRepository(db)
    val projectRepository = ProjectRepository(db)
    val userRepository = UserRepository(db)

    @BeforeTest
    fun cleanUp() {
        cleanDb(db)
    }

    @Test
    fun testAllSurveysIsEmpty() {
//        assertEquals(0, indexSurveyRepository.getAllSurveys().size)
    }

    @Test
    fun testTwoSurveys() {
        projectRepository.save(Project(name = "Project"))
        surveyRepository.save(createMockSurvey())
        surveyRepository.save(createMockSurvey())

//        assertEquals(2, indexSurveyRepository.getAllSurveys().size)
    }
}
