package com.example.battleofwits.ui

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.R
import com.example.battleofwits.adapters.AdapterListQuestions
import com.example.battleofwits.adapters.AdapterListThemes
import com.example.battleofwits.classes.showToast
import kotlinx.android.synthetic.main.login.*

class ListQuestions : AppCompatActivity() {

    private lateinit var adapter: AdapterListQuestions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_questions)

        val quest1: TextView = findViewById(R.id.quest1)
        val quest2: TextView = findViewById(R.id.quest2)
        val quest3: TextView = findViewById(R.id.quest3)

        setLogin(this)
//        login.setOnClickListener {
//            setLogin(this)
//        }
        getData(this)
//        passTextField.setEndIconOnClickListener {
//            getData(this)
//        }

    }

    private fun getData(context: Context) {

    }

    private fun setLogin(context: Context) {

    }

}