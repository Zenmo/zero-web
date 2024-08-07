package com.zenmo.excelreadnamed.v3

import com.zenmo.zummon.companysurvey.*
import com.zenmo.zummon.companysurvey.TimeSeriesUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.apache.poi.ss.util.AreaReference
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.util.*
import kotlin.time.Duration.Companion.minutes

class CompanyDataDocument(
    private val workbook: XSSFWorkbook
) {
    constructor(inputStream: java.io.InputStream) : this(XSSFWorkbook(inputStream))

    companion object {
        fun fromFile(fileName: String): CompanyDataDocument {
            val workbook = XSSFWorkbook(fileName)
            return CompanyDataDocument(workbook)
        }

        fun fromResource(resourceName: String): CompanyDataDocument {
            val inputStream = ClassLoader.getSystemResourceAsStream(resourceName)
            if (inputStream == null) {
                throw Exception("Resource not found: $resourceName")
            }
            return CompanyDataDocument(inputStream)
        }
    }

    fun getSurveyObject(): Survey {
        var companyName = getStringField("companyName")
        val realSurvey =
            Survey(
                companyName = companyName,
                zenmoProject = getNumericField("projectId").toString(),
                personName = "Contactpersoon",
                addresses =
                listOf(
                    Address(
                        id = UUID.randomUUID(),
                        street = getStringField("street"),
                        houseNumber =
                        getNumericField("houseNumberCombined")
                            .toInt(),
                        city = getStringField("city"),
                        gridConnections =
                        listOf(
                            GridConnection(
                                electricity =
                                Electricity(
                                    hasConnection =
                                    true,
                                    annualElectricityDelivery_kWh =
                                    getNumericField(
                                        "annualElectricityDeliveryKwh"
                                    )
                                        .toInt(),
                                    annualElectricityFeedIn_kWh =
                                    getNumericField(
                                        "annualElectricityFeedinKwh"
                                    )
                                        .toInt(),
                                    // ean =
                                    // "123456789012345678",
                                    quarterHourlyDelivery_kWh =
                                    getElectricityDeliveryTimeSeries(),
                                    /*QuarterHourlyElectricityFeedin =
                                    getUsageTable(
                                            workbook,
                                            "quarterHourlyElectricityFeedinKwh"
                                    ),*/
                                    grootverbruik =
                                    CompanyGrootverbruik(
                                        contractedConnectionDeliveryCapacity_kW =
                                        getNumericField(
                                            "contractedConnectionDeliveryCapacityKw"
                                        )
                                            .toInt(),
                                        contractedConnectionFeedInCapacity_kW =
                                        getNumericField(
                                            "contractedConnectionFeedinCapacityKw"
                                        )
                                            .toInt(),
                                        // physicalCapacityKw = 300,
                                    ),
                                ),
                                supply =
                                Supply(
                                    hasSupply =
                                    true,
                                    pvInstalledKwp =
                                    getNumericField(
                                        "pvInstalledKwp"
                                    )
                                        .toInt(),
                                    pvOrientation =
                                    PVOrientation
                                        .SOUTH,
                                    /*pvPlanned = true,
                                    pvPlannedKwp = 200,
                                    pvPlannedOrientation = PVOrientation.EAST_WEST,
                                    pvPlannedYear = 2022,
                                    windInstalledKw = 300f,
                                    windPlannedKw = 400f,
                                    otherSupply = "Other supply",
                                    missingPvReason = MissingPvReason.OTHER,*/
                                ),
                                naturalGas =
                                NaturalGas(
                                    ean =
                                    "123456789012345678",
                                    hasConnection =
                                    true,
                                    annualDelivery_m3 =
                                    getNumericField(
                                        "naturalGasAnnualDeliveryM3"
                                    )
                                        .toInt(),
                                    /*hourlyValuesFiles = listOf(
                                        File(
                                            blobName = "qwerty-uurwaarden-2022.csv",
                                            originalName = "uurwaarden-2022.csv",
                                            contentType = "text/csv",
                                            size = 1000,
                                        ),
                                    ),*/
                                    percentageUsedForHeating =
                                    100,
                                ),
                                /*heat = Heat(
                                    heatingTypes = listOf(HeatingType.GAS_BOILER, HeatingType.DISTRICT_HEATING),
                                    sumGasBoilerKw = 28.8f,
                                    sumHeatPumpKw = 0f,
                                    sumHybridHeatPumpElectricKw = 0f,
                                    annualDistrictHeatingDelivery_GJ = 300f,
                                    localHeatExchangeDescription = "Local heat exchange description",
                                    hasUnusedResidualHeat = false,
                                ),
                                storage = Storage(
                                    hasBattery = false,
                                    batteryCapacityKwh = 0f,
                                    batteryPowerKw = 0f,
                                    batterySchedule = "Battery schedule",
                                    hasPlannedBattery = true,
                                    plannedBatteryCapacityKwh = 100f,
                                    plannedBatteryPowerKw = 10f,
                                    plannedBatterySchedule = "Planned battery schedule",
                                    hasThermalStorage = true,
                                ),
                                mainConsumptionProcess = "Main consumption process",
                                electrificationPlans = "Electrification plans",
                                consumptionFlexibility = "Consumption flexibility",
                                energyOrBuildingManagementSystemSupplier = "EnergyBrothers",*/
                                surveyFeedback =
                                "Survey feedback",
                                transport =
                                Transport(
                                    hasVehicles =
                                    true,
                                    // numDailyCarAndVanCommuters = 14,
                                    // numDailyCarVisitors = 5,
                                    // numCommuterAndVisitorChargePoints = 2,
                                    cars =
                                    Cars(
                                        numCars =
                                        getNumericField(
                                            "numCars"
                                        )
                                            .toInt(),
                                        numElectricCars =
                                        getNumericField(
                                            "numElectricCars"
                                        )
                                            .toInt(),
                                        numChargePoints =
                                        getNumericField(
                                            "numChargePoints"
                                        )
                                            .toInt(),
                                        powerPerChargePointKw =
                                        (getNumericField(
                                            "chargePointsTotalPowerKw"
                                        ) /
                                                getNumericField(
                                                    "numChargePoints"
                                                ))
                                            .toFloat(),
                                        annualTravelDistancePerCarKm =
                                        getNumericField(
                                            "annualTravelDistancePerCarKm"
                                        )
                                            .toInt(),
                                        // numPlannedElectricCars = 0,
                                        // numPlannedHydrogenCars = 2,
                                    ),
                                    trucks =
                                    Trucks(
                                        numTrucks =
                                        getNumericField(
                                            "numTrucks"
                                        )
                                            .toInt(),
                                        numElectricTrucks =
                                        getNumericField(
                                            "numElectricTrucks"
                                        )
                                            .toInt(),
                                        numChargePoints =
                                        0,
                                        powerPerChargePointKw =
                                        0f,
                                        annualTravelDistancePerTruckKm =
                                        getNumericField(
                                            "annualTravelDistancePerTruckKm"
                                        )
                                            .toInt(),
                                        // numPlannedElectricTrucks = 0,
                                        // numPlannedHydrogenTrucks = 2,
                                    ),
                                    vans =
                                    Vans(
                                        numVans =
                                        getNumericField(
                                            "numVans"
                                        )
                                            .toInt(),
                                        numElectricVans =
                                        getNumericField(
                                            "numElectricVans"
                                        )
                                            .toInt(),
                                        numChargePoints =
                                        0,
                                        powerPerChargePointKw =
                                        0f,
                                        annualTravelDistancePerVanKm =
                                        getNumericField(
                                            "annualTravelDistancePerVanKm"
                                        )
                                            .toInt(),
                                        // numPlannedElectricVans = 0,
                                        // numPlannedHydrogenVans = 2,
                                    ),
                                    /*otherVehicles = OtherVehicles(
                                        hasOtherVehicles = true,
                                        description = "Other vehicles description",
                                    )*/
                                )
                            )
                        )
                    )
                )
            )

        return realSurvey
    }


    fun getNumericField(field: String): Double {

        val numericValueName = workbook.getName(field)
        if (numericValueName == null) {
            return 0.0
        } else {
            val ref = AreaReference(numericValueName.refersToFormula, workbook.spreadsheetVersion)
            val cellReference = ref.firstCell
            val numericValue =
                workbook.getSheet(cellReference.sheetName)
                    .getRow(cellReference.row)
                    .getCell(cellReference.col.toInt())
            return numericValue.numericCellValue
        }
    }

    fun getStringField(field: String): String {

        val stringValueName = workbook.getName(field)
        val areaRef = AreaReference(stringValueName.refersToFormula, workbook.spreadsheetVersion)
        val cellReference = areaRef.firstCell
        val stringValue =
            workbook.getSheet(cellReference.sheetName)
                .getRow(cellReference.row)
                .getCell(cellReference.col.toInt())
        return stringValue.stringCellValue
    }

    fun getUsageTable(field: String): FloatArray {
        val numericValueName = workbook.getName(field)
        val ref = AreaReference(numericValueName.refersToFormula, workbook.spreadsheetVersion)
        val cellReference = ref.firstCell
        // val sheet = workbook.getSheet(cellReference.sheetName)
        // val row = sheet.getRow(cellReference.row)
        // val cell = row.getCell(cellReference.col.toInt())
        // val numericValue =
        // workbook.getSheet(cellReference.sheetName).getRow(cellReference.row).getCell(cellReference.col.toInt())
        // return numericValue.numericCellValue

        // Check if table is complete
        val tableComplete =
            workbook.getSheet(cellReference.sheetName)
                .getRow(cellReference.row - 6)
                .getCell(cellReference.col.toInt() + 1)
                .booleanCellValue

        println("Table complete? $tableComplete")

        if (tableComplete) {
            val numCols = ref.lastCell.col - ref.firstCell.col + 1
            if (numCols != 2) {
                throw IllegalArgumentException("Number of columns in arrayField should be 2!")
            }

            val numRows = ref.lastCell.row - ref.firstCell.row + 1
            var result = FloatArray(numRows)

            for (i in 0 until numRows) {
                val usage_kWh =
                    workbook.getSheet(cellReference.sheetName)
                        .getRow(cellReference.row + i)
                        .getCell(cellReference.col.toInt() + 1)
                        .numericCellValue
                        .toFloat()
                // println("cell value: ${cell.numericCellValue}")

                val timeStamp =
                    workbook.getSheet(ref.firstCell.sheetName)
                        .getRow(ref.firstCell.row)
                        .getCell(ref.firstCell.col.toInt())
                        .dateCellValue
                        .toInstant()
                val kotlinTimeStamp = Instant.fromEpochMilliseconds(timeStamp.toEpochMilli())
                val currentUsage = TimeSeriesDataPoint(kotlinTimeStamp, usage_kWh)
                result[i] = currentUsage.value
            }
        }

        return floatArrayOf()
    }

    fun getArrayField(field: String): FloatArray {
        val numericValueName = workbook.getName(field)

        val ref = AreaReference(numericValueName.refersToFormula, workbook.spreadsheetVersion)

        val cellReference = ref.firstCell
        // val sheet = workbook.getSheet(cellReference.sheetName)
        // val row = sheet.getRow(cellReference.row)
        // val cell = row.getCell(cellReference.col.toInt())
        // val numericValue =
        // workbook.getSheet(cellReference.sheetName).getRow(cellReference.row).getCell(cellReference.col.toInt())
        // return numericValue.numericCellValue

        val numCols = ref.lastCell.col - ref.firstCell.col + 1
        if (numCols != 2) {
            throw IllegalArgumentException("Number of columns in arrayField should be 2!")
        }

        val numRows = ref.lastCell.row - ref.firstCell.row + 1
        println("numRows: $numRows")
        // var tableArray = emptyArray<Double>()
        var tableArray = FloatArray(numRows)
        // var tableArray = Array<Double>(numRows)

        for (i in 0 until numRows) {

            val cell =
                workbook.getSheet(cellReference.sheetName)
                    .getRow(cellReference.row + i)
                    .getCell(cellReference.col.toInt() + 1)
            // println("cell value: ${cell.numericCellValue}")
            tableArray[i] = cell.numericCellValue.toFloat()
        }
        return tableArray
    }

    fun getElectricityDeliveryTimeSeries(): TimeSeries? {
        val firstCell = getFirstCellOfNamedRange("quarterHourlyElectricityDeliveryKwh")
        if (!isTimeSeriesTableComplete(firstCell)) {
            return null
        }

        val year = getYearOfTimeSeries(firstCell)
        val start = yearToFirstOfJanuary(year)
        val values = getArrayField("quarterHourlyElectricityDeliveryKwh")

        return TimeSeries(
            type = TimeSeriesType.ELECTRICITY_DELIVERY,
            start = start,
            timeStep = 15.minutes,
            unit = TimeSeriesUnit.KWH,
            values = values
        )
    }

    fun yearToFirstOfJanuary(year: Int): Instant {
        val firstOfJanuary = LocalDate(year, 1, 1)
        return firstOfJanuary.atStartOfDayIn(TimeZone.of("CET"))
    }

    fun isTimeSeriesTableComplete(firstCell: CellReference): Boolean {
        // Find the cell that indicates if the table is complete
        return workbook.getSheet(firstCell.sheetName)
            .getRow(firstCell.row - 6)
            .getCell(firstCell.col.toInt() + 1)
            .booleanCellValue
    }

    fun getYearOfTimeSeries(firstCell: CellReference): Int {
        val year = workbook.getSheet(firstCell.sheetName)
            .getRow(1)
            .getCell(firstCell.col.toInt() + 1)
            .numericCellValue
            .toInt()

        if (year < 2000 || year > 2100) {
            throw IllegalArgumentException("Year of time series should be between 2000 and 2100")
        }

        return year
    }

    fun getFirstCellOfNamedRange(rangeName: String): CellReference {
        val numericValueName = workbook.getName(rangeName)
        val ref = AreaReference(numericValueName.refersToFormula, workbook.spreadsheetVersion)
        return ref.firstCell
    }
}