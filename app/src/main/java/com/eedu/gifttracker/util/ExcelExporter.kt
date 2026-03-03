package com.eedu.gifttracker.util

import android.content.Context
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.database.entities.Gift
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelExporter(private val context: Context) {

    fun exportGiftsToExcel(event: Event, gifts: List<Gift>): File {
        val fileName = "eedu_${event.name.replace(" ", "_")}_${System.currentTimeMillis()}.xlsx"
        val file = File(context.getExternalFilesDir(null), fileName)

        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Gifts")

        // Create header style
        val headerStyle = workbook.createCellStyle().apply {
            val font = workbook.createFont()
            font.bold = true
            setFont(font)
        }

        // Event summary row
        val summaryRow = sheet.createRow(0)
        summaryRow.createCell(0).apply {
            setCellValue("Event: ${event.name}")
            cellStyle = headerStyle
        }

        val dateRow = sheet.createRow(1)
        dateRow.createCell(0).setCellValue("Date: ${event.date}")

        val totalRow = sheet.createRow(2)
        totalRow.createCell(0).apply {
            setCellValue("Total Gifts: ${gifts.size}")
        }
        totalRow.createCell(1).apply {
            setCellValue("Total Amount: ₹${String.format("%.2f", gifts.sumOf { it.amount })}")
        }

        // Header row
        val headerRow = sheet.createRow(4)
        val headers = listOf("#", "Name", "Amount (₹)", "Relationship", "Payment Method", "Village", "Notes", "Date")
        headers.forEachIndexed { index, header ->
            headerRow.createCell(index).apply {
                setCellValue(header)
                cellStyle = headerStyle
            }
        }

        // Data rows
        gifts.forEachIndexed { index, gift ->
            val row = sheet.createRow(5 + index)
            row.createCell(0).setCellValue((index + 1).toDouble())
            row.createCell(1).setCellValue(gift.giverName)
            row.createCell(2).setCellValue(gift.amount)
            row.createCell(3).setCellValue(gift.relationship)
            row.createCell(4).setCellValue(gift.paymentMethod)
            row.createCell(5).setCellValue(gift.village)
            row.createCell(6).setCellValue(gift.notes)
            row.createCell(7).setCellValue(
                java.text.SimpleDateFormat("dd-MM-yyyy HH:mm", java.util.Locale.getDefault())
                    .format(java.util.Date(gift.timestamp))
            )
        }

        // Auto-size columns
        for (i in headers.indices) {
            sheet.autoSizeColumn(i)
        }

        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()

        return file
    }
}
