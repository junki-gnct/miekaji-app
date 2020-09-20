package jp.ac.gifu_nct.miekaji.ui.home

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.MeasureActivity
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.housework.HouseworkFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ToKind=view.findViewById<Button>(R.id.toWorkchoose)
        val ToWork=view.findViewById<Button>(R.id.toHouseWork)
        val ToDevice = view.findViewById<Button>(R.id.toDeviceConnectionTest)
        ToKind.setOnClickListener {
            val kindFragment = KindFragment()
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,kindFragment)
            fragmentTransaction?.commit()
        }
        ToWork.setOnClickListener {
            val HworkFragment = HouseworkFragment()
            (activity!!.findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId = R.id.navigation_housework
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,HworkFragment)
            fragmentTransaction?.commit()
        }
        ToDevice.setOnClickListener {
            val intent = Intent(activity, MeasureActivity::class.java)
            startActivity(intent)
        }
    }
}