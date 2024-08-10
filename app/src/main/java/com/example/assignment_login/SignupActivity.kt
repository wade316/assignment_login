package com.example.assignment_login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_login.databinding.ActivitySignupBinding
import java.security.DigestException
import java.security.MessageDigest
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModels<MainViewModel>()
    private val userInfo = mutableListOf<UserInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_signup)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        pwCheckChanged()
        isFocus()
        isClick()


    }
    private fun isClick() {
        binding.btnDone.setOnClickListener {
            signupDone()
        }

        //중복확인 버튼
        binding.tvDuplicateCheck.setOnClickListener {
            userIdCheck(binding.edId.text.toString())
        }

        binding.igbtnBack.setOnClickListener {
            finish()
        }
    }
    private fun isFocus() {
        //EditText가 포커스 될때 안내문구 나오게 하기
        binding.edPw.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvWarning.visibility = View.VISIBLE
                pwTextChanged()
            } else {
                binding.tvWarning.visibility = View.GONE
            }
        }
        binding.edId.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvIdCheck.visibility = View.VISIBLE
                idTextChanged()
            } else {
                binding.tvIdCheck.visibility = View.GONE
            }
        }
    }

    private fun signupDone() {
        val name = binding.edName.text
        val email = binding.edId.text
        val password = binding.edPw.text
        val user = UserInfo(email.toString(), viewModel.getSign(password.toString()))
        if (!name.isEmpty() && isEmailCheck() && isPwCheck() && passwordCheck()) {
            MyApp.pref.addUser(email.toString(), viewModel.getSign(password.toString()))
            userInfo.add(user)
            viewModel.addLoginInfo(userInfo)
            Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
            finish()
        } else if (!name.isEmpty() && !isEmailCheck() && isPwCheck() && passwordCheck()) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show()
        } else if (!name.isEmpty() && isEmailCheck() && !isPwCheck() && passwordCheck()) {
            Toast.makeText(this, "비밀번호 형식이 올바르지 않습니다", Toast.LENGTH_SHORT).show()
        }else if (!name.isEmpty() && isEmailCheck() && isPwCheck() && !passwordCheck()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(this, "입력되지 않은 정보가 있습니다", Toast.LENGTH_SHORT).show()
        }
    }

    //email 중복 확인
    private fun userIdCheck(id: String) {
        if (MyApp.pref.containId(id)) {
            Toast.makeText(this, "중복된 아이디 입니다", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "사용 가능한 아이디 입니다", Toast.LENGTH_SHORT).show()
        }
    }



    //비밀번호 확인 입력값 변화 감지
    private fun pwCheckChanged() {
        binding.edPwCheck.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                passwordCheck()
            }
        })
    }

    //비밀번호와 비밀번호 확인 입력값이 같은지 확인
    private fun passwordCheck(): Boolean {
        if (binding.edPw.text.toString() == binding.edPwCheck.text.toString()) {
            binding.tvPwCheckWarning.setText("비밀번호가 일치합니다")
            binding.tvPwCheckWarning.setTextColor(getColorStateList(R.color.green))
            return true
        } else {
            binding.tvPwCheckWarning.text = "비밀번호가 일치하지 않습니다"
            binding.tvPwCheckWarning.setTextColor(getColorStateList(R.color.red))
            return false
        }
    }

    //비밀번호 입력값 변화 감지
    private fun pwTextChanged() {
        binding.edPw.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPwCheck()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    //이메일 입력값 변화 감지
    private fun idTextChanged() {
        binding.edId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isEmailCheck()
//                emailcheck(binding.edId.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    //비밀번호 조건 확인
    private fun isPwCheck(): Boolean {
        val regex = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,12}$"
        if (Pattern.matches(regex, binding.edPw.text)) {
            binding.tvWarning.text = "올바른 형식 입니다"
            binding.tvWarning.setTextColor(getColorStateList(R.color.green))
        } else {
            binding.tvWarning.text = "영문, 숫자, 특수문자 포함 8~12자를 입력해 주세요"
            binding.tvWarning.setTextColor(getColorStateList(R.color.red))
        }
        return Pattern.matches(regex, binding.edPw.text)
    }

    //이메일 조건 확인
    private fun isEmailCheck(): Boolean {
        val regex = "\\w+@\\w+\\.\\w+(\\.\\w+)?"
        if (Pattern.matches(regex, binding.edId.text)) {
            binding.tvIdCheck.text = "올바른 형식 입니다"
            binding.tvIdCheck.setTextColor(getColorStateList(R.color.green))
            return true
        } else {
            binding.tvIdCheck.text = "e-mail형식으로 입력해 주세요"
            binding.tvIdCheck.setTextColor(getColorStateList(R.color.red))
            return false
        }
//        return Pattern.matches(regex,binding.edId.text)
    }
    //이메일 조건 확인 2
    private fun emailcheck(email: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tvIdCheck.text = "올바른 형식 입니다"
            binding.tvIdCheck.setTextColor(getColorStateList(R.color.green))
            return true
        } else {
            binding.tvIdCheck.text = "e-mail형식으로 입력해 주세요"
            binding.tvIdCheck.setTextColor(getColorStateList(R.color.red))
            return false
        }
    }
}