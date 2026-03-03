package com.eedu.gifttracker.ui.events

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eedu.gifttracker.data.database.EeduDatabase
import com.eedu.gifttracker.data.database.entities.Event
import com.eedu.gifttracker.data.repository.EventRepository
import com.eedu.gifttracker.databinding.ActivityCreateEventBinding
import com.eedu.gifttracker.util.Constants
import com.eedu.gifttracker.viewmodel.EventViewModel

class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var eventViewModel: EventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Event"

        val database = EeduDatabase.getDatabase(applicationContext)
        val repository = EventRepository(database.eventDao())
        eventViewModel = ViewModelProvider(
            this,
            EventViewModel.Factory(repository)
        )[EventViewModel::class.java]

        setupEventTypeSpinner()
        setupSaveButton()
        observeInsertResult()
    }

    private fun setupEventTypeSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Constants.EVENT_TYPES
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerEventType.adapter = adapter
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val name = binding.editTextEventName.text.toString().trim()
            val type = binding.spinnerEventType.selectedItem.toString()
            val date = binding.editTextDate.text.toString().trim()

            if (name.isEmpty()) {
                binding.editTextEventName.error = "Event name is required"
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                binding.editTextDate.error = "Date is required"
                return@setOnClickListener
            }

            val event = Event(name = name, type = type, date = date)
            eventViewModel.insertEvent(event)
        }
    }

    private fun observeInsertResult() {
        eventViewModel.insertResult.observe(this) { id ->
            if (id > 0) {
                Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        eventViewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
