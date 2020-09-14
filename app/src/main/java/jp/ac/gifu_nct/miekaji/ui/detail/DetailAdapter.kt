package jp.ac.gifu_nct.miekaji.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class DetailAdapter(private val list:List<DetailData>,private val listener:ListListener):
    RecyclerView.Adapter<DetailHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHolder {
        Log.d("Adapter","onCreateViewHolder")
        val rowView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_do,parent,false)
        return DetailHolder(rowView)
    }

    override fun onBindViewHolder(holder:DetailHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder")
        holder.doView.text=list[position].dowork
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,list[position])
        }
    }

    override fun getItemCount():Int{
        Log.d("Adapter","getItemCount")
        return list.size
    }

    interface ListListener{
        fun onClickRow(tappedView: View, listData:DetailData)
    }
}