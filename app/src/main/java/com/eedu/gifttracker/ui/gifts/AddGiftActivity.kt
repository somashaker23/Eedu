package com.eedu.gifttracker.ui.gifts

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eedu.gifttracker.data.database.EeduDatabase
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.data.repository.GiftRepository
import com.eedu.gifttracker.databinding.ActivityAddGiftBinding
import com.eedu.gifttracker.util.Constants
import com.eedu.gifttracker.viewmodel.GiftViewModel
import kotlinx.coroutines.launch

class AddGiftActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGiftBinding
    private lateinit var giftViewModel: GiftViewModel
    private var eventId: Long = 0
    private var giftId: Long = 0
    private var existingGift: Gift? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        eventId = intent.getLongExtra(Constants.EXTRA_EVENT_ID, 0)
        giftId = intent.getLongExtra(Constants.EXTRA_GIFT_ID, 0)

        val database = EeduDatabase.getDatabase(applicationContext)
        val repository = GiftRepository(database.giftDao())
        giftViewModel = ViewModelProvider(
            this,
            GiftViewModel.Factory(repository)
        )[GiftViewModel::class.java]

        setupSpinners()
        loadExistingGift()
        setupSaveButton()
        observeResults()
    }

    private fun setupSpinners() {
        val relationshipAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Constants.RELATIONSHIPS)
        relationshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRelationship.adapter = relationshipAdapter

        val paymentAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Constants.PAYMENT_METHODS)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = paymentAdapter
    }

    private fun loadExistingGift() {
        if (giftId > 0) {
            supportActionBar?.title = "Edit Gift"
            lifecycleScope.launch {
                val db = EeduDatabase.getDatabase(applicationContext)
                existingGift = db.giftDao().getGiftById(giftId)
                existingGift?.let { populateFields(it) }
            }
        } else {
            supportActionBar?.title = "Add Gift"
        }
    }

    private fun populateFields(gift: Gift) {
        binding.editTextGiverName.setText(gift.giverName)
        binding.editTextAmount.setText(gift.amount.toString())
        binding.editTextVillage.setText(gift.village)
        binding.editTextNotes.setText(gift.notes)

        val relIndex = Constants.RELATIONSHIPS.indexOf(gift.relationship)
        if (relIndex >= 0) binding.spinnerRelationship.setSelection(relIndex)

        val payIndex = Constants.PAYMENT_METHODS.indexOf(gift.paymentMethod)
        if (payIndex >= 0) binding.spinnerPaymentMethod.setSelection(payIndex)
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextGiverName.text.toString().trim()
            val amountStr = binding.editTextAmount.text.toString().trim()
            val village = binding.editTextVillage.text.toString().trim()
            val notes = binding.editTextNotes.text.toString().trim()
            val relationship = binding.spinnerRelationship.selectedItem.toString()
            val paymentMethod = binding.spinnerPaymentMethod.selectedItem.toString()

            if (name.isEmpty()) {
                binding.editTextGiverName.error = "Name is required"
                return@setOnClickListener
            }
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                binding.editTextAmount.error = "Enter a valid amount"
                return@setOnClickListener
            }

            val gift = Gift(
                id = existingGift?.id ?: 0,
                eventId = eventId,
                giverName = name,
                amount = amount,
                relationship = relationship,
                paymentMethod = paymentMethod,
                village = village,
                notes = notes,
                timestamp = existingGift?.timestamp ?: System.currentTimeMillis()
            )

            if (existingGift != null) {
                giftViewModel.updateGift(gift)
                Toast.makeText(this, "Gift updated", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                giftViewModel.insertGift(gift)
            }
        }
    }

    private fun observeResults() {
        giftViewModel.insertResult.observe(this) { id ->
            if (id > 0) {
                Toast.makeText(this, "Gift added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        giftViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
