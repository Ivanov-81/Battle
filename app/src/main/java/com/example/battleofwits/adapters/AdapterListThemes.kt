package com.example.battleofwits.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.data.SendObjectThemeResult
import com.example.battleofwits.data.Themes
import com.example.battleofwits.network.NetworkFactory
import com.example.battleofwits.network.ServerApiService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class AdapterListThemes(
    listArray: ArrayList<Themes>,
    callback: AdapterCallback,
    context: Context
): RecyclerView.Adapter<AdapterListThemes.ViewHolder>() {

    private var listArrayR = listArray
    var contextR = context
    var callbackR = callback

    interface AdapterCallback {
        fun onAdapterSelected(pos: Int)
    }

    interface PersonFactory {
        fun create(theme: Array<Int>, contest: Boolean): Obj
    }

    class Obj private constructor(val theme: Array<Int>, val contest: Boolean) {
        companion object : PersonFactory {
            override fun create(theme: Array<Int>, contest: Boolean): Obj {
                return Obj(theme, contest)
            }
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val text: TextView = view.findViewById(R.id.textTheme)
        val image: ImageView = view.findViewById(R.id.imageTheme)

        fun bind(listItem: Themes) {
            Log.d(TAG_APP, listItem.name)
            text.text = listItem.name
//            image.setImageResource(listItem.id)
            itemView.setOnClickListener {
                Toast.makeText(contextR, "Pressed: ${listItem.id}", Toast.LENGTH_LONG).show()
                getDataNewGame(listItem, contextR)
            }
        }

        private fun getDataNewGame(data: Themes, context: Context) {

            val repository: ServerApiService = NetworkFactory.getData(context)
            val arr = arrayOf(data.id)
            val obj = Obj.create(arr, false)
            Log.e(TAG_APP, "$obj")
            repository.newGameOnTheme(obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<SendObjectThemeResult> {

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

                    override fun onNext(t: SendObjectThemeResult) {
                        val id: Int = t.data.id
                        callbackR.onAdapterSelected(id)
                    }

                })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(contextR)
        return ViewHolder(inflater.inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return listArrayR.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = listArrayR[position]
        holder.bind(listItem)
    }

//    fun updateAdapter(listArray: ArrayList<Themes>) {
//        listArrayR.clear()
//        listArrayR.addAll(listArray)
//        notifyDataSetChanged()
//    }

}