package jp.ac.gifu_nct.miekaji.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.JobCategoryandValue
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import kotlinx.android.synthetic.main.fragment_do.*

class DetailFragment:Fragment() {
    private val dataList = ArrayList<JobCategoryandValue>()

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

        val handler = Handler(Looper.getMainLooper())
        view.findViewById<LinearLayout>(R.id.loading_ovelay_detail).visibility = View.VISIBLE

        val recyclerView=do_list
        val adapter = DetailAdapter(dataList, object : DetailAdapter.ListListener {
            override fun onClickRow(tappedView: View, listData: JobCategoryandValue) {
                this@DetailFragment.onClickRow(tappedView,listData)
            }
        })

        Thread() {
            val jobs = DataUtil.fetchTodayJob()
            var sum = 0.0
            var time = 0.0
            jobs.forEach {
                sum += it.jobValue
                time += it.jobTime
            }
            dataList.clear()
            val sums = DataUtil.fetchTodayJobTimeValueByEach(jobs)
            sums.keys.forEach {
                dataList.add(JobCategoryandValue(it, sums[it]!!))
            }
            handler.post {
                adapter.notifyDataSetChanged()
                view.findViewById<LinearLayout>(R.id.loading_ovelay_detail).visibility = View.GONE
                view.findViewById<TextView>(R.id.timeText).text = "%.1f".format(time)
                view.findViewById<TextView>(R.id.textView4).text = "%.1f".format(sum)
            }
        }.start()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    fun onClickRow(tappedView:View,listData: JobCategoryandValue){
    }
}