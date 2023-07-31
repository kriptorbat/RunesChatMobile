package com.example.runeschat.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runeschat.databinding.ItemContainerRecivedMessageBinding
import com.example.runeschat.databinding.ItemConteinerSentMessageBinding
import com.example.runeschat.models.ChatMessage

class ChatAdapter(var chatMessage :List<ChatMessage>, var receiverProfileImage: Bitmap,var senderId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return if (viewType == VIEW_TYPE_SENT){
            val itemContainer = ItemConteinerSentMessageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(itemContainer)
        } else{
            val itemContainer = ItemContainerRecivedMessageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(itemContainer)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            holder as SentMessageViewHolder
            holder.setData(chatMessage[position])
        } else {
            holder as ReceivedMessageViewHolder
            holder.setData(chatMessage[position],receiverProfileImage)
        }
    }

    override fun getItemCount(): Int = chatMessage.size

    override fun getItemViewType(position: Int): Int {
        return if (chatMessage[position].senderId == senderId){
            VIEW_TYPE_SENT
        }else{
            VIEW_TYPE_RECEIVED
        }
    }

    inner class SentMessageViewHolder(var itemConteiner: ItemConteinerSentMessageBinding) : RecyclerView.ViewHolder(itemConteiner.root) {
        fun setData(chatMessage: ChatMessage){
            itemConteiner.textMessage.text = chatMessage.message
            itemConteiner.textDateTime.text = chatMessage.dateTime
        }
    }
    inner class ReceivedMessageViewHolder(var itemConteiner: ItemContainerRecivedMessageBinding) : RecyclerView.ViewHolder(itemConteiner.root){
        fun setData(chatMessage: ChatMessage,receiverProfileImage: Bitmap){
            itemConteiner.textMessage.text = chatMessage.message
            itemConteiner.textDateTime.text = chatMessage.dateTime
            itemConteiner.imageProfile.setImageBitmap(receiverProfileImage)
        }
    }

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }
}