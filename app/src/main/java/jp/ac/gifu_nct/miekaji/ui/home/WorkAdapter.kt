package jp.ac.gifu_nct.miekaji.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.JobCategory

class WorkAdapter(private val list:List<JobCategory>,private val listener:ListListener):RecyclerView.Adapter<WorkHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkHolder {
        val rowView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_work,parent,false)
        return WorkHolder(rowView)
    }


    override fun onBindViewHolder(holder:WorkHolder, position: Int) {
        holder.workView.text = "${list[position].displayName} (x${"%.1f".format(list[position].jobWeight)})"
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,list[position])
        }
    }

    override fun getItemCount():Int{
        return list.size
    }

    interface ListListener{
        fun onClickRow(tappedView: View, listData: JobCategory)
    }
}