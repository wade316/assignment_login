package com.example.assignment_login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
        binding.btnDone.setOnClickListener {
            val name = binding.edName.text
            val email = binding.edId.text
            val password = binding.edPw.text
            val user = UserInfo(email.toString(), password.toString())
            if (!name.isEmpty() && isEmailCheck() && isPwCheck() && passwordCheck()) {
                getSign(password.toString()).let {
                    Log.d("sdc", "getSign: $it")
                    MyApp.pref.setString(email.toString(), it)
                    userInfo.add(user)
                    viewModel.addLoginInfo(userInfo)

                    for (i in viewModel.loginInfo.value!!) {
                        Log.d("sdc", "loginInfo: ${i.email} ${i.password}")
                    }
                    for (u in userInfo) {
                        Log.d("sdc", "userInfo: ${u.email} ${u.password}")
                    }
                    finish()
                }
            }
        }
        pwCheckChanged()


    }

    fun getSign(password: String): String {
        val hash: ByteArray
        try {
            val mdigest = MessageDigest.getInstance("SHA-256")
            mdigest.update(password.toByteArray())
            hash = mdigest.digest()
        } catch (e: CloneNotSupportedException) {
            throw DigestException("couldn't make digest of patial content")
        }
        //% = 문자열 시작, 0 = 자릿수가 부족하면 0으로 채우고. 2 = 자릿수 묶음
        //x = 16진수로 출력, 대문자'X'일 경우 대문자로 출력
        return hash.joinToString("") { "%02X".format(it) }
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
}