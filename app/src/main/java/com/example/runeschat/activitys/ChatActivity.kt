package com.example.runeschat.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.runeschat.databinding.ActivityChatBinding
import com.example.runeschat.models.ChatMessage
import com.example.runeschat.models.User
import com.example.runeschat.utilites.Constants

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    lateinit var receiverUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListners()
        loadReciverDetails()
    }

    fun setListners(){
        binding.imageBack.setOnClickListener { onBackPressed() }
    }

    fun loadReciverDetails(){
        receiverUser = intent.getSerializableExtra(Constants.KEY_USER) as User
        binding.textName.text = receiverUser.name
    }
}