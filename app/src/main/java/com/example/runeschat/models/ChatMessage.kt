package com.example.runeschat.models

import java.util.Date
import java.util.EventObject

class ChatMessage {
    lateinit var senderId: String
    lateinit var receiverId: String
    lateinit var message: String
    lateinit var dateTime: String
    lateinit var dateObject: Date
}
