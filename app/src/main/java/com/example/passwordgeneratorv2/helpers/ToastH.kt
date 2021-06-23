package com.example.passwordgeneratorv2.helpers

import android.app.Activity
import android.widget.Toast


//AppCompatActivity can be replaced by Activity
class ToastH(var activity: Activity) {
    private var toast: Toast

    init {
        toast = Toast(activity.applicationContext)
    }

    fun showToast(message: String) {
        if (message.isNotEmpty()) {
            toast.cancel()
            toast = Toast.makeText(
                activity.applicationContext,
                message,
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

}