package com.example.runeschat.activitys

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.runeschat.R
import com.example.runeschat.databinding.ActivityMainBinding
import com.example.runeschat.utilites.Constants
import com.example.runeschat.utilites.PreferenceManager
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity()
{
    lateinit var binding : ActivityMainBinding
    lateinit var preferenceManager: PreferenceManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(applicationContext)
        if (!preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
            finish()
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //FullScreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val animForText = AnimationUtils.loadAnimation(this,R.anim.text_anim)
        val animForImage = AnimationUtils.loadAnimation(this,R.anim.image_anim)
        val animForButton = AnimationUtils.loadAnimation(this,R.anim.button_anim)

        binding.textName.startAnimation(animForText)
        binding.imageProfile.startAnimation(animForImage)
        binding.imageSignOut.startAnimation(animForImage)
        binding.frame.startAnimation(animForButton)

        loadUser()
        getToken()
        setListners()

    }

    private fun setListners(){
        binding.imageSignOut.setOnClickListener{signOut()}
        binding.fabnewChat.setOnClickListener{
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadUser(){
        binding.textName.text = preferenceManager.getString(Constants.KEY_NAME)
        val bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        binding.imageProfile.setImageBitmap(bitmap)
    }

    @Suppress("UNUSED_EXPRESSION")
    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnSuccessListener(this::updateToken)
    }
    fun updateToken(token : String){
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        val documentReference : DocumentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
            preferenceManager.getString(Constants.KEY_USERS_ID))
        documentReference.update(Constants.KEY_FMC_TOKEN,token)
            .addOnFailureListener { e ->  Toast.makeText(this,"Unable to update token",Toast.LENGTH_SHORT).show()}
    }
    fun signOut(){
        Toast.makeText(this,"Signing out",Toast.LENGTH_SHORT).show()
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        val documentReference : DocumentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USERS_ID))
        val updates : HashMap<String,Any> = hashMapOf<String,Any>()
        updates.put(Constants.KEY_FMC_TOKEN,FieldValue.delete())
        documentReference.update(updates)
            .addOnSuccessListener { unused ->
                preferenceManager.clear()
                startActivity(Intent(applicationContext,sign_in::class.java))
                finish()
            }
            .addOnFailureListener { e -> Toast.makeText(this,"Token update",Toast.LENGTH_SHORT).show() }
    }
}