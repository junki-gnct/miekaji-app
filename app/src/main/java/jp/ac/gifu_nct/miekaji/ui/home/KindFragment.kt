package jp.ac.gifu_nct.miekaji.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.housework.HouseworkViewModel
import kotlinx.android.synthetic.main.fragment_worklist.*

class KindFragment:Fragment() {
    private lateinit var kindViewModel: HouseworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_worklist,container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifeCycle","onViewCreated")

        val recyclerView=work_list
        val adapter=WorkAdapter(createDataList(), object : WorkAdapter.ListListener {
            override fun onClickRow(tappedView: View, listData: WorkData) {
                this@KindFragment.onClickRow(tappedView,listData)
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=adapter
    }

    private fun createDataList():List<WorkData>{
        val dataList= mutableListOf<WorkData>()
        for (i in 0..10){
            val data:WorkData=WorkData().also {
                it.work=WorkData().workList[i]
            }
            dataList.add(data)
        }
        return dataList
    }

    fun onClickRow(tappedView:View,listData: WorkData){
        Toast.makeText(context, "リスト${listData.work}", Toast.LENGTH_LONG).show()
    }
}