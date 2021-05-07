package com.example.battleofwits.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.PREFERENCES
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.classes.showToast
import com.example.battleofwits.data.Games
import com.example.battleofwits.data.SendLoginDataResult
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.login.*

class Login : AppCompatActivity() {

    private var mSettings: SharedPreferences? = null

    interface ObjectFactory {
        fun create(email: String, password: String, device_os: String, device_token: String): Obj
    }

    class Obj private constructor(
        val email: String,
        val password: String,
        val device_os: String,
        val device_token: String,
    ) {
        companion object : ObjectFactory {
            override fun create(email: String, password: String, device_os: String, device_token: String): Obj {
                return Obj(email, password, device_os, device_token)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        getData(this)

        login.setOnClickListener {
            setLogin(this)
        }

        passTextField.setEndIconOnClickListener {
            showToast(this, "Показываем пароль")
        }

    }

    private fun getData(context: Context) {

        val mSettings: SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val email: String = mSettings.getString("email", "").toString()
        val pass: String = mSettings.getString("password", "").toString()

        if(email != "" && pass != "") {
            val em = emailTextField.editText
            if (em != null) {
                em.hint = email
                em.setText(email)
            }

            val pas = passTextField.editText
            if (pas != null) {
                pas.hint = pass
                pas.setText(pass)
            }

            showToast(this, "Авторизуемся!")

            var counter: Int = 0;

            object : CountDownTimer(1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    counter++
                }
                override fun onFinish() {
                    setLogin(context)
                }
            }.start()

        }
    }

    private fun setLogin(context: Context) {

        val strEmail = emailTextField.editText?.text.toString()
        val strPass = passTextField.editText?.text.toString()

        if(strEmail == "") {
            showToast(this, "Заполните поле логин", Toast.LENGTH_LONG)
            return
        }

        if(strPass == "") {
            showToast(this, "Заполните поле пароль", Toast.LENGTH_LONG)
            return
        }

        entry(strEmail, strPass, "android", context, getDeviceToken())

    }

    private fun getDeviceToken(): String {
        return ""
    }

    private fun entry(
        email: String,
        password: String,
        os: String,
        context: Context,
        device: String
    ) {

        mSettings = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        val repository: ServerApiService = NetworkFactory.getData(context)
        val obj = Obj.create(email, password, os, device)

        repository.login(obj)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<SendLoginDataResult> {

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

                override fun onNext(t: SendLoginDataResult) {
                    if(t.result) {
                        showToast(context, "Данные получены!")
                        val editor: SharedPreferences.Editor? = mSettings?.edit()
                        if (editor != null) {
                            editor.putString("email", obj.email)
                            editor.putString("password", obj.password)
                            editor.putString("device_os", obj.device_os)
                            editor.putString("device_token", t.data)
                            val games: Any? = mSettings?.getString("completed_games", null)
                            if (games == null) {
                                editor.putString("completed_games", Gson().toJson(emptyList<Games>()))
                            }
                            editor.apply()
                        }

                        val intent = Intent(context, SecondActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        showToast(context, "${t.error}", Toast.LENGTH_LONG)
                    }
                }

            })

    }

}