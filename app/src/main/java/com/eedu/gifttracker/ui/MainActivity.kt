package com.eedu.gifttracker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eedu.gifttracker.data.database.EeduDatabase
import com.eedu.gifttracker.data.repository.EventRepository
import com.eedu.gifttracker.databinding.ActivityMainBinding
import com.eedu.gifttracker.ui.adapter.EventAdapter
import com.eedu.gifttracker.ui.events.CreateEventActivity
import com.eedu.gifttracker.ui.events.EventDetailActivity
import com.eedu.gifttracker.util.Constants
import com.eedu.gifttracker.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var eventViewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val database = EeduDatabase.getDatabase(applicationContext)
        val repository = EventRepository(database.eventDao())
        eventViewModel = ViewModelProvider(
            this,
            EventViewModel.Factory(repository)
        )[EventViewModel::class.java]

        setupRecyclerView()
        observeEvents()
        setupFab()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra(Constants.EXTRA_EVENT_ID, event.id)
            }
            startActivity(intent)
        }
        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = eventAdapter
        }
    }

    private fun observeEvents() {
        eventViewModel.allEvents.observe(this) { events ->
            eventAdapter.submitList(events)
            binding.textViewEmpty.visibility =
                if (events.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun setupFab() {
        binding.fabAddEvent.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }
    }
}
