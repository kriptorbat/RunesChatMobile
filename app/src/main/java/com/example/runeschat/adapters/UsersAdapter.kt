package com.example.runeschat.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.runeschat.databinding.ItemContainerUserBinding
import com.example.runeschat.listners.UserListner
import com.example.runeschat.models.User

class UsersAdapter(private val users:List<User>,var userListner: UserListner) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemContainer: ItemContainerUserBinding = ItemContainerUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemContainer)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(users.get(position))
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemConteiner: ItemContainerUserBinding) : RecyclerView.ViewHolder(itemConteiner.root) {
        var binding = itemConteiner
        fun setUserData(user : User){
            binding.textName.text = user.name
            binding.textEmail.text = user.email
            binding.imageProfile.setImageBitmap(getUserImage(user.image))
            binding.root.setOnClickListener { userListner.onUserClicked(user) }
        }
    }
    private fun getUserImage(encodeImage : String) : Bitmap{
        val bytes = Base64.decode(encodeImage,Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes,0,bytes.size)
    }
}