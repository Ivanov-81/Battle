package com.example.battleofwits.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.battleofwits.PREFERENCES
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.adapters.ExpandableListAdapterSecondAct
import com.example.battleofwits.classes.MyDialogFragment
import com.example.battleofwits.classes.showToast
import com.example.battleofwits.data.Data
import com.example.battleofwits.data.Games
import com.example.battleofwits.data.ListData
import com.example.battleofwits.data.ServerResponse
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.second_activity.*
import java.util.HashMap

object ExpandableListData {
    val data: HashMap<String, List<Any>>
        get() {
            val expandableListDetail = HashMap<String, List<Any>>()
            val myFavCricketPlayers: MutableList<String> = java.util.ArrayList()
            val myFavFootballPlayers: MutableList<String> = java.util.ArrayList()
            val myFavTennisPlayers: MutableList<String> = java.util.ArrayList()
            expandableListDetail["ВАШ ХОД"] = myFavCricketPlayers
            expandableListDetail["ЖДЁМ СОПЕРНИКА"] = myFavFootballPlayers
            expandableListDetail["ЗАВЕРШЁННЫЕ ИГРЫ"] = myFavTennisPlayers
            return expandableListDetail
        }
}

class SecondActivity : AppCompatActivity() {

    private lateinit var repository: ServerApiService
    private var mSettings: SharedPreferences? = null

    private var expandableListView: ExpandableListView? = null
    private var adapter: ExpandableListAdapter? = null
    private var titleList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        mSettings = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        repository = NetworkFactory.getData(this)

        onEntry(this)

        marathon.setOnClickListener {
            further(this, false)
        }

        duel.setOnClickListener {
            further(this, true)
        }

        back.setOnClickListener {
            setBack()
        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun fillSelect(data: Data, context: Context) {

        expandableListView = findViewById(R.id.expendableList)

        if (expandableListView != null) {

            val dt: HashMap<String, List<Any>> = ExpandableListData.data

            if(data.games.isNotEmpty()) {

                var arr1 = emptyArray<Any>()
                var arr2 = emptyArray<Any>()
                var arr3 = emptyList<Games>()

                val games = mSettings?.getString("completed_games", null)

                val d: List<Games> = Gson().fromJson(games, Array<Games>::class.java).toList()

                if (games != null) {
                    arr3 = d
                }

                for ((index, value) in data.games.withIndex()) {
                    if(value.end == null) {
                        if(value.multi) {
                            arr2 += value
                        }
                        else {
                            arr1 += value
                        }
                    }
                }

                dt["ВАШ ХОД"] = arr1.toList()
                dt["ЖДЁМ СОПЕРНИКА"] = arr2.toList()
                dt["ЗАВЕРШЁННЫЕ ИГРЫ"] = arr3.toList()
            }
            else {
                showToast(context, "Список игр пуст!")
            }

            titleList = ArrayList(dt.keys)
            adapter = ExpandableListAdapterSecondAct(this, titleList as ArrayList<String>, dt)
            expandableListView!!.setAdapter(adapter)
            expandableListView!!.setOnGroupExpandListener {groupPosition ->
//                showToast(
//                    applicationContext,
//                    (titleList as ArrayList<String>)[groupPosition] + " List Expanded." + groupPosition
//                )
            }
            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
//                showToast(
//                    applicationContext,
//                    (titleList as ArrayList<String>)[groupPosition] + " List Collapsed." + groupPosition
//                )
            }
            expandableListView!!.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->

                val questions = dt[(titleList as ArrayList<String>)[groupPosition]]!![childPosition]

                val editor: SharedPreferences.Editor? = mSettings?.edit()

                if (editor != null) {
                    editor.putString("questions", Gson().toJson(questions))
                    editor.apply()
                }

                val intent = Intent(applicationContext, ListQuestions::class.java)
                startActivityForResult(intent, 100)

                false
            }
        }
    }

    override fun onBackPressed() {
        val myDialogFragment = MyDialogFragment()
        val manager = supportFragmentManager
        myDialogFragment.show(manager, "myDialog")
    }
    private fun further(context: Context, boolean: Boolean) {
        val intent = Intent(context, ThemesActivity::class.java)
        intent.putExtra("contest", boolean.toString())
        startActivity(intent)
    }
    private fun setBack() {
        Log.d("Назад", "")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun onEntry(context: Context) {

        repository.search()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ServerResponse> {

                override fun onSubscribe(d: Disposable) {
                    // Log.d(TAG_APP, "onSubscribe ${d.isDisposed}")
                }

                override fun onNext(t: ServerResponse) {

                    val text: String
                    val duration: Any

                    if (t.result) {
                        fillData(t.data, context)
                    } else {
                        text = "${t.error}"
                        duration = Toast.LENGTH_LONG
                        showToast(context, text, duration)
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
    private fun fillData(data: Data, context: Context) {

        val image = findViewById<ImageView>(R.id.profile_image)
        image.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val qtyThemes = findViewById<TextView>(R.id.qty_themes)
        qtyThemes.text = data.themes.size.toString()

        fillSelect(data, context)

    }

}