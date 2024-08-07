package com.zenmo.excelreadnamed.v5

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.apache.poi.ss.util.CellReference

fun CellReference.oneToTheRight(): CellReference =
    CellReference(
        this.sheetName,
        this.row,
        this.col + 1,
        this.isRowAbsolute,
        this.isColAbsolute,
    )

fun yearToFirstOfJanuary(year: Int): Instant {
    val firstOfJanuary = LocalDate(year, 1, 1)
    return firstOfJanuary.atStartOfDayIn(TimeZone.of("CET"))
}
