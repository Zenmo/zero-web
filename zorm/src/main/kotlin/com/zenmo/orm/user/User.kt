package com.zenmo.orm.user

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.zenmo.zummon.companysurvey.Project

data class User(
    // Keycloak id
    val id: Uuid = uuid4(),
    val projects: List<Project> = emptyList(),
    val note: String,
)
