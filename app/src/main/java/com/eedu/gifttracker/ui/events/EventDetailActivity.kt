package com.eedu.gifttracker.ui.events

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eedu.gifttracker.data.database.EeduDatabase
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.EventRepository
import com.eedu.gifttracker.data.repository.GiftRepository
import com.eedu.gifttracker.databinding.ActivityEventDetailBinding
import com.eedu.gifttracker.ui.adapter.GiftAdapter
import com.eedu.gifttracker.ui.export.ExportActivity
import com.eedu.gifttracker.ui.gifts.AddGiftActivity
import com.eedu.gifttracker.util.Constants
import com.eedu.gifttracker.viewmodel.EventViewModel
import com.eedu.gifttracker.viewmodel.GiftViewModel

class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private lateinit var giftViewModel: GiftViewModel
    private lateinit var eventViewModel: EventViewModel
    private lateinit var giftAdapter: GiftAdapter
    private var eventId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventId = intent.getLongExtra(Constants.EXTRA_EVENT_ID, 0)
        if (eventId == 0L) {
            Toast.makeText(this, "Invalid event", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val database = EeduDatabase.getDatabase(applicationContext)
        val giftRepository = GiftRepository(database.giftDao())
        val eventRepository = EventRepository(database.eventDao())

        giftViewModel = ViewModelProvider(
            this,
            GiftViewModel.Factory(giftRepository)
        )[GiftViewModel::class.java]

        eventViewModel = ViewModelProvider(
            this,
            EventViewModel.Factory(eventRepository)
        )[EventViewModel::class.java]

        setupRecyclerView()
        observeData()
        setupFab()
        setupExportButton()
    }

    private fun setupRecyclerView() {
        giftAdapter = GiftAdapter(
            onEditClick = { gift -> editGift(gift) },
            onDeleteClick = { gift -> confirmDeleteGift(gift) }
        )
        binding.recyclerViewGifts.apply {
            layoutManager = LinearLayoutManager(this@EventDetailActivity)
            adapter = giftAdapter
        }
    }

    private fun observeData() {
        giftViewModel.getGiftsForEvent(eventId).observe(this) { gifts ->
            giftAdapter.submitList(gifts)
            binding.textViewGiftCount.text = "Gifts: ${gifts.size}"
            val total = gifts.sumOf { it.amount }
            binding.textViewTotalAmount.text = "Total: ₹${String.format("%.2f", total)}"
        }
    }

    private fun setupFab() {
        binding.fabAddGift.setOnClickListener {
            val intent = Intent(this, AddGiftActivity::class.java).apply {
                putExtra(Constants.EXTRA_EVENT_ID, eventId)
            }
            startActivity(intent)
        }
    }

    private fun setupExportButton() {
        binding.buttonExport.setOnClickListener {
            val intent = Intent(this, ExportActivity::class.java).apply {
                putExtra(Constants.EXTRA_EVENT_ID, eventId)
            }
            startActivity(intent)
        }
    }

    private fun editGift(gift: Gift) {
        val intent = Intent(this, AddGiftActivity::class.java).apply {
            putExtra(Constants.EXTRA_EVENT_ID, eventId)
            putExtra(Constants.EXTRA_GIFT_ID, gift.id)
        }
        startActivity(intent)
    }

    private fun confirmDeleteGift(gift: Gift) {
        AlertDialog.Builder(this)
            .setTitle("Delete Gift")
            .setMessage("Delete gift from ${gift.giverName}?")
            .setPositiveButton("Delete") { _, _ -> giftViewModel.deleteGift(gift) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
