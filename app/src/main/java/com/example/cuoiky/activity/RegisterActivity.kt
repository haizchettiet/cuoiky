package com.example.cuoiky.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.cuoiky.R
import com.example.cuoiky.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progress : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        progress = ProgressDialog(this)
        progress.setTitle("Please wait")
        progress.setCanceledOnTouchOutside(false)

        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        btnBack()
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            validateData()
        }
    }
    private var firstname = ""
    private var lastname = ""
    private var email = ""
    private var password = ""
    private var condition = false
    private fun validateData() {
        firstname = binding.etFirstName.text.toString().trim()
        lastname = binding.etLastName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        condition = binding.cbCondition.isChecked

        if(firstname.isEmpty()){
            Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
            binding.etFirstName.requestFocus()
            return
        }
        else if(lastname.isEmpty()){
            Toast.makeText(this, "Please enter your last name", Toast.LENGTH_SHORT).show()
            binding.etLastName.requestFocus()
            return
        }
        else if(email.isEmpty()){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            binding.etEmail.requestFocus()
            return
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            binding.etEmail.requestFocus()
            return
        }
        else if(password.isEmpty()){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            binding.etPassword.requestFocus()
            return
        }
        else if(password.length < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            binding.etPassword.requestFocus()
            return
        }
        else if(confirmPassword.isEmpty()){
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
            binding.etConfirmPassword.requestFocus()
            return
        }
        else if(confirmPassword != password){
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show()
            binding.etConfirmPassword.requestFocus()
            return
        }
        else if(!condition){
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            binding.cbCondition.requestFocus()
            return
        }
        else{
            registerUser()
        }
    }

    private fun registerUser() {
        progress.setMessage("Creating your account...")
        progress.show()

        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progress.dismiss()
                val user = auth.currentUser
                val email = user?.email
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))

            }
            .addOnFailureListener {
                progress.dismiss()
                Toast.makeText(this, "Failed to create your account", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateAccount() {
        progress.setMessage("Save your account...")
        val uid = auth.uid
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["firstname"] = firstname
        hashMap["lastname"] = lastname
        hashMap["email"] = email
//        hashMap["usertype"] = "user"
        //send data to firebase

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!).setValue(hashMap)
            .addOnSuccessListener {
                progress.dismiss()
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                progress.dismiss()
                Toast.makeText(this, "Failed to create your account .Email already exists", Toast.LENGTH_SHORT).show()
            }
    }

    private fun btnBack(){
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

    }
}