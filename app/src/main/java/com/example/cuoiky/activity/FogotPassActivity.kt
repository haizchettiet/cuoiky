package com.example.cuoiky.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.cuoiky.databinding.ActivityFogotPassBinding
import com.google.firebase.auth.FirebaseAuth

class FogotPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFogotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFogotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        binding.btnSubmit.setOnClickListener {
            fogotPass()
        }
    }

    private fun fogotPass(){
        val email: String = binding.etFogotEmail.text.toString().trim() { it <= ' ' }
        if(email.isEmpty()){
            binding.etFogotEmail.error = "Email is required"
            binding.etFogotEmail.requestFocus()
            return
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    } else {
                        Toast.makeText(this,task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}