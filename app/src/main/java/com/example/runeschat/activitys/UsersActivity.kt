package com.example.runeschat.activitys

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.runeschat.adapters.UsersAdapter
import com.example.runeschat.databinding.ActivityUsersBinding
import com.example.runeschat.listners.UserListner
import com.example.runeschat.models.User
import com.example.runeschat.utilites.Constants
import com.example.runeschat.utilites.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class UsersActivity : AppCompatActivity(), UserListner {

    lateinit var binding : ActivityUsersBinding
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //fullscreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        preferenceManager = PreferenceManager(applicationContext)
        setListner()
        getUsers()
    }

    fun setListner(){
        binding.imageBack.setOnClickListener { onBackPressed() }
    }

    fun getUsers(){
        loading(true)
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener {
                loading(false)
                val currentUserId : String = preferenceManager.getString(Constants.KEY_USERS_ID)
                if (it.isSuccessful && it.result != null){
                    val users = mutableListOf<User>()
                    for (queryDocumentSnapshot : QueryDocumentSnapshot in it.result){
                        if (currentUserId.equals(queryDocumentSnapshot.id)) continue
                        val user = User()
                        user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME).toString()
                        user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL).toString()
                        user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE).toString()
                        user.token = queryDocumentSnapshot.getString(Constants.KEY_FMC_TOKEN).toString()
                        user.id = queryDocumentSnapshot.id
                        users.add(user)
                    }
                    if (users.size > 0){
                        val usersAdapter = UsersAdapter(users,this)
                        binding.userRecyclerView.adapter = usersAdapter
                        binding.userRecyclerView.visibility = View.VISIBLE
                    } else {
                        showErrorMessage()
                    }
                } else {
                    showErrorMessage()
                }
            }
    }
    fun showErrorMessage(){
        binding.textErrorMessage.text = String.format("%s","No user availble")
        binding.textErrorMessage.visibility = View.VISIBLE
    }
    fun loading(isLoading : Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onUserClicked(user: User) {
        val intent = Intent(applicationContext,ChatActivity::class.java)
        intent.putExtra(Constants.KEY_USER,user)
        startActivity(intent)
        finish()
    }
}