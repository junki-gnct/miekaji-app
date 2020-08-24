package jp.ac.gifu_nct.miekaji.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment:Fragment() {

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
        val adapter=ViewAdapter(createDataList(), object : ViewAdapter.ListListener {
            override fun onClickRow(tappedView: View, listData: ListData) {
                this@ListFragment.onClickRow(tappedView,listData)
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=adapter
    }

    private fun createDataList():List<ListData>{
        val dataList= mutableListOf<ListData>()
        for (i in 0..10){
            val data:ListData=ListData().also {
                it.name="フレンド"+i
            }
            dataList.add(data)
        }
        return dataList
    }

    fun onClickRow(tappedView:View,lidtData: ListData){
    }
}