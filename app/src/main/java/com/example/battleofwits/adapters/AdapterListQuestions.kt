package com.example.battleofwits.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.battleofwits.R
import com.example.battleofwits.TAG_APP
import com.example.battleofwits.classes.showToast
import com.example.battleofwits.data.Themes

class AdapterListQuestions(
    listArray: ArrayList<Themes>,
    callback: AdapterCallback,
    context: Context
): RecyclerView.Adapter<AdapterListQuestions.ViewHolderQuest>()  {

    private var listArrayR = listArray
    var contextR = context
    var callbackR = callback

    interface AdapterCallback {
        fun onAdapterSelected(pos: Int)
    }

    inner class ViewHolderQuest(view: View): RecyclerView.ViewHolder(view) {

        val text: TextView = view.findViewById(R.id.textTheme)

        fun bind(listItem: Themes) {
            Log.d(TAG_APP, listItem.name)
            itemView.setOnClickListener {
                showToast(contextR, "Pressed: ${listItem.id}")
                callbackR.onAdapterSelected(listItem.id)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderQuest {
        val inflater = LayoutInflater.from(contextR)
        return ViewHolderQuest(inflater.inflate(R.layout.item_question, parent, false))
    }

    override fun getItemCount(): Int {
        return listArrayR.size
    }

    override fun onBindViewHolder(holder: ViewHolderQuest, position: Int) {
        val listItem = listArrayR[position]
        holder.bind(listItem)
    }

    override fun onBindViewHolder(holder: AdapterListThemes.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}