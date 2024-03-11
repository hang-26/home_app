package com.example.homeapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.example.homeapp.MainActivity
import com.example.homeapp.R
import com.example.homeapp.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    var hint: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setLogin()
        setOnclickButton()
    }

    fun setOnclickButton() {
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.imvHidePassword.setOnClickListener {
            hint = !hint
            inputPass(hint)
        }
    }

//    private fun passwordVisibly() {
//        val currentTransformationMethod = binding.etPassWork.transformationMethod
//
//        if (currentTransformationMethod == PasswordTransformationMethod.getInstance()) {
//            // Hiển thị mật khẩu
//            binding.etPassWork.transformationMethod = null
//            binding.ivPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility) // Thay đổi icon khi hiển thị mật khẩu
//        } else {
//            // Ẩn mật khẩu
//            binding.etPassWork.transformationMethod = PasswordTransformationMethod.getInstance()
//            binding.ivPassword.setImageResource(com.google.android.material.R.drawable.design_ic_visibility_off) // Thay đổi icon khi ẩn mật khẩu
//        }
//    }

    fun setLogin() {
        val intent = Intent(this, MainActivity::class.java)
        var currentAccount = auth.currentUser
        if (currentAccount != null) {
            startActivity(intent)
        } else {
            binding.btLogin.setOnClickListener {
                val email = binding.etUserName.text.toString()
                val pass = binding.etPassWork.text.toString()
                if (email.isNotEmpty() && pass.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this," Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Điền thông tin đăng nhập", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    fun inputPass (hint: Boolean) {
        if (hint == false) {
            binding.imvHidePassword.setImageResource(R.drawable.icon_eye)
        } else {
            binding.etPassWork.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

}