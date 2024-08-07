package com.zenmo

import com.zenmo.zummon.companysurvey.*
import java.io.File
import java.util.*
import kotlinx.datetime.Instant as KotlinxInstant
import org.apache.poi.ss.util.AreaReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun getSurveyObject(filename: String): Survey {
    // Open file
    // val classloader = Thread.currentThread().contextClassLoader // Wat doet dit?
    val inputStream = File(filename).inputStream()
    val workbook = XSSFWorkbook(inputStream)
    var companyName = getStringField(workbook, "companyName")
    println("Getting survey data for company: $companyName")
    val realSurvey =
            Survey(
                    companyName = companyName,
                    zenmoProject = getNumericField(workbook, "projectId").toString(),
                    personName = "Contactpersoon",
                    addresses =
                            listOf(
                                    Address(
                                            id = UUID.randomUUID(),
                                            street = getStringField(workbook, "street"),
                                            houseNumber =
                                                    getNumericField(workbook, "houseNumberCombined")
                                                            .toInt(),
                                            city = getStringField(workbook, "city"),
                                            // street =
                                            // "Kerkstraat",
                                            // houseNumber = 35,
                                            // houseLetter = "A",
                                            // houseNumberSuffix = "100",
                                            // postalCode = "1234AB",
                                            // city =
                                            // "Amsterdam",
                                            gridConnections =
                                                    listOf(
                                                            GridConnection(
                                                                    electricity =
                                                                            Electricity(
                                                                                    hasConnection =
                                                                                            true,
                                                                                    annualElectricityDemandKwh =
                                                                                            getNumericField(
                                                                                                            workbook,
                                                                                                            "annualElectricityDeliveryKwh"
                                                                                                    )
                                                                                                    .toInt(),
                                                                                    annualElectricityProductionKwh =
                                                                                            getNumericField(
                                                                                                            workbook,
                                                                                                            "annualElectricityFeedinKwh"
                                                                                                    )
                                                                                                    .toInt(),
                                                                                    // ean =
                                                                                    // "123456789012345678",
                                                                                    quarterHourlyDelivery_kWh =
                                                                                            TimeSeries(
                                                                                                    getUsageTable(
                                                                                                            workbook,
                                                                                                            "quarterHourlyElectricityDeliveryKwh"
                                                                                                    )
                                                                                            ),
                                                                                    /*QuarterHourlyElectricityFeedin =
                                                                                    getUsageTable(
                                                                                            workbook,
                                                                                            "quarterHourlyElectricityFeedinKwh"
                                                                                    ),*/
                                                                                    grootverbruik =
                                                                                            CompanyGrootverbruik(
                                                                                                    contractedConnectionDemandCapacityKw =
                                                                                                            getNumericField(
                                                                                                                            workbook,
                                                                                                                            "contractedConnectionDeliveryCapacityKw"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    contractedConnectionSupplyCapacityKw =
                                                                                                            getNumericField(
                                                                                                                            workbook,
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
                                                                                                            workbook,
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
                                                                                    annualDemandM3 =
                                                                                            getNumericField(
                                                                                                            workbook,
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
                                                                        annualDistrictHeatingDemandGj = 300f,
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
                                                                                                                            workbook,
                                                                                                                            "numCars"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numElectricCars =
                                                                                                            getNumericField(
                                                                                                                            workbook,
                                                                                                                            "numElectricCars"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numChargePoints =
                                                                                                            getNumericField(
                                                                                                                            workbook,
                                                                                                                            "numChargePoints"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    powerPerChargePointKw =
                                                                                                            (getNumericField(
                                                                                                                            workbook,
                                                                                                                            "chargePointsTotalPowerKw"
                                                                                                                    ) /
                                                                                                                            getNumericField(
                                                                                                                                    workbook,
                                                                                                                                    "numChargePoints"
                                                                                                                            ))
                                                                                                                    .toFloat(),
                                                                                                    annualTravelDistancePerCarKm =
                                                                                                            getNumericField(
                                                                                                                            workbook,
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
                                                                                                                            workbook,
                                                                                                                            "numTrucks"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numElectricTrucks =
                                                                                                            getNumericField(
                                                                                                                            workbook,
                                                                                                                            "numElectricTrucks"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numChargePoints =
                                                                                                            0,
                                                                                                    powerPerChargePointKw =
                                                                                                            0f,
                                                                                                    annualTravelDistancePerTruckKm =
                                                                                                            getNumericField(
                                                                                                                            workbook,
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
                                                                                                                            workbook,
                                                                                                                            "numVans"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numElectricVans =
                                                                                                            getNumericField(
                                                                                                                            workbook,
                                                                                                                            "numElectricVans"
                                                                                                                    )
                                                                                                                    .toInt(),
                                                                                                    numChargePoints =
                                                                                                            0,
                                                                                                    powerPerChargePointKw =
                                                                                                            0f,
                                                                                                    annualTravelDistancePerVanKm =
                                                                                                            getNumericField(
                                                                                                                            workbook,
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

    /*
    val mockSurvey = Survey(
        companyName = "Zenmo",
        zenmoProject = "Project",
        personName = "John Doe",
        email = "john@example.com",
        dataSharingAgreed = true,
        addresses = listOf(
            Address(
                id = UUID.randomUUID(),
                street = "Kerkstraat",
                houseNumber = 35,
                houseLetter = "A",
                houseNumberSuffix = "100",
                postalCode = "1234AB",
                city = "Amsterdam",
                gridConnections = listOf(
                    GridConnection(
                        electricity = Electricity(
                            hasConnection = true,
                            annualElectricityDemandKwh = 1000,
                            annualElectricityProductionKwh = 2000,
                            ean = "123456789012345678",
                            quarterHourlyUsage = usageArray,
                            grootverbruik = CompanyGrootverbruik(
                                contractedConnectionDemandCapacityKw = 100,
                                contractedConnectionSupplyCapacityKw = 200,
                                physicalCapacityKw = 300,
                            ),
                            kleinverbruik = CompanyKleinverbruik(
                                connectionCapacity = KleinverbruikElectricityConnectionCapacity._3x63A,
                                consumptionProfile = KleinverbruikElectricityConsumptionProfile.TWO,
                            ),
                            authorizationFile = File(
                                blobName = "authorization.pdf",
                                originalName = "Authorization.pdf",
                                contentType = "application/pdf",
                                size = 10000,
                            ),
                            gridExpansion = GridExpansion(
                                hasRequestAtGridOperator = true,
                                requestedKW = 200,
                                reason = "Mooaar power!",
                            ),
                        ),
                        supply = Supply(
                            hasSupply = true,
                            pvInstalledKwp = 100,
                            pvOrientation = PVOrientation.SOUTH,
                            pvPlanned = true,
                            pvPlannedKwp = 200,
                            pvPlannedOrientation = PVOrientation.EAST_WEST,
                            pvPlannedYear = 2022,
                            windInstalledKw = 300f,
                            windPlannedKw = 400f,
                            otherSupply = "Other supply",
                            missingPvReason = MissingPvReason.OTHER,
                        ),
                        naturalGas = NaturalGas(
                            ean = "123456789012345678",
                            hasConnection = true,
                            annualDemandM3 = 3500,
                            hourlyValuesFiles = listOf(
                                File(
                                    blobName = "qwerty-uurwaarden-2022.csv",
                                    originalName = "uurwaarden-2022.csv",
                                    contentType = "text/csv",
                                    size = 1000,
                                ),
                            ),
                            percentageUsedForHeating = 50,
                        ),
                        heat = Heat(
                            heatingTypes = listOf(HeatingType.GAS_BOILER, HeatingType.DISTRICT_HEATING),
                            sumGasBoilerKw = 28.8f,
                            sumHeatPumpKw = 0f,
                            sumHybridHeatPumpElectricKw = 0f,
                            annualDistrictHeatingDemandGj = 300f,
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
                        energyOrBuildingManagementSystemSupplier = "EnergyBrothers",
                        surveyFeedback = "Survey feedback",
                        transport = Transport(
                            hasVehicles = false,
                            numDailyCarAndVanCommuters = 14,
                            numDailyCarVisitors = 5,
                            numCommuterAndVisitorChargePoints = 2,
                            cars = Cars(
                                numCars = 2,
                                numElectricCars = 0,
                                numChargePoints = 0,
                                powerPerChargePointKw = 0f,
                                annualTravelDistancePerCarKm = 0,
                                numPlannedElectricCars = 0,
                                numPlannedHydrogenCars = 2,
                            ),
                            trucks = Trucks(
                                numTrucks = 5,
                                numElectricTrucks = 0,
                                numChargePoints = 0,
                                powerPerChargePointKw = 0f,
                                annualTravelDistancePerTruckKm = 0,
                                numPlannedElectricTrucks = 0,
                                numPlannedHydrogenTrucks = 2,
                            ),
                            vans = Vans(
                                numVans = 2,
                                numElectricVans = 0,
                                numChargePoints = 0,
                                powerPerChargePointKw = 0f,
                                annualTravelDistancePerVanKm = 0,
                                numPlannedElectricVans = 0,
                                numPlannedHydrogenVans = 2,
                            ),
                            otherVehicles = OtherVehicles(
                                hasOtherVehicles = true,
                                description = "Other vehicles description",
                            )
                        )
                    )
                )
            )
        )
    )*/

    return realSurvey
}

fun getNumericField(workbook: XSSFWorkbook, field: String): Double {

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

fun getStringField(workbook: XSSFWorkbook, field: String): String {

    val stringValueName = workbook.getName(field)
    val areaRef = AreaReference(stringValueName.refersToFormula, workbook.spreadsheetVersion)
    val cellReference = areaRef.firstCell
    val stringValue =
            workbook.getSheet(cellReference.sheetName)
                    .getRow(cellReference.row)
                    .getCell(cellReference.col.toInt())
    return stringValue.stringCellValue
}

fun getUsageTable(workbook: XSSFWorkbook, field: String): List<TimeSeriesDataPoint> {
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

    var usageList: MutableList<TimeSeriesDataPoint> = mutableListOf()
    println("Table complete? $tableComplete")
    if (tableComplete) {

        val numCols = ref.lastCell.col - ref.firstCell.col + 1
        if (numCols != 2) {
            throw IllegalArgumentException("Number of columns in arrayField should be 2!")
        }

        val numRows = ref.lastCell.row - ref.firstCell.row + 1
        // println("numRows: $numRows")
        // var tableArray = emptyArray<Double>()

        // var tableArray = Array<Double>(numRows)

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
            val kotlinTimeStamp = KotlinxInstant.fromEpochMilliseconds(timeStamp.toEpochMilli())
            val currentUsage = TimeSeriesDataPoint(kotlinTimeStamp, usage_kWh)
            usageList.add(currentUsage)
            // tableArray(i) = currentUsage
        }
    }

    return usageList
}

fun getArrayField(workbook: XSSFWorkbook, field: String): Array<Double> {
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
    var tableArray = Array<Double>(numRows) { 0.0 }
    // var tableArray = Array<Double>(numRows)

    for (i in 0 until numRows) {

        val cell =
                workbook.getSheet(cellReference.sheetName)
                        .getRow(cellReference.row + i)
                        .getCell(cellReference.col.toInt() + 1)
        // println("cell value: ${cell.numericCellValue}")
        tableArray[i] = cell.numericCellValue
    }
    return tableArray
}

fun main(): Double {
    //    val logger = LogManager.getLogger();
    //    logger.info("Hello, world!")

    val classloader = Thread.currentThread().contextClassLoader

    val inputStream = classloader.getResourceAsStream("17832.Schunk Carbon Technology BV.xlsx")

    val workbook = XSSFWorkbook(inputStream)

    workbook.allNames.forEach { println(it.nameName) }

    val companyNameName = workbook.getName("companyName")

    val areaReference = AreaReference(companyNameName.refersToFormula, workbook.spreadsheetVersion)

    val cellReference = areaReference.firstCell
    val sheet = workbook.getSheet(cellReference.sheetName)
    val row = sheet.getRow(cellReference.row)
    val cell = row.getCell(cellReference.col.toInt())

    /*val cellValue: String = when (cell.cellType) {
        CellType.STRING -> cell.stringCellValue
        CellType.NUMERIC -> cell.numericCellValue.toString()
        CellType.BOOLEAN -> cell.booleanCellValue.toString()
        CellType.FORMULA -> {
            val evaluator: FormulaEvaluator = workbook.creationHelper.createFormulaEvaluator()
            val cellValue = evaluator.evaluate(cell)
            cellValue.formatAsString()
        }
        else -> ""
    }

    println("companyName: $cellValue")*/

    val quarterHourlyValuesName = workbook.getName("quarterHourlyElectricityDeliveryKwh")
    val ref = AreaReference(quarterHourlyValuesName.refersToFormula, workbook.spreadsheetVersion)
    val numCols = ref.lastCell.col - ref.firstCell.col + 1
    if (numCols != 2) {
        throw IllegalArgumentException(
                "Number of columns in quarterHourlyElectricityDeliveryKwh should be 2"
        )
    }

    val numRows = ref.lastCell.row - ref.firstCell.row + 1
    println("numRows: $numRows")

    println("first cell: ${ref.firstCell}")
    val firstCell =
            workbook.getSheet(ref.firstCell.sheetName)
                    .getRow(ref.firstCell.row)
                    .getCell(ref.firstCell.col.toInt())
    println("first cell date: ${firstCell.dateCellValue}")
    val firstValue =
            workbook.getSheet(ref.firstCell.sheetName)
                    .getRow(ref.firstCell.row)
                    .getCell(ref.firstCell.col + 1)
    println("first cell value: ${firstValue.numericCellValue}")
    val secondCell =
            workbook.getSheet(ref.firstCell.sheetName)
                    .getRow(ref.firstCell.row)
                    .getCell(ref.firstCell.col + 1)
    println("second cell value: ${secondCell.numericCellValue}")

    val summerCell =
            workbook.getSheet(ref.firstCell.sheetName)
                    .getRow(13_000)
                    .getCell(ref.firstCell.col.toInt())
    println("summer cell value: ${summerCell.dateCellValue}")

    for (i in 0 until 20) {
        val cell =
                workbook.getSheet(ref.firstCell.sheetName)
                        .getRow(8660 - (24 * 6 * 4) + i)
                        .getCell(ref.firstCell.col.toInt())
        println("cell value: ${cell.dateCellValue}")
    }

    // print columns of table vehicles
    /*workbook.getTable("vehicles").columns.forEach {
        println(it.name)
    }*/
    return firstValue.numericCellValue
}
