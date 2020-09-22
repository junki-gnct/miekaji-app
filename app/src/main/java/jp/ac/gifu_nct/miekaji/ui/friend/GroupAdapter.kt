package jp.ac.gifu_nct.miekaji.ui.friend

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.User
import jp.ac.gifu_nct.miekaji.utils.DataUtil

class GroupAdapter(private val friend:List<User>, private val listener: GroupListener):
    RecyclerView.Adapter<GroupHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        Log.d("Adapter","onCreateViewHolder")
        val rowView:View=LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_group, parent,false)
        return GroupHolder(rowView)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder")
        if(DataUtil.me!! == friend[position]) {
            holder.nameView.text = "${friend[position].userName} (自分)"
            holder.fWorkValue.text = "%.1f".format(friend[position].jobSum)
            holder.toGroup.text = "グループから離脱"
            holder.toGroup.setOnClickListener {
                Log.d("TODO", "Query for leave");
            }
        } else {
            holder.nameView.text=friend[position].userName
            holder.fWorkValue.text = "%.1f".format(friend[position].jobSum)
            holder.toGroup.setOnClickListener {
                Log.d("TODO", "Query for delete.");
            }
        }
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,friend[position])
        }
    }

    override fun getItemCount():Int{
        Log.d("Adapter","getItemCount")
        return friend.size
    }

    interface GroupListener{
        fun onClickRow(tappedView: View, friendData: User)
    }
}