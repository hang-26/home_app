package com.example.homeapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.homeapp.MainActivity
import com.example.homeapp.R
import com.example.homeapp.data.UserData
import com.example.homeapp.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    lateinit var daRef: DatabaseReference
    var hint : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        daRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.btSignup.setOnClickListener {
            setUserData()
        }

        binding.ivViewPassword.setOnClickListener {
            hint = !hint
            hidePassword(hint)
        }
    }

    private fun hidePassword(hint: Boolean) {
        if (hint == false) {
            binding.ivViewPassword.setImageResource(R.drawable.icon_eye)
        } else {
            binding.edtPass.inputType = InputType.TYPE_CLASS_TEXT
            binding.ivViewPassword.setImageResource(R.drawable.icon_password)
        }
    }

    fun setUserData() {
        var email = binding.edtUserEmail.text.toString()
        var userPass = binding.edtPass.text.toString()
        var rePass = binding.edtRePass.text.toString()
        var userName = binding.edtNameUser.text.toString()
        var userDate = binding.edtAge.text.toString()
        var address = binding.edtAddress.text.toString()
        var numberPhone = binding.edtNumberPhone.text.toString()
        if (email.isNotEmpty() && userPass.isNotEmpty() ) {
            if (userPass == rePass) {
                auth.createUserWithEmailAndPassword(email, userPass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            var user = UserData(userId!!, userName, userDate, address, numberPhone, 0f)
                            daRef.child(userId!!).setValue(user)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        Toast.makeText(this, "Đăng ký người dùng thành công", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                        } else {

                        }

                    }

            } else {
                Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
            }
        }
    }
}