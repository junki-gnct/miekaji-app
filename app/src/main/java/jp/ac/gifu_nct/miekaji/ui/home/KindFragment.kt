package jp.ac.gifu_nct.miekaji.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.JobCategory
import jp.ac.gifu_nct.miekaji.ui.housework.HouseworkViewModel
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import kotlinx.android.synthetic.main.fragment_list.*

class KindFragment:Fragment() {
    val CategoryList = ArrayList<JobCategory>()

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
        val adapter = WorkAdapter(CategoryList, object : WorkAdapter.ListListener {
            override fun onClickRow(tappedView: View, listData: JobCategory) {
                this@KindFragment.onClickRow(tappedView,listData)
            }
        })

        val handler = Handler(Looper.getMainLooper())
        view.findViewById<LinearLayout>(R.id.loadingOverlay).visibility = View.VISIBLE

        Thread() {
            val cat = DataUtil.fetchCategories()
            CategoryList.clear()
            CategoryList.addAll(cat)
            handler.post {
                adapter.notifyDataSetChanged()
                view.findViewById<LinearLayout>(R.id.loadingOverlay).visibility = View.GONE
            }
        }.start()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=adapter
    }

    fun onClickRow(tappedView:View, listData: JobCategory){
        setFragmentResult("home_category", bundleOf(
            "categoryID" to listData.categoryID,
            "displayName" to listData.displayName,
            "categoryDetail" to listData.categoryDetail,
            "jobWeight" to listData.jobWeight
        ))
        parentFragmentManager.popBackStack()
    }
}