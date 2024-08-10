package com.example.assignment_login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.security.DigestException
import java.security.MessageDigest

class MainViewModel: ViewModel() {
    private val _loginInfo = MutableLiveData<List<UserInfo>>(emptyList())
    val loginInfo: LiveData<List<UserInfo>> = _loginInfo

    private val _loginCheck = MutableLiveData<Boolean>(false)
    val loginCheck: LiveData<Boolean> = _loginCheck


    fun addLoginInfo(userInfo: List<UserInfo>) {
        val currentList = _loginInfo.value ?: emptyList()
        _loginInfo.value = currentList + userInfo
    }

    fun isLoginCheck() {
        _loginCheck.value = _loginCheck.value != true
    }

    fun clearLoginInfo() {
        _loginInfo.value = emptyList()
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
}