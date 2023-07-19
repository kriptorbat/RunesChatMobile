package com.example.runeschat.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.runeschat.databinding.ActivitySignUpBinding
import com.example.runeschat.utilites.Constants
import com.example.runeschat.utilites.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream

class sign_up : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding
    lateinit var preferenceManager : PreferenceManager
    private var encodedImage : String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferenceManager = PreferenceManager(applicationContext)
        setListner() //обработчик нажатий

        //FullScreen
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    private fun setListner(){
        //Кнопка регистрации
        binding.signUpButton.setOnClickListener {if (isValidSignUpDetails()) signUp()}

        //Текст кнопка перехода на кнопку входа
        binding.signUpSignIn.setOnClickListener {
            val intent = Intent(this,sign_in::class.java)
            startActivity(intent)
        }

        //Установить картинку
        binding.layoutImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    fun signUp(){
        loading(true)
        val database : FirebaseFirestore = FirebaseFirestore.getInstance()
        val users : HashMap<String,Any> = hashMapOf<String,Any>()
        users.put(Constants.KEY_NAME,binding.editLoginSignup.text.toString())
        users.put(Constants.KEY_EMAIL,binding.editEmailSignup.text.toString())
        users.put(Constants.KEY_PASSWORD,binding.editPasswordSignup.text.toString())
        users.put(Constants.KEY_IMAGE,encodedImage)
        database.collection(Constants.KEY_COLECTION_USERS)
            .add(users)
            .addOnSuccessListener {
                loading(false)
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true)
                preferenceManager.putString(Constants.KEY_USERS_ID,it.id)
                preferenceManager.putString(Constants.KEY_NAME,binding.editLoginSignup.text.toString())
                preferenceManager.putString(Constants.KEY_IMAGE,encodedImage)
                val intent : Intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                loading(false)
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
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

    //@Suppress("UNUSED_LAMBDA_EXPRESSION")
    private val pickImage : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode != 0) {
            if (it.data != null) {
                val imageUri = it.data!!.data
                try {
                    val inputStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                    val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.imageProfile.setImageBitmap(bitmap)
                    binding.textAddImage.visibility = View.GONE
                    encodedImage = encodeImage(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun isValidSignUpDetails() : Boolean{
        if (encodedImage == "") {
            Toast.makeText(this,"Selected profile image",Toast.LENGTH_SHORT).show()
            return false
        } else if(binding.editLoginSignup.text.toString().trim().isEmpty()) {
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.editEmailSignup.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            return false
        }else if (binding.editPasswordSignup.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.editPassword2Signup.text.toString().trim().isEmpty()){
            Toast.makeText(this,"Confirm password",Toast.LENGTH_SHORT).show()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editEmailSignup.text.toString()).matches()){
            Toast.makeText(this,"Enter correct email",Toast.LENGTH_SHORT).show()
            return false
        } else if (!binding.editPasswordSignup.text.toString().equals(binding.editPassword2Signup.text.toString())){
            Toast.makeText(this,"Password & ConfirmPassword",Toast.LENGTH_SHORT).show()
            return false
        } else return true
    }
    private fun loading(isLoading : Boolean)
    {
        if (isLoading) {
            binding.signUpButton.visibility = View.INVISIBLE
            binding.signUpProgressBar.visibility = View.VISIBLE
        } else {
            binding.signUpProgressBar.visibility = View.INVISIBLE
            binding.signUpButton.visibility = View.VISIBLE
        }
    }
}