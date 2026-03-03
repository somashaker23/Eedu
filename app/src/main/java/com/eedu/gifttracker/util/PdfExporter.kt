package com.eedu.gifttracker.util

import android.content.Context
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.database.entities.Gift
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PdfExporter(private val context: Context) {

    fun exportGiftsToPdf(event: Event, gifts: List<Gift>): File {
        val fileName = "eedu_${event.name.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(null), fileName)

        val pdfWriter = PdfWriter(file)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Title
        val title = Paragraph("Eedu - Gift Register")
            .setFontSize(20f)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
        document.add(title)

        // Event details
        document.add(Paragraph("Event: ${event.name}").setFontSize(14f).setBold())
        document.add(Paragraph("Type: ${event.type}").setFontSize(12f))
        document.add(Paragraph("Date: ${event.date}").setFontSize(12f))
        document.add(Paragraph("Total Gifts: ${gifts.size}").setFontSize(12f))
        val totalAmount = gifts.sumOf { it.amount }
        document.add(Paragraph("Total Amount: ₹${String.format("%.2f", totalAmount)}").setFontSize(14f).setBold())
        document.add(Paragraph("\n"))

        // Gifts table
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f, 2f, 2f, 2f, 2f)))
            .useAllAvailableWidth()

        // Header row
        val headers = listOf("#", "Name", "Amount (₹)", "Relationship", "Payment", "Village")
        headers.forEach { header ->
            table.addCell(
                Cell().add(Paragraph(header).setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        // Data rows
        gifts.forEachIndexed { index, gift ->
            table.addCell(Cell().add(Paragraph("${index + 1}").setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(gift.giverName)))
            table.addCell(Cell().add(Paragraph(String.format("%.2f", gift.amount)).setTextAlignment(TextAlignment.RIGHT)))
            table.addCell(Cell().add(Paragraph(gift.relationship)))
            table.addCell(Cell().add(Paragraph(gift.paymentMethod)))
            table.addCell(Cell().add(Paragraph(gift.village)))
        }

        document.add(table)

        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        document.add(Paragraph("\nGenerated on: ${dateFormat.format(Date())}").setFontSize(10f))

        document.close()
        return file
    }
}
