package jp.ac.gifu_nct.miekaji.ui.housework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.detail.DetailFragment
import jp.ac.gifu_nct.miekaji.ui.flower.FlowerFragment
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import kotlinx.android.synthetic.main.fragment_housework.*


class HouseworkFragment : Fragment() {

    private lateinit var houseworkboardViewModel: HouseworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        houseworkboardViewModel =
            ViewModelProviders.of(this).get(HouseworkViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_housework, container, false)
        val Wvalue: TextView = root.findViewById(R.id.workValue)
        houseworkboardViewModel.text.observe(viewLifecycleOwner, Observer {
            Wvalue.text = "0"
        })

        root.findViewById<LinearLayout>(R.id.loadingOverlay_housework).visibility = View.VISIBLE
        Thread() {
            val jobs = DataUtil.fetchTodayJob()
            var sum = 0.0
            jobs.forEach {
                sum += it.jobValue
            }
            activity!!.runOnUiThread {
                Wvalue.text = "%.1f".format(sum)
                root.findViewById<LinearLayout>(R.id.loadingOverlay_housework).visibility = View.GONE
            }
        }.start()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailWork=view.findViewById<Button>(R.id.toDetail)
        val flowerJump=view.findViewById<Button>(R.id.toFlower)

        detailWork.setOnClickListener {
            val detailFragment= DetailFragment()
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,detailFragment)
            fragmentTransaction?.commit()
        }
        flowerJump.setOnClickListener {
            (activity!!.findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId = R.id.navigation_flower
            val flowerFragment= FlowerFragment()
            val fragmentTransaction= fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,flowerFragment)
            fragmentTransaction?.commit()
        }
    }
}