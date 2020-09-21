package jp.ac.gifu_nct.miekaji.ui.friend

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.User

class GroupAdapter(private val friend:List<User>, private val listener: GroupListener):
    RecyclerView.Adapter<GroupHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        Log.d("Adapter","onCreateViewHolder")
        val rowView:View=LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_group, parent,false)
        return GroupHolder(rowView)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder")
        holder.nameView.text=friend[position].userName
        holder.fWorkValue.text = "%.1f".format(friend[position].jobSum)
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