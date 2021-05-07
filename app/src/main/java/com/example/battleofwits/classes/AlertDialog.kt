package com.example.battleofwits.classes

import androidx.fragment.app.DialogFragment
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.battleofwits.R

class MyDialogFragment: DialogFragment() {

    private val duration: Int = Toast.LENGTH_LONG

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Важное сообщение!")
                .setMessage("Вы уверены, что хотите выйти из программы?")
                .setCancelable(true)
                .setIcon(R.drawable.hungrycat)
                .setPositiveButton("Покинуть") { dialog, id ->
                    showToast(activity!!, "Вы покинули приложение!", duration)
                }
                .setNegativeButton("Не покидать",
                    DialogInterface.OnClickListener { dialog, id ->
                        showToast(activity!!, "Вы продолжили игру!", duration)
                    }
                )

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}