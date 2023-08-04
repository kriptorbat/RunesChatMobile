package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import com.example.runeschat.R
import com.example.runeschat.adapters.ChatAdapter
import com.example.runeschat.databinding.ActivityChatBinding
import com.example.runeschat.models.ChatMessage
import com.example.runeschat.models.User
import com.example.runeschat.utilites.Constants
import com.example.runeschat.utilites.PreferenceManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    lateinit var receiverUser : User
    lateinit var chatMessages : MutableList<ChatMessage>
    lateinit var chatAdapter: ChatAdapter
    lateinit var preferenceManager: PreferenceManager
    lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) window.setFlags(
        //    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        //    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setListners()
        loadReciverDetails()
        init()
        listenMessage()
    }
    fun setListners(){
        binding.imageBack.setOnClickListener { onBackPressed()}
        binding.layoutSend.setOnClickListener { sendMessage() }
    }
    fun init(){
        preferenceManager = PreferenceManager(applicationContext)
        chatMessages = mutableListOf<ChatMessage>()
        chatAdapter = ChatAdapter(
            chatMessages,
            getBitmapFromEncodedString(receiverUser.image),
            preferenceManager.getString(Constants.KEY_USERS_ID)
        )
        binding.chatRecyclerView.adapter = chatAdapter
        database = FirebaseFirestore.getInstance()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun listenMessage(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERS_ID))
            .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.id)
            .addSnapshotListener{ value, error ->
                if (error != null) Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                if (value != null){
                    val count = chatMessages.size
                    for (documentChange : DocumentChange in value.documentChanges){
                        if (documentChange.type == DocumentChange.Type.ADDED){
                            val chatMessage = ChatMessage()
                            chatMessage.senderId = documentChange.document.getString(Constants.KEY_SENDER_ID).toString()
                            chatMessage.receiverId = documentChange.document.getString(Constants.KEY_RECEIVER_ID).toString()
                            chatMessage.message = documentChange.document.getString(Constants.KEY_MESSAGE).toString()
                            chatMessage.dateTime = getReadableDataTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!)
                            chatMessage.dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                            chatMessages.add(chatMessage)
                        }
                    }
                    Collections.sort(chatMessages,{obj1,obj2 -> obj1.dateObject.compareTo(obj2.dateObject)})
                    if (count == 0){
                        chatAdapter.notifyDataSetChanged()
                    } else {
                        chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                        binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size - 1)
                    }
                    binding.chatRecyclerView.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }
        database.collection(Constants.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.id)
            .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USERS_ID))
            .addSnapshotListener{ value, error ->
                if (error != null) Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                if (value != null){
                    val count = chatMessages.size
                    for (documentChange : DocumentChange in value.documentChanges){
                        if (documentChange.type == DocumentChange.Type.ADDED){
                            val chatMessage = ChatMessage()
                            chatMessage.senderId = documentChange.document.getString(Constants.KEY_SENDER_ID).toString()
                            chatMessage.receiverId = documentChange.document.getString(Constants.KEY_RECEIVER_ID).toString()
                            chatMessage.message = documentChange.document.getString(Constants.KEY_MESSAGE).toString()
                            chatMessage.dateTime = getReadableDataTime(documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!)
                            chatMessage.dateObject = documentChange.document.getDate(Constants.KEY_TIMESTAMP)!!
                            chatMessages.add(chatMessage)
                        }
                    }
                    Collections.sort(chatMessages,{obj1,obj2 -> obj1.dateObject.compareTo(obj2.dateObject)})
                    if (count == 0){
                        chatAdapter.notifyDataSetChanged()
                    } else {
                        chatAdapter.notifyItemRangeInserted(chatMessages.size,chatMessages.size)
                        binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size - 1)
                    }
                    binding.chatRecyclerView.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
            }
    }
    fun sendMessage(){
        val message : HashMap<String,Any> = hashMapOf<String,Any>()
        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USERS_ID))
        message.put(Constants.KEY_RECEIVER_ID,receiverUser.id)
        message.put(Constants.KEY_MESSAGE,binding.inputMessage.text.toString())
        message.put(Constants.KEY_TIMESTAMP, Date())
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message)
        binding.inputMessage.text = null
    }
    private fun getBitmapFromEncodedString(encodeImage : String) : Bitmap {
        val bytes = Base64.decode(encodeImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }
    fun getReadableDataTime(date : Date): String {
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }
    fun loadReciverDetails(){
        receiverUser = intent.getSerializableExtra(Constants.KEY_USER) as User
        binding.textName.text = receiverUser.name
    }
}