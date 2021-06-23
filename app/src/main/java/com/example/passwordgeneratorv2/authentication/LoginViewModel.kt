package com.example.passwordgeneratorv2.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.passwordgeneratorv2.firebase.FirebaseAuth

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {
    private val toastMessage = MutableLiveData<String>()
    fun getToastMessage(): LiveData<String> = toastMessage
    val auth = FirebaseAuth()

    fun loginWithEmailAndPassword(email: String, password: String) {

    }
}