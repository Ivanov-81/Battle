package com.example.battleofwits.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.PREFERENCES
import com.example.battleofwits.R
import com.example.battleofwits.SECRET_KEY
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
import kotlinx.android.synthetic.main.auth.*

class Auth : AppCompatActivity()  {

    private var mSettings: SharedPreferences? = null
    private lateinit var context: Context

    interface ObjectFactory {
        fun create(name: String, email: String, password: String, device_os: String, device_token: String): Obj
    }

    class Obj private constructor(
        val name: String,
        val email: String,
        val password: String,
        val device_os: String,
        val device_token: String
    ) {
        companion object : ObjectFactory {
            override fun create(name: String, email: String, password: String, device_os: String, device_token: String): Obj {
                return Obj(name, email, password, device_os, device_token)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth)
        context = this

        mSettings = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        auth.setOnClickListener {
            setAuth(context)
        }

        passTextField1.setEndIconOnClickListener {
            showToast(this, "Показываем пароль")
        }

        passTextField2.setEndIconOnClickListener {
            showToast(this, "Показываем пароль")
        }

    }


    private fun setAuth(context: Context) {

        val strName = nameTextField.editText?.text.toString()
        val strEmail = emailTextField1.editText?.text.toString()
        val strPass1 = passTextField1.editText?.text.toString()
        val strPass2 = passTextField2.editText?.text.toString()

        if(strName == "") {
            showToast(this, "Заполните поле имя", Toast.LENGTH_LONG)
            return
        }

        if(strEmail == "") {
            showToast(this, "Заполните поле логин", Toast.LENGTH_LONG)
            return
        }

        if(strPass1 == "") {
            showToast(this, "Заполните поле пароль", Toast.LENGTH_LONG)
            return
        }

        if(strPass2 == "") {
            showToast(this, "Заполните поле пароль", Toast.LENGTH_LONG)
            return
        }

        if(strPass1 != strPass2) {
            showToast(this, "Пароли не совпадают!", Toast.LENGTH_LONG)
            return
        }

        auth(strName, strEmail, strPass1, "android", getDeviceToken(), context)

    }

    private fun getDeviceToken(): String {
//        var token = ""
//        try {
//
//            val instanceID: InstanceID = InstanceID.getInstance(this)
//            token = instanceID.getToken(
//                getString(R.string.gcm_defaultSenderId),
//                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null
//            )
//
//            Log.i("****GCM Token****", token)
//        } catch (e: Exception) {
//            Log.d("****GCM Token****", "Failed to complete token refresh", e)
//        }
        return SECRET_KEY
    }

    private fun auth(
        name: String,
        email: String,
        password: String,
        os: String,
        deviceToken: String,
        context: Context
    ) {

        val repository: ServerApiService = NetworkFactory.getData(context)
        val obj = Obj.create(name, email, password, os, deviceToken)

        repository.register(obj)
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

                        val editor: SharedPreferences.Editor? = mSettings?.edit()
                        if (editor != null) {
                            editor.putString("name", obj.name)
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