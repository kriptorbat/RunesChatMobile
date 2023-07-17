package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.runeschat.R
import com.google.firebase.firestore.FirebaseFirestore

class sign_in() : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //FullScreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        val sign_in_register : TextView = findViewById(R.id.sign_in_signup)
        val sign_in_button : Button = findViewById(R.id.sign_in_button)

        sign_in_register.setOnClickListener {
            val intent = Intent(this,sign_up::class.java)
            startActivity(intent)
        }
        sign_in_button.setOnClickListener { addDataToFirestore() }

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
    private fun addDataToFirestore()
    {
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        val data : HashMap<String, Any> = hashMapOf<String,Any>()
        data.put("first_name","Stanislav")
        data.put("last_name", "Asanov")
        database.collection("users")
            .add(data)
            .addOnSuccessListener {documentReference ->
                Toast.makeText(this,"Data insperted",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,exception.message,Toast.LENGTH_SHORT).show()
            }
    }
}