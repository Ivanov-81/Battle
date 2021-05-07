package com.example.battleofwits.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.R

class Screensaver : AppCompatActivity()  {

//    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
//        println("Обработайте отпускание клавиши, верните true, если обработка выполнена")
//        return false;
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screensaver)
        val progressBar = findViewById<View>(R.id.progress_screensaver) as ProgressBar
        progressBar.visibility = ProgressBar.VISIBLE;

        startTimeCounter()

    }

    private fun startTimeCounter() {

        var counter: Int = 0;

        object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
            }
            override fun onFinish() {
                nextActivity()
            }
        }.start()
    }

    private fun nextActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}