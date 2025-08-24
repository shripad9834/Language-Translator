//package com.example.languagetranslator
//
//import android.content.Intent
//import android.content.pm.ActivityInfo
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//
//class Register : AppCompatActivity() {
//
//    private lateinit var mail: EditText
//    private lateinit var pass: EditText
//    private lateinit var btn: Button
//    private lateinit var pb: ProgressBar
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var tv: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
//        mAuth = FirebaseAuth.getInstance()
//        mail = findViewById(R.id.editTextEmail)
//        pass = findViewById(R.id.editTextPassword)
//        btn = findViewById(R.id.register)
//        pb = findViewById(R.id.progressbar)
//        tv = findViewById(R.id.loginnow)
//
//        tv.setOnClickListener {
//            val intent = Intent(applicationContext, Login::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        btn.setOnClickListener {
//            pb.visibility = View.VISIBLE
//            val email = mail.text.toString()
//            val password = pass.text.toString()
//
//            if (email.isEmpty()) {
//                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
//                pb.visibility = View.GONE
//                return@setOnClickListener
//            }
//
//            if (password.isEmpty()) {
//                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
//                pb.visibility = View.GONE
//                return@setOnClickListener
//            }
//
//            mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    pb.visibility = View.GONE
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "Register Successful !!!", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(applicationContext, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        Toast.makeText(this, "Incorrect Fields or already have the same account", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }
//}

//
//package com.example.languagetranslator
//
//import android.content.Intent
//import android.content.pm.ActivityInfo
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import androidx.appcompat.app.AppCompatActivity
//import com.google.firebase.auth.FirebaseAuth
//
//class Register : AppCompatActivity() {
//
//    private lateinit var mail: EditText
//    private lateinit var pass: EditText
//    private lateinit var btn: Button
//    private lateinit var pb: ProgressBar
//    private lateinit var mAuth: FirebaseAuth
//    private lateinit var tv: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
//        mAuth = FirebaseAuth.getInstance()
//        mail = findViewById(R.id.editTextEmail)
//        pass = findViewById(R.id.editTextPassword)
//        btn = findViewById(R.id.register)
//        pb = findViewById(R.id.progressbar)
//        tv = findViewById(R.id.loginnow)
//
//        tv.setOnClickListener {
//            val intent = Intent(applicationContext, Login::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        btn.setOnClickListener {
//            pb.visibility = View.VISIBLE
//            val email = mail.text.toString()
//            val password = pass.text.toString()
//
//            if (email.isEmpty()) {
//                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
//                pb.visibility = View.GONE
//                return@setOnClickListener
//            }
//
//            if (password.isEmpty()) {
//                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
//                pb.visibility = View.GONE
//                return@setOnClickListener
//            }
//
//            if (password.length < 8 || !password.matches(".*[!@#\$%^&*(),.?\":{}|<>].*".toRegex())) {
//                Toast.makeText(this, "Password must be at least 8 characters and include at least one special character", Toast.LENGTH_SHORT).show()
//                pb.visibility = View.GONE
//                return@setOnClickListener
//            }
//
//            mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    pb.visibility = View.GONE
//                    if (task.isSuccessful) {
//                        Toast.makeText(this, "Register Successful !!!", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(applicationContext, MainActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        Toast.makeText(this, "Incorrect Fields or already have the same account", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }
//}


package com.example.languagetranslator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.languagetranslator.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class Register : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var mail: EditText
    private lateinit var pass: EditText
    private lateinit var btn: Button
    private lateinit var pb: ProgressBar
    private lateinit var mAuth: FirebaseAuth
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        name = findViewById(R.id.editTextName)  // Get Name field
        mail = findViewById(R.id.editTextEmail)
        pass = findViewById(R.id.editTextPassword)
        btn = findViewById(R.id.register)
        pb = findViewById(R.id.progressbar)
        tv = findViewById(R.id.loginnow)

        tv.setOnClickListener {
            startActivity(Intent(applicationContext, Login::class.java))
            finish()
        }

        btn.setOnClickListener {
            pb.visibility = View.VISIBLE
            val userName = name.text.toString()
            val email = mail.text.toString()
            val password = pass.text.toString()

            if (userName.isEmpty()) {
                Toast.makeText(this, "Enter Full Name", Toast.LENGTH_SHORT).show()
                pb.visibility = View.GONE
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                pb.visibility = View.GONE
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                pb.visibility = View.GONE
                return@setOnClickListener
            }

            if (password.length < 8 || !password.matches(".*[!@#\$%^&*(),.?\":{}|<>].*".toRegex())) {
                Toast.makeText(this, "Password must be at least 8 characters and include at least one special character", Toast.LENGTH_SHORT).show()
                pb.visibility = View.GONE
                return@setOnClickListener
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    pb.visibility = View.GONE
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        updateUserProfile(user, userName)
                    } else {
                        Toast.makeText(this, "Incorrect Fields or already have the same account", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updateUserProfile(user: FirebaseUser?, userName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }
}
