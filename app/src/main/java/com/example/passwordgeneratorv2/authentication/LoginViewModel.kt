package com.example.passwordgeneratorv2.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.passwordgeneratorv2.R
import androidx.lifecycle.MutableLiveData
import com.example.passwordgeneratorv2.firebase.FirebaseAuth

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val toastMessage = MutableLiveData<String?>()
    fun getToastMessage(): MutableLiveData<String?> = toastMessage
    private val loginSuccess = MutableLiveData<Boolean>()
    fun wasLoginSuccessful(): LiveData<Boolean> = loginSuccess
    private val auth = FirebaseAuth()

    fun login(email: String, password: String) {
        toastMessage.value = when {
            email.isEmpty() && password.isEmpty() -> getApplication<Application>().getString(R.string.edt_text_all_empty)
            email.isEmpty() && password.isNotEmpty() -> getApplication<Application>().getString(R.string.edt_text_email_empty)
            email.isNotEmpty() && password.isEmpty() -> getApplication<Application>().getString(R.string.edt_text_password_empty)
            else -> null
        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.loginWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) loginSuccess.value = true
                else {
                    toastMessage.value = it.exception?.message
                }
            }
        }
    }

}
