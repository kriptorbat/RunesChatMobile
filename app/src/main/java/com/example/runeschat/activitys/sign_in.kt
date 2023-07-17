package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.runeschat.R

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

        sign_in_register.setOnClickListener {
            val intent = Intent(this,sign_up::class.java)
            startActivity(intent)
        }

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
}