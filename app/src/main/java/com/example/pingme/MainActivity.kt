package com.example.pingme

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        none.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }
        login.setOnClickListener {
            logMe()
        }
        forget.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view=layoutInflater.inflate(R.layout.dialog_box,null)
            builder.setView(view)
            val username = view.findViewById<EditText>(R.id.et_username)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which ->
                forgot(username)
            })
            builder.setNegativeButton("Close", DialogInterface.OnClickListener { dialog, which -> })
            builder.show()
        }
    }
    private fun forgot(username:EditText){
        if(username.text.toString().isEmpty()){
            return
        }
        if(! Patterns.EMAIL_ADDRESS.matcher(username.text.toString()).matches()){
            return
        }
        auth.sendPasswordResetEmail(username.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext,"Reset Password Email sent", Toast.LENGTH_SHORT).show()
                }
            }

    }
    private fun logMe(){
        if(e_mail.text.toString().isEmpty()){
            e_mail.error="Please enter an email address"
            e_mail.requestFocus()
            return
        }
        if(! Patterns.EMAIL_ADDRESS.matcher(e_mail.text.toString()).matches()){
            e_mail.error="Please enter a valid email address"
            e_mail.requestFocus()
            return
        }
        if(password.text.toString().isEmpty()){
            password.error="Please enter a password"
            password.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(e_mail.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                  val user=auth.currentUser
                updateUI(user)
                } else {
                    Toast.makeText(baseContext,"Login Failed", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                  }

            }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser:FirebaseUser?){
        if(currentUser!=null){
        if(currentUser.isEmailVerified){
            startActivity(Intent(this,Welcome::class.java))
            finish()
        }
            else{
            Toast.makeText(baseContext,"Please verify your email.", Toast.LENGTH_SHORT).show()
        }
        }
    }
}

