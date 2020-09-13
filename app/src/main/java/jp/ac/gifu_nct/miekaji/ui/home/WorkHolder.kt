package jp.ac.gifu_nct.miekaji.ui.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class WorkHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val workView: TextView =itemView.findViewById(R.id.work_name)
}