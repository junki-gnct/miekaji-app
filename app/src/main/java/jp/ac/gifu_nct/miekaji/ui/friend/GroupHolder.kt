package jp.ac.gifu_nct.miekaji.ui.friend

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R

class GroupHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val nameView: TextView =itemView.findViewById(R.id.name2)
    val toGroup:Button=itemView.findViewById(R.id.button42)
    val fWorkValue: TextView = itemView.findViewById(R.id.fworkValue2)
}