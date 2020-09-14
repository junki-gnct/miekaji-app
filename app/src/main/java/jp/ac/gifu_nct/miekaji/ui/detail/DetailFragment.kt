package jp.ac.gifu_nct.miekaji.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import kotlinx.android.synthetic.main.fragment_do.*

class DetailFragment:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_detail, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_flower)
        flowerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifeCycle","onViewCreated")

        val recyclerView=do_list
        val adapter=DetailAdapter(createDataList(), object : DetailAdapter.ListListener {
            override fun onClickRow(tappedView: View, listData: DetailData) {
                this@DetailFragment.onClickRow(tappedView,listData)
            }
        })

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=adapter
    }

    private fun createDataList():List<DetailData>{
        val dataList= mutableListOf<DetailData>()
        for (i in 0..10){
            val data:DetailData=DetailData().also {
                it.dowork=DetailData().workList[i]
            }
            dataList.add(data)
        }
        return dataList
    }

    fun onClickRow(tappedView:View,listData: DetailData){
        Toast.makeText(context, "リスト${listData.dowork}", Toast.LENGTH_LONG).show()
    }
}