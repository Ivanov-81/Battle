package com.example.battleofwits.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.battleofwits.PREFERENCES
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.adapters.AdapterListThemes
import com.example.battleofwits.classes.showToast
import com.example.battleofwits.data.Games
import com.example.battleofwits.data.SendAnswerResult
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class GameActivity : Activity() {

    private lateinit var repository: ServerApiService
    private var count: Timer? = null
    private var round: Int = 0
    private var timer: Int = 0
    private val gson = Gson()
    private var anim: ObjectAnimator? = null
    private var set_timer: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        repository = NetworkFactory.getData(this)

        val timeout: TextView = findViewById(R.id.timeout)

        val mSettings: SharedPreferences =
            this.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val questions = mSettings.getString("questions", "")

        val data: Games = gson.fromJson(questions, Games::class.java)

        var question = ""
        var question1 = ""
        var question2 = ""
        var question3 = ""
        var question4 = ""
        if (data.rounds?.isNotEmpty()!!) {
            round = data.rounds[0].round_num
            timer = data.rounds[0].questions[0].timer
            question = data.rounds[0].questions[0].question

            if (data.rounds[0].questions.isNotEmpty()) {
                val answers = data.rounds[0].questions[0].answers
                question1 = answers[0].answer
                question2 = answers[1].answer
                question3 = answers[2].answer
                question4 = answers[3].answer
            }
        }

        val themeView: TextView = findViewById(R.id.theme)
        themeView.text = data.theme?.name

        val questionView: TextView = findViewById(R.id.question)
        questionView.text = question

        val num1: TextView = findViewById(R.id.num1)
        num1.text = question1

        val num2: TextView = findViewById(R.id.num2)
        num2.text = question2

        val num3: TextView = findViewById(R.id.num3)
        num3.text = question3

        val num4: TextView = findViewById(R.id.num4)
        num4.text = question4

        setListenersButtons(num1, num2, num3, num4, timeout, data)

        startTimer(this, num1, num2, num3, num4, timeout)
    }

    private fun setListenersButtons(
        num1: TextView,
        num2: TextView,
        num3: TextView,
        num4: TextView,
        timeout: TextView,
        data: Games
    ) {

        num1.setOnClickListener{
            showToast(this, "Click 1")
            responseProcessing(timeout, num1, num2, num3, num4, data, 1)
        }

        num2.setOnClickListener{
            showToast(this, "Click 2")
            responseProcessing(timeout, num1, num2, num3, num4, data, 2)
        }

        num3.setOnClickListener{
            showToast(this, "Click 3")
            responseProcessing(timeout, num1, num2, num3, num4, data, 3)
        }

        num4.setOnClickListener{
            showToast(this, "Click 4")
            responseProcessing(timeout, num1, num2, num3, num4, data, 4)
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun responseProcessing(
        timeout: TextView,
        num1: TextView,
        num2: TextView,
        num3: TextView,
        num4: TextView,
        data: Games,
        i: Int
    ) {
        println(data)
        set_timer = false
        num1.isClickable = false
        num2.isClickable = false
        num3.isClickable = false
        num4.isClickable = false
        anim?.cancel()
        if (data.rounds?.isNotEmpty()!!) {
            if (data.rounds[0].questions.isNotEmpty()) {
                when (i) {
                    1 -> if(data.rounds[0].questions[0].answers[0].correct) done(timeout) else wrong(timeout)
                    2 -> if(data.rounds[0].questions[0].answers[1].correct) done(timeout) else wrong(timeout)
                    3 -> if(data.rounds[0].questions[0].answers[2].correct) done(timeout) else wrong(timeout)
                    4 -> if(data.rounds[0].questions[0].answers[3].correct) done(timeout) else wrong(timeout)
                }
            }
        }

        onSendAnswer(data.id, i, 0)

    }

    private fun onSendAnswer(id: Int, answer: Int, timer_left: Int) {

        repository.sendAnswer(QuestionActivity.Object.create(id, answer, timer_left))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SendAnswerResult> {

                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {}

                override fun onNext(t: SendAnswerResult) {
                    val text: String
                    val duration: Any

                    if (t.result) {
                        text = "Ответ получен!"
                        duration = Toast.LENGTH_SHORT
                    }
                    else {
                        text = "${t.error}"
                        duration = Toast.LENGTH_LONG
                    }

                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                }

            })

    }

    @SuppressLint("ResourceAsColor")
    private fun wrong(timeout: TextView) {
        timeout.text = "Вы ошиблись"
        timeout.setTextColor(R.color.backgroundEntryButtonEnd)
        setTimer()
    }

    @SuppressLint("ResourceAsColor")
    private fun done(timeout: TextView) {
        timeout.text = "Правильный ответ"
        timeout.setTextColor(R.color.backgroundEntryButtonEnd)
        setTimer()
    }

    private fun setTimer() {
        val tm = Timer()
        tm.schedule(object : TimerTask() {
            override fun run() {
                val intent = Intent(applicationContext, ListQuestions::class.java)
                startActivity(intent)
                tm.cancel()
            }
        }, 5000,1000)
    }


    private fun startTimer(
        context: Context,
        num1: TextView,
        num2: TextView,
        num3: TextView,
        num4: TextView,
        timeout: TextView
    ) {

        val progress: ProgressBar = findViewById(R.id.progress_screensaver)
        val hundred = timer * 1000
        progress.max = 100
        progress.progress = 100

        anim = ObjectAnimator.ofInt(progress, "progress", 0).apply {
            duration = hundred.toLong()
            addListener(
                object : Animator.AnimatorListener {
                    @SuppressLint("ResourceAsColor")
                    override fun onAnimationEnd(animation: Animator?) {
                        println("End timer")

                        timeout.text = "Время вышло!"
                        timeout.setTextColor(R.color.colorRed)

                        num1.isClickable = false
                        num2.isClickable = false
                        num3.isClickable = false
                        num4.isClickable = false

                        if(set_timer) {
                            count = Timer()
                            count?.schedule(object : TimerTask() {
                                override fun run() {
                                    println("Конец анимации")
                                    val intent = Intent(context, SecondActivity::class.java)
                                    startActivity(intent)
                                    count!!.cancel()
                                }
                            }, 3000,1000)
                        }

                    }
                    override fun onAnimationStart(animation: Animator?) {
                        val cnt = Timer()
                        cnt.schedule(object : TimerTask() {
                            override fun run() {
                                println("Конец таймера")
                                cnt.cancel()
                            }
                        }, hundred.toLong(),1000)
                    }
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                },
            )
            start()
        }

//        Thread(Runnable {
//            println(hundred)
//            while (hundred <= 0) {
//                println(hundred)
//                hundred -= number
//
//                try {
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//                println(secondaryHandler)
//                secondaryHandler?.post {
//                    println(hundred.toInt())
//                    progress.progress = hundred.toInt()
//
//                    if (hundred <= 0) {
//                        progress.progress = 0
//                    }
//                }
//            }
//        }).start()
    }

}
