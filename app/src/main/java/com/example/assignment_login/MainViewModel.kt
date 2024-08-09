package com.example.assignment_login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _loginInfo = MutableLiveData<List<UserInfo>>(emptyList())
    val loginInfo: LiveData<List<UserInfo>> = _loginInfo

    private val _loginCheck = MutableLiveData<Boolean>(false)
    val loginCheck: LiveData<Boolean> = _loginCheck


    fun addLoginInfo(userInfo: MutableList<UserInfo>) {
        val currentList = _loginInfo.value
        _loginInfo.value = currentList?.plus(userInfo)
    }

    fun loginCheck() {
        _loginCheck.value = _loginCheck.value != true
    }

    fun clearLoginInfo() {
        _loginInfo.value = emptyList()
    }
}