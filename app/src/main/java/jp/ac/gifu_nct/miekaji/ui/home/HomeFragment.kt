package jp.ac.gifu_nct.miekaji.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.MeasureActivity
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ResultActivity
import jp.ac.gifu_nct.miekaji.structures.JobCategory
import jp.ac.gifu_nct.miekaji.ui.housework.HouseworkFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var category: JobCategory? = null

    private val measureRequestCode = 1
    private val resultRequestCode = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ToKind=view.findViewById<Button>(R.id.toWorkchoose)
        val ToWork=view.findViewById<Button>(R.id.toHouseWork)
        setFragmentResultListener("home_category") { _, bundle ->
            run {
                val categoryID = bundle.getLong("categoryID")
                val categoryName = bundle.getString("displayName")
                val categoryDetail = bundle.getString("categoryDetail")
                val jobWeight = bundle.getDouble("jobWeight")
                if (categoryID != null &&
                        categoryName != null &&
                        categoryDetail != null &&
                        jobWeight != null) {
                    category = JobCategory(categoryID, categoryName, categoryDetail, jobWeight)
                    // TODO: SET BUTTON LABEL
                }
            }
        }

        ToKind.setOnClickListener {
            val kindFragment = KindFragment()
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,kindFragment)
            fragmentTransaction?.commit()
        }
        ToWork.setOnClickListener {
            if(category == null) {
                AlertDialog.Builder(requireActivity()!!)
                    .setTitle("エラー")
                    .setMessage("家事が選択されていません。")
                    .setPositiveButton("OK", null)
                    .setCancelable(false)
                    .show()
                return@setOnClickListener;
            }
            val intent = Intent(activity, MeasureActivity::class.java)
            intent.putExtra("categoryID", category!!.categoryID)
            intent.putExtra("displayName", category!!.displayName)
            intent.putExtra("categoryDetail", category!!.categoryDetail)
            intent.putExtra("jobWeight", category!!.jobWeight)
            startActivityForResult(intent, measureRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == measureRequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO: Receive some results.
                val intent = Intent(activity, ResultActivity::class.java)
                // TODO: send some results.
                startActivityForResult(intent, resultRequestCode)
            }
        } else if(requestCode == resultRequestCode) {
            val HworkFragment = HouseworkFragment()
            (requireActivity()!!.findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId = R.id.navigation_housework
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,HworkFragment)
            fragmentTransaction?.commit()
        }
    }
}