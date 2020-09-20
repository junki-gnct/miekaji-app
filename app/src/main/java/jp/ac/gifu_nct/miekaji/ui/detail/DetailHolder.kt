package jp.ac.gifu_nct.miekaji.ui.detail

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class DetailHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val doView: TextView = itemView.findViewById(R.id.do_name)
    val valueView = itemView.findViewById<TextView>(R.id.do_time)
}