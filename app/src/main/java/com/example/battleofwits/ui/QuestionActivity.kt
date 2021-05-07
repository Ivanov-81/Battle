package com.example.battleofwits.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.classes.MyDialogFragment
import com.example.battleofwits.data.GetQuestion
import com.example.battleofwits.data.SendAnswerResult
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.question_activity.*

class QuestionActivity : AppCompatActivity() {

    private lateinit var repository: ServerApiService
    private var mInfoTextView: Int = 0

    interface AnswerFactory {
        fun create(game: Int, answer: Int, timer_left: Int): Object
    }

    class Object private constructor(val game: Int, val answer: Int, val timer_left: Int) {
        companion object : AnswerFactory {
            override fun create(game: Int, answer: Int, timer_left: Int): Object {
                return Object(game, answer, timer_left)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity)
        repository = NetworkFactory.getData(this)
        val arguments = intent.extras
        onEntry(arguments!!["id"] as Int)

        send_answer.setOnClickListener {
            sendData(arguments["id"] as Int)
        }

        val redRadioButton =
            findViewById<View>(R.id.option0) as RadioButton
        redRadioButton.setOnClickListener(radioButtonClickListener)

        val greenRadioButton =
            findViewById<View>(R.id.option1) as RadioButton
        greenRadioButton.setOnClickListener(radioButtonClickListener)

        val blueRadioButton =
            findViewById<View>(R.id.option2) as RadioButton
        blueRadioButton.setOnClickListener(radioButtonClickListener)

        val grayRadioButton =
            findViewById<View>(R.id.option3) as RadioButton
        grayRadioButton.setOnClickListener(radioButtonClickListener)

    }

    private var radioButtonClickListener =
        View.OnClickListener { v ->
            val rb = v as RadioButton
            when (rb.id) {
                R.id.option0 -> mInfoTextView = 1
                R.id.option1 -> mInfoTextView = 2
                R.id.option2 -> mInfoTextView = 3
                R.id.option3 -> mInfoTextView = 4
                else -> {
                }
            }
        }

    override fun onBackPressed() {
        val myDialogFragment = MyDialogFragment()
        val manager = supportFragmentManager
        myDialogFragment.show(manager, "myDialog")
    }

    private fun sendData(id: Int) {

        var status = true
        var answer = 0

        if(mInfoTextView == 0) {
            status = false
        }
        else {
            answer = mInfoTextView
        }

        if(status) {
            onSendAnswer(id, answer, 0)
        }
        else {
            val toast = Toast.makeText(applicationContext, "Выберите ответ!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
        }

    }

    private fun onEntry(id: Int) {

        repository.getQuestion(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<GetQuestion> {

                override fun onSubscribe(d: Disposable) {
                    // Log.d(TAG_APP, "onSubscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    e.message?.let {
                        Log.e(TAG_APP, it)
                    }
                }

                override fun onComplete() {
//                    Log.d(TAG_APP, "onComplete")
                }

                override fun onNext(t: GetQuestion) {
                    Log.e(TAG_APP, "$t")
                    if(t.result) {
                        val question: TextView = findViewById(R.id.question)
                        question.text = t.data[0].questions[0].question

                        val option0: RadioButton = findViewById(R.id.option0)
                        option0.text = t.data[0].questions[0].answers[0].answer

                        val option1: RadioButton = findViewById(R.id.option1)
                        option1.text = t.data[0].questions[0].answers[1].answer

                        val option2: RadioButton = findViewById(R.id.option2)
                        option2.text = t.data[0].questions[0].answers[2].answer

                        val option3: RadioButton = findViewById(R.id.option3)
                        option3.text = t.data[0].questions[0].answers[3].answer
                    }
                }

            })

    }

    private fun onSendAnswer(id: Int, answer: Int, timer_left: Int) {

        repository.sendAnswer(Object.create(id, answer, timer_left))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SendAnswerResult> {

                override fun onSubscribe(d: Disposable) {
                    // Log.d(TAG_APP, "onSubscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    e.message?.let {
                        Log.e(TAG_APP, it)
                    }
                }

                override fun onComplete() {
//                    Log.d(TAG_APP, "onComplete")
                }

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

}