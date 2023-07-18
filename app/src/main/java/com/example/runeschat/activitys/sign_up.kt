package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.runeschat.R
import java.io.ByteArrayOutputStream

class sign_up : AppCompatActivity() {

    private var encodedImage : String = ""

    var inputName : EditText? = null
    var inputEmail : EditText? = null
    var inputPassword : EditText? = null
    var inputPassword2 : EditText? = null
    var button : Button? = null
    var progressBar : ProgressBar? = null
    var frameL : FrameLayout? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        inputName = findViewById(R.id.editLoginSignup)
        inputEmail = findViewById(R.id.editEmailSignup)
        inputPassword = findViewById(R.id.editPasswordSignup)
        inputPassword2 = findViewById(R.id.editPassword2Signup)
        progressBar = findViewById(R.id.sign_up_progressBar)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        val sign_up_signin : TextView = findViewById(R.id.sign_up_signIn)
        sign_up_signin.setOnClickListener {
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }
        button = findViewById(R.id.sign_up_button)
        button?.setOnClickListener {
            if (isValidSignUpDetails()) signUp()
        }
    }

    fun signUp(){
        Toast.makeText(this,"Регистрация в разработке",Toast.LENGTH_SHORT).show()
    }

    private fun encodeImage(bitmap: Bitmap) : String
    {
        var previewWidth = 150
        var previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false)
        val byteArrayOutputStream :ByteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50 , byteArrayOutputStream)
        var bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    //private fun pickImage : ActivityResultLauncher<Intent> = registerForActivityResult(
    //    ActivityResultContracts.StartActivityForResult(),
    //    result -> {

    //    }
    //)
    private fun isValidSignUpDetails() : Boolean{
        if (encodedImage == null) {
            Toast.makeText(this,"Selected profile image",Toast.LENGTH_SHORT).show()
            return false
        } else if(inputName?.text.toString().trim().isEmpty()) {
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show()
            return false
        } else if (inputPassword?.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show()
            return false
        } else if (inputPassword2?.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Confirm password",Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail?.text.toString()).matches()){
            Toast.makeText(this,"Enter correct email",Toast.LENGTH_SHORT).show()
            return false
        } else if (!inputPassword?.text.toString().equals(inputPassword2?.text.toString())){
            Toast.makeText(this,"Password & ConfirmPassword",Toast.LENGTH_SHORT).show()
            return false
        } else return true
    }
    private fun loading(isLoading : Boolean)
    {
        if (isLoading)
        {
            button?.visibility = View.INVISIBLE
            progressBar?.visibility = View.VISIBLE
        }
        else
        {
            progressBar?.visibility = View.INVISIBLE
            button?.visibility = View.VISIBLE
        }
    }
}