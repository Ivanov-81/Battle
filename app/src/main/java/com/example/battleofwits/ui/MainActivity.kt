package com.example.battleofwits.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.battleofwits.R
import com.example.battleofwits.classes.MyDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login.setOnClickListener {
            setLogin()
        }

        create.setOnClickListener {
            setCreate()
        }

    }

    override fun onBackPressed() {
        val myDialogFragment = MyDialogFragment()
        val manager = supportFragmentManager
        myDialogFragment.show(manager, "myDialog")
    }

    private fun setLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }

    private fun setCreate() {
        val intent = Intent(this, Auth::class.java)
        startActivity(intent)
    }
}