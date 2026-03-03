package com.eedu.gifttracker.ui.export

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eedu.gifttracker.data.database.EeduDatabase
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.EventRepository
import com.eedu.gifttracker.data.repository.GiftRepository
import com.eedu.gifttracker.databinding.ActivityExportBinding
import com.eedu.gifttracker.util.Constants
import com.eedu.gifttracker.viewmodel.ExportViewModel
import kotlinx.coroutines.launch
import java.io.File

class ExportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExportBinding
    private lateinit var exportViewModel: ExportViewModel
    private lateinit var eventRepository: EventRepository
    private lateinit var giftRepository: GiftRepository
    private var eventId: Long = 0
    private var currentEvent: Event? = null
    private var currentGifts: List<Gift> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Export"

        eventId = intent.getLongExtra(Constants.EXTRA_EVENT_ID, 0)

        val database = EeduDatabase.getDatabase(applicationContext)
        eventRepository = EventRepository(database.eventDao())
        giftRepository = GiftRepository(database.giftDao())

        exportViewModel = ViewModelProvider(
            this,
            ExportViewModel.Factory(eventRepository, giftRepository)
        )[ExportViewModel::class.java]

        loadData()
        observeExport()
    }

    private fun loadData() {
        lifecycleScope.launch {
            currentEvent = eventRepository.getEventById(eventId)
            currentGifts = giftRepository.getGiftsForEventList(eventId)

            currentEvent?.let { event ->
                binding.textViewEventName.text = event.name
                binding.textViewGiftCount.text = "Gifts: ${currentGifts.size}"
                binding.textViewTotalAmount.text = "Total: ₹${String.format("%.2f", currentGifts.sumOf { it.amount })}"
            }

            setupExportButtons()
        }
    }

    private fun setupExportButtons() {
        binding.buttonExportPdf.setOnClickListener {
            val event = currentEvent ?: return@setOnClickListener
            if (isPaymentRequired()) {
                showPaymentRequired()
            } else {
                exportViewModel.exportToPdf(this, event, currentGifts)
            }
        }

        binding.buttonExportExcel.setOnClickListener {
            val event = currentEvent ?: return@setOnClickListener
            if (isPaymentRequired()) {
                showPaymentRequired()
            } else {
                exportViewModel.exportToExcel(this, event, currentGifts)
            }
        }
    }

    private fun isPaymentRequired(): Boolean {
        val prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE)
        val paymentDone = prefs.getBoolean(Constants.PREF_PAYMENT_DONE, false)
        return !paymentDone && currentGifts.size > Constants.FREE_TIER_GIFT_LIMIT
    }

    private fun showPaymentRequired() {
        Toast.makeText(
            this,
            "Export is locked. Please complete payment of ₹100 to unlock.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun observeExport() {
        exportViewModel.isLoading.observe(this) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        exportViewModel.exportedFile.observe(this) { file ->
            file?.let { shareFile(it) }
        }

        exportViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareFile(file: File) {
        val uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            file
        )
        val mimeType = when {
            file.name.endsWith(".pdf") -> "application/pdf"
            file.name.endsWith(".xlsx") -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            else -> "application/octet-stream"
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
