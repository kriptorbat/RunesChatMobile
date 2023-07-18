package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.runeschat.utilites.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.makeramen.roundedimageview.RoundedImageView
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class sign_up : AppCompatActivity() {

    private var encodedImage : String = ""

    var inputName : EditText? = null
    var inputEmail : EditText? = null
    var inputPassword : EditText? = null
    var inputPassword2 : EditText? = null
    var button : Button? = null
    var progressBar : ProgressBar? = null
    var frameL : FrameLayout? = null
    var imageProfile : RoundedImageView? = null
    var textAddImage : TextView? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        inputName = findViewById(R.id.editLoginSignup)
        inputEmail = findViewById(R.id.editEmailSignup)
        inputPassword = findViewById(R.id.editPasswordSignup)
        inputPassword2 = findViewById(R.id.editPassword2Signup)
        progressBar = findViewById(R.id.sign_up_progressBar)
        frameL = findViewById(R.id.layoutImage)
        imageProfile = findViewById(R.id.imageProfile)
        textAddImage = findViewById(R.id.textAddImage)

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
        button?.setOnClickListener {if (isValidSignUpDetails()) signUp()}

        frameL?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }

    }

    fun signUp(){
        loading(true)
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        val users : HashMap<String,Any> = hashMapOf<String,Any>()
        users.put(Constants.KEY_NAME,inputName?.text.toString())
        users.put(Constants.KEY_EMAIL,inputEmail?.text.toString())
        users.put(Constants.KEY_PASSWORD,inputPassword?.text.toString())
        users.put(Constants.KEY_IMAGE,encodedImage)
        database.collection(Constants.KEY_COLECTION_USERS)
            .add(users)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
        //Toast.makeText(this,"Регистрация в разработке",Toast.LENGTH_SHORT).show()
    }

    private fun encodeImage(bitmap: Bitmap) : String
    {
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false)
        val byteArrayOutputStream :ByteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50 , byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    @Suppress("UNUSED_LAMBDA_EXPRESSION")
    private val pickImage : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){result -> {
            if(result.resultCode != null){
                if(result.data != null){
                    val imageUri = result.data!!.data
                    try {
                        val inputStream : InputStream? = contentResolver.openInputStream(imageUri!!)
                        val bitmap : Bitmap = BitmapFactory.decodeStream(inputStream)
                        imageProfile?.setImageBitmap(bitmap)
                        textAddImage?.visibility = View.GONE
                        encodedImage = encodeImage(bitmap)
                    } catch ( e : FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
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