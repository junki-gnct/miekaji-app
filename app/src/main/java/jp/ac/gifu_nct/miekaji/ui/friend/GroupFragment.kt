package jp.ac.gifu_nct.miekaji.ui.friend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import kotlinx.android.synthetic.main.fragment_list.*

class GroupFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_list,container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifeCycle","onViewCreated")

        val recyclerView=recycler_list
        val adapter= FriendAdapter(
            createDataList(),
            object : FriendAdapter.ListListener {
                override fun onClickRow(tappedView: View, friendData: FriendData) {
                    this@GroupFragment.onClickRow(tappedView, friendData)
                }
            })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=adapter
    }

    private fun createDataList():List<FriendData>{
        val dataList= mutableListOf<FriendData>()
        for (i in 0..5){
            val data: FriendData =
                FriendData().also {
                    it.name="グループ"+i
                }
            dataList.add(data)
        }
        return dataList
    }

    fun onClickRow(tappedView: View, friendData: FriendData){
        Toast.makeText(context, "リスト${friendData.name}", Toast.LENGTH_LONG).show()
    }
}