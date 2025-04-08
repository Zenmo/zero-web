package com.zenmo.excelreadnamed.v5

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFCell

fun CellReference.oneToTheRight(): CellReference =
    CellReference(
        this.sheetName,
        this.row,
        this.col + 1,
        this.isRowAbsolute,
        this.isColAbsolute,
    )

fun XSSFCell.getNumber(): Double {
    return try {
        this.numericCellValue
    } catch (e: RuntimeException) {
        throw Exception("Can't read numer from cell ${this.reference}", e)
    }
}

fun yearToFirstOfJanuary(year: Int): Instant {
    val firstOfJanuary = LocalDate(year, 1, 1)
    return firstOfJanuary.atStartOfDayIn(TimeZone.of("CET"))
}
