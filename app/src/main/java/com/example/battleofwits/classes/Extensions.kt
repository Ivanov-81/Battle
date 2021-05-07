package com.example.battleofwits.classes

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

fun showToast(context: Context, message: String, timer: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, timer).show()
}