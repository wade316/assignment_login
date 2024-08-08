package com.example.assignment_login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.assignment_login.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        binding.ibtPwHide.setOnClickListener {//비밀번호 보이기 & 숨기기
            when(it.tag) {
                "0" -> {
                    binding.ibtPwHide.tag = "1"
                    binding.edPw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.ibtPwHide.setImageResource(R.drawable.img_unhide)
                }
                "1" -> {
                    binding.ibtPwHide.tag = "0"
                    binding.edPw.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.ibtPwHide.setImageResource(R.drawable.img_hide)
                }
            }
        }
    }
}