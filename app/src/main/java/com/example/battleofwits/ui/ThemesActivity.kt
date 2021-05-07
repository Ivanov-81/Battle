package com.example.battleofwits.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.adapters.AdapterListThemes
import com.example.battleofwits.classes.showToast
import com.example.battleofwits.data.CreateNewGame
import com.example.battleofwits.data.DataTheme
import com.example.battleofwits.data.Themes
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.themes_activity.*


class ThemesActivity : AppCompatActivity() {

    private lateinit var repository: ServerApiService
    private lateinit var adapter: AdapterListThemes
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.themes_activity)
        context = this
        repository = NetworkFactory.getData(context)

        val callback: AdapterListThemes.AdapterCallback = object : AdapterListThemes.AdapterCallback {
            override fun onAdapterSelected(pos: Int) {
                val intent = Intent(applicationContext, GameActivity::class.java)
                intent.putExtra("id", pos)
                startActivityForResult(intent, 100)
            }
        }

        themeSelection(this, callback)

    }

    private fun themeSelection(context: Context, callback: AdapterListThemes.AdapterCallback) {

        repository.newGame(0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<CreateNewGame> {

                override fun onSubscribe(d: Disposable) {
                    // Log.d(TAG_APP, "onSubscribe ${d.isDisposed}")
                }

                override fun onNext(t: CreateNewGame) {
                    if (t.result) {
                        fillListThemes(t.data, context, callback)
                    }
                    else {
                        showToast(context, "${t.error}", Toast.LENGTH_LONG)
                    }
                }

                override fun onError(e: Throwable) {
                    e.message?.let {
                        Log.e(TAG_APP, it)
                    }
                }

                override fun onComplete() {
//                    Log.d(TAG_APP, "onComplete")
                }

            })
    }

    fun fillListThemes(data: DataTheme, context: Context, callback: AdapterListThemes.AdapterCallback) {
        val themes: List<Themes> = data.theme
        RecyclerView.hasFixedSize()
        RecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AdapterListThemes(themes as ArrayList<Themes>, callback, context)
        RecyclerView.adapter = adapter
    }

}