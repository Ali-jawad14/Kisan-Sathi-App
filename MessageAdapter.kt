package com.example.kisansathi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kisansathi.databinding.ItemMessageBotBinding
import com.example.kisansathi.databinding.ItemMessageUserBinding

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_BOT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ItemMessageUserBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            UserViewHolder(binding)
        } else {
            val binding = ItemMessageBotBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            BotViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (holder is UserViewHolder) {
            holder.bind(message)
        } else if (holder is BotViewHolder) {
            holder.bind(message)
        }
    }

    inner class UserViewHolder(private val binding: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.text
        }
    }

    inner class BotViewHolder(private val binding: ItemMessageBotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.text
        }
    }
}
