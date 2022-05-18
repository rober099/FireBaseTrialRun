package com.example.firebasetrialrun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasetrialrun.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.regBtn.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser(){
        binding.apply {
            val email = email.text.toString()
            val password = password.text.toString()

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this@MainActivity,"User Registered",Toast.LENGTH_SHORT).show()
                        if (auth.currentUser != null){
                            currentUser = auth.currentUser as FirebaseUser
                        }
                        emailVerify()
                    }else{
                        Toast.makeText(this@MainActivity,"Registration Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun loginUser(){
        binding.apply {
            val email = email.text.toString()
            val password = password.text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this@MainActivity,"Successful Login",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@MainActivity,"Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun emailVerify(){
        currentUser?.let{
            currentUser.sendEmailVerification().addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Toast.makeText(this@MainActivity,"Verification Email Sent",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity,"Error: Email Not Sent",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onStart(){
        super.onStart()
        auth = Firebase.auth
        if (this::currentUser.isInitialized){
            if (auth.currentUser != null && currentUser != auth){
                currentUser = auth.currentUser as FirebaseUser
            }
        }
    }
}