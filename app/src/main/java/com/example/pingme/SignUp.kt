package com.example.pingme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()

        signup.setOnClickListener{
            sign_fun()
        }
    }
    private fun sign_fun(){
        if(mail.text.toString().isEmpty()){
            mail.error="Please enter an email address"
            mail.requestFocus()
            return
        }
        if(! Patterns.EMAIL_ADDRESS.matcher(mail.text.toString()).matches()){
            mail.error="Please enter a valid email address"
            mail.requestFocus()
            return
        }
        if(pwd.text.toString().isEmpty()){
            pwd.error="Please enter a password"
            pwd.requestFocus()
            return
        }
        auth.createUserWithEmailAndPassword(mail.text.toString(), pwd.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user=auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(baseContext,"Sign-Up Failed",Toast.LENGTH_SHORT).show()
                }

            }
    }

}
