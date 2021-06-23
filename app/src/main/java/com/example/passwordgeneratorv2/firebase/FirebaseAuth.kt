package com.example.passwordgeneratorv2

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuth {

    private val instance = FirebaseAuth.getInstance()
    var currentUser: FirebaseUser? = null

    fun isSomeoneLogged(): Boolean {
        if (currentUser == null) {
            currentUser = instance.currentUser
        }
        return currentUser != null
    }
}