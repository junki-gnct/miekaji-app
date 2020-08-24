package jp.ac.gifu_nct.miekaji.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class ViewAdapter(private val list:List<ListData>,private val listener:ListListener):RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("Adapter","onCreateViewHolder")
        val rowView:View=LayoutInflater.from(parent.context).inflate(R.layout.recycle_item,parent,false)
        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder")
        holder.nameView.text=list[position].name
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,list[position])
        }
    }

    override fun getItemCount():Int{
        Log.d("Adapter","getItemCount")
        return list.size
    }

    interface ListListener{
        fun onClickRow(tappedView: View, listData:ListData)
    }
}