package com.example.assignment_login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        //회원가입 페이지 이동
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
        //로그인 상태 감지
        viewModel.loginCheck.observe(this) {
            if (viewModel.loginCheck.value == true) {
                Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show()
            }
//            else {
//                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
//            }
        }
        //로그인 시도
        binding.btnSignin.setOnClickListener {
            if (loginCheck()) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        //엔터키 제어
        binding.edId.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT ) {
                binding.edPw.requestFocus()
                Toast.makeText(this, "엔터", Toast.LENGTH_SHORT).show()
                true
            } else false
        }
    }
    //로그인 성공여부
    private fun loginCheck(): Boolean {
        val id = binding.edId.text.toString()
        val pw = binding.edPw.text.toString()
        if (MyApp.pref.containId(id) && MyApp.pref.getUser(id) == viewModel.getSign(pw)) {
            viewModel.isLoginCheck()
            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Toast.makeText(this, "아이디, 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}