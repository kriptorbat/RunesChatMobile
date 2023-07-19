package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.runeschat.databinding.ActivitySignInBinding
import com.example.runeschat.utilites.Constants
import com.example.runeschat.utilites.PreferenceManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class sign_in() : AppCompatActivity() {

    lateinit var binding : ActivitySignInBinding
    lateinit var preferenceManager: PreferenceManager
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager = PreferenceManager(applicationContext)
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN))
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListner()

        //FullScreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        //Keyboard is visible
        //val r = Rect()
        //val constraint1 : ConstraintLayout = findViewById(R.id.constraint1)
        //constraint1.getWindowVisibleDisplayFrame(r);
        //val screenHeight: Int = constraint1.rootView.height
        //val keypadHeight = screenHeight - r.bottom
        //if (keypadHeight > screenHeight * 0.15) {
        //    Toast.makeText(this, "Keyboard is showing", Toast.LENGTH_LONG).show();
        //} else {
        //    Toast.makeText(this, "keyboard closed", Toast.LENGTH_LONG).show();
        //}
    }

    private fun setListner(){
        binding.signInSignup.setOnClickListener {
            val intent = Intent(this,sign_up::class.java)
            startActivity(intent)
        }
        binding.signInButton.setOnClickListener {
            if (isValidSignInDetails()) signIn()
        }
    }

    private fun signIn(){
        loading(true)
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL, binding.editEmailSignin.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,binding.editPasswordSignin.text.toString().toString())
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result.documents.size > 0){
                    val documentSnapshot : DocumentSnapshot = it.result.documents.get(0)
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                    preferenceManager.putString(Constants.KEY_USERS_ID,documentSnapshot.id)
                    preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME).toString())
                    preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE).toString())
                    val intent : Intent = Intent(applicationContext,MainActivity::class.java)
                    Toast.makeText(this,"You Are Winner",Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    loading(false)
                    Toast.makeText(this,"Unable to sign in",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun loading(isLoading : Boolean){
        if (isLoading){
            binding.signInButton.visibility = View.INVISIBLE
            binding.signInProgressBar.visibility = View.VISIBLE
        } else {
            binding.signInButton.visibility = View.VISIBLE
            binding.signInProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun isValidSignInDetails() : Boolean{
        return if (binding.editEmailSignin.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editEmailSignin.text.toString()).matches()){
            Toast.makeText(this,"Enter valid email",Toast.LENGTH_SHORT).show()
            false
        } else if (binding.editPasswordSignin.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show()
            false
        } else true
    }
}