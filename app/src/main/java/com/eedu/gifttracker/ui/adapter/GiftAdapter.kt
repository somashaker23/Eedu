package com.eedu.gifttracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eedu.gifttracker.data.database.entities.Gift
import com.eedu.gifttracker.databinding.ItemGiftBinding

class GiftAdapter(
    private val onEditClick: (Gift) -> Unit,
    private val onDeleteClick: (Gift) -> Unit
) : ListAdapter<Gift, GiftAdapter.GiftViewHolder>(GiftDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GiftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GiftViewHolder(private val binding: ItemGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gift: Gift) {
            binding.textViewGiverName.text = gift.giverName
            binding.textViewAmount.text = "₹${String.format("%.2f", gift.amount)}"
            binding.textViewRelationship.text = gift.relationship
            binding.textViewPaymentMethod.text = gift.paymentMethod
            binding.textViewVillage.text = gift.village
            binding.buttonEdit.setOnClickListener { onEditClick(gift) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(gift) }
        }
    }

    class GiftDiffCallback : DiffUtil.ItemCallback<Gift>() {
        override fun areItemsTheSame(oldItem: Gift, newItem: Gift) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Gift, newItem: Gift) = oldItem == newItem
    }
}
