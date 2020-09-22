package jp.ac.gifu_nct.miekaji.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.JobCategoryandValue

class DetailAdapter(private val list:List<JobCategoryandValue>,private val listener:ListListener):
    RecyclerView.Adapter<DetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        val rowView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_do,parent,false)
        return DetailHolder(rowView)
    }

    override fun onBindViewHolder(holder:DetailHolder, position: Int) {
        holder.doView.text = list[position].categoryValue.displayName
        holder.valueView.text = "%.1f 分 - %.1f".format(list[position].jobValue.second, list[position].jobValue.first)
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,list[position])
        }
    }

    override fun getItemCount():Int{
        return list.size
    }

    interface ListListener{
        fun onClickRow(tappedView: View, listData: JobCategoryandValue)
    }
}