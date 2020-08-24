package jp.ac.gifu_nct.miekaji.ui.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val nameView: TextView =itemView.findViewById(R.id.name)
}