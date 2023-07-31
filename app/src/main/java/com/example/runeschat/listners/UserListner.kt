package com.example.runeschat.listners

import com.example.runeschat.models.User

interface UserListner {
    fun onUserClicked(user: User)
}