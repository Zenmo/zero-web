package com.zenmo.diagram

import com.zenmo.zummon.companysurvey.Address
import com.zenmo.zummon.companysurvey.Survey
import io.github.kelvindev15.kotlin2plantuml.plantuml.ClassDiagram
import io.github.kelvindev15.kotlin2plantuml.plantuml.Configuration
import java.nio.file.Files
import kotlin.io.path.Path

fun main() {
    val diagramString = ClassDiagram(
        Survey::class,
        Address::class,
        configuration = Configuration()
    ).plantUml()

    Files.write(Path("zummon.plantuml"), diagramString.toByteArray())
}