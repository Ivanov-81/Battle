package com.example.battleofwits.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.battleofwits.R
import com.example.battleofwits.data.Games

fun formatLongToStrTime(time: Long): String {
    val mod = time % 3600000
    var hours: Long = 0
    var mint: Long = 0
    mint = if (mod != 0L) mod / 60000 else 0
    hours = time / 3600000
    var hoursStr = hours.toString()
    var mintsStr = mint.toString()
    if (hoursStr.length == 1) hoursStr = "0$hoursStr"
    if (mintsStr.length == 1) mintsStr = "0$mintsStr"
    return "$hoursStr:$mintsStr"
}

class ExpandableListAdapterSecondAct internal constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, List<Any>>
) : BaseExpandableListAdapter() {

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertViewC: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertViewC

        val expandedListText = getChild(listPosition, expandedListPosition) as Games
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item_sub, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(R.id.listView)
        val expandedListTextView3 = convertView.findViewById<TextView>(R.id.listView1_3)
        val expandedListStart = convertView.findViewById<TextView>(R.id.start)
        val expandedListFinish = convertView.findViewById<TextView>(R.id.finish)
        val expandedListRound = convertView.findViewById<TextView>(R.id.round)


        var theme = expandedListText.theme?.name
        val start = formatLongToStrTime(expandedListText.start)
        var finish = "не закончена"
        var round = ""


        if(expandedListText.theme?.name == null) {
            theme = ""
        }

        if(expandedListText.rounds != null) {
            round = expandedListText.rounds[0].round_num.toString()
        }

        if(expandedListText.end != null) {
            finish = formatLongToStrTime(expandedListText.end)
        }

        expandedListTextView.text = expandedListText.gamers[0].name
        expandedListTextView3.text = theme
        expandedListStart.text = "Старт: $start"
        expandedListFinish.text = "Финиш: $finish"
        expandedListRound.text = "Раунд: $round"
        return convertView
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertViewG: View?,
        parent: ViewGroup
    ): View {

        var convertView = convertViewG
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listView)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle + " (" + dataList[listTitle]?.size +")"
        return convertView
    }

}