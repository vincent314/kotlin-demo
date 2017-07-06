package org.vince.kotlindemo.customers

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.springframework.core.io.ClassPathResource
import java.io.File
import kotlin.coroutines.experimental.buildSequence

class Styles(workbook: SXSSFWorkbook) {
    var normalStyle: CellStyle
    var boldStyle: CellStyle

    init {
        val boldFont = workbook.createFont()
        boldFont.bold = true

        normalStyle = workbook.createCellStyle()
        boldStyle = workbook.createCellStyle()
        boldStyle.setFont(boldFont)
    }
}

class Workbook {
    val workbook: SXSSFWorkbook = SXSSFWorkbook()
    val styles: Styles = Styles(workbook)

    fun sheet(sheetName: String? = null,
              colSize: Array<Int>? = null,
              init: Sheet.() -> Unit
    ): Sheet {
        val sxssfSheet = workbook.createSheet(sheetName)

        colSize?.forEachIndexed { idx, width ->
            sxssfSheet.setColumnWidth(idx, width)
        }

        val sheet = Sheet(sxssfSheet, this)
        sheet.init()
        return sheet
    }

    fun write(file: File) {
        workbook.write(file.outputStream())
    }
}

class Sheet(val sheet: SXSSFSheet, val workbook: Workbook) {
    val rowSequence = buildIterator()

    fun row(init: Row.() -> Unit): Row {
        val row = Row(sheet.createRow(rowSequence.next()), workbook)
        row.init()
        return row
    }

    fun customers(filename: String): List<Customer> =
            readCustomers(ClassPathResource(filename).inputStream)
                    .filterNotNull()
}

class Row(val row: SXSSFRow, val workbook: Workbook) {
    val cellSequence = buildIterator()
    private fun buildCell(style: CellStyle? = null): SXSSFCell {
        val sxssfCell = row.createCell(cellSequence.next())

        sxssfCell.cellStyle = style ?: workbook.styles.normalStyle
        return sxssfCell
    }

    fun cell(text: String, style: CellStyle? = null) {
        val sxssfCell = buildCell(style)
        sxssfCell.setCellValue(text)
    }

    fun cell(value: Double, style: CellStyle? = null) {
        val sxssfCell = buildCell(style)
        sxssfCell.setCellValue(value)
    }
}

fun workbook(filename:String, init: Workbook.() -> Unit) {
    val workbook = Workbook()
    workbook.init()
    workbook.write(File(filename))
}


fun buildIterator(): Iterator<Int> {
    return buildSequence {
        var value = 0
        while (true) {
            yield(value++)
        }
    }.iterator()
}

fun main(args: Array<String>) {
    workbook(filename = "/tmp/test.xlsx") {
        sheet(
                sheetName = "Test 1",
                colSize = arrayOf(5000, 5000, 7000, 4000, 5000)) {
            row {
                cell("Prénom", styles.boldStyle)
                cell("Nom", styles.boldStyle)
                cell("Email", styles.boldStyle)
                cell("CA", styles.boldStyle)
                cell("Pays", styles.boldStyle)
            }
            customers("customers.json").forEach {
                row {
                    cell(it.firstname)
                    cell(it.lastname)
                    cell(it.email)
                    cell(it.turnover?.parseCurrency() ?: 0.0)
                    cell(it.country)
                }
            }
        }
    }
}

