package com.zenmo.excelreadnamed.v5

import com.zenmo.zummon.companysurvey.Project
import kotlin.uuid.ExperimentalUuidApi

fun interface ProjectProvider {
    /**
     * Fetch additional project data using the ID from the Excel.
     */
    fun getProjectByEnergiekeRegioId(energiekeRegioId: Int): Project

    companion object {
        @OptIn(ExperimentalUuidApi::class)
        val default: ProjectProvider = ProjectProvider {
            Project(
                energiekeRegioId = it,
                name = "Energieke Regio project $it",
            )
        }
    }
}
