package jp.ac.gifu_nct.miekaji.ui.flower

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.detail.DetailFragment
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import kotlinx.android.synthetic.main.fragment_flowers.*
import org.w3c.dom.Text

class FlowerFragment : Fragment() {

    private lateinit var flowerViewModel: FlowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flowerViewModel =
            ViewModelProviders.of(this).get(FlowerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_flowers, container, false)
        /*val textView: TextView = root.findViewById(R.id.dayWorkText)
        flowerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        if(activity != null) {
            val loading = root.findViewById<LinearLayout>(R.id.loadingOverlay_flower)
            loading.visibility = View.VISIBLE
            Thread() {
                val jobs = DataUtil.fetchGroupMembers()
                var sum = 0.0
                var today = 0.0
                jobs.forEach {
                    sum += it.jobSum
                    today += it.todaySum
                }
                activity!!.runOnUiThread {
                    root.findViewById<TextView>(R.id.dayWorkText).text = "%.1f".format(today)
                    root.findViewById<TextView>(R.id.editTextTextPersonName2).text = "%.1f".format(sum)
                    root.findViewById<TextView>(R.id.editTextTextPersonName3).text = "EditText"
                    loading.visibility = View.GONE
                }
            }.start()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flowerImg=view.findViewById<ImageView>(R.id.flowerImage)
        val Jump=view.findViewById<Button>(R.id.jumpbutton)
        //月で分ける
        Flowerset(TimeCatcher().Ten,TimeCatcher().Month)

        Jump.setOnClickListener {
            (activity!!.findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId = R.id.navigation_housework
            val detailFragment= DetailFragment()
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,detailFragment)
            fragmentTransaction?.commit()
        }
    }

    fun Flowerset(ten:Char,month:Char){
        when (month){
            '2'->flowerImage.setImageResource(R.drawable.sp1flower)
            '3'->flowerImage.setImageResource(R.drawable.sp1flower)
            '4'->flowerImage.setImageResource(R.drawable.sp1flower)
            '5'->flowerImage.setImageResource(R.drawable.sp1flower)
            '6'->flowerImage.setImageResource(R.drawable.sp1flower)
            '7'->flowerImage.setImageResource(R.drawable.sp1flower)
            '8'->flowerImage.setImageResource(R.drawable.sp1flower)
            '9'->flowerImage.setImageResource(R.drawable.sp1flower)
            '0'->flowerImage.setImageResource(R.drawable.sp1flower)
        }
        when(ten){
            '1'->when(month){
                '0'->flowerImage.setImageResource(R.drawable.sp1flower)
                '1'->flowerImage.setImageResource(R.drawable.sp1flower)
                '2'->flowerImage.setImageResource(R.drawable.sp1flower)
            }
        }
    }

    /*fun Flowerset(ten:Char,month:Char){
        when (month){
            '2'->flowerImage.setImageResource(R.drawable.flowex)
            '3'->flowerImage.setImageResource(R.drawable.sp1flower)
            '4'->flowerImage.setImageResource(R.drawable.sp1flower)
            '5'->flowerImage.setImageResource(R.drawable.flowex)
            '6'->flowerImage.setImageResource(R.drawable.flowex)
            '7'->flowerImage.setImageResource(R.drawable.flowex)
            '8'->flowerImage.setImageResource(R.drawable.flowex)
            '9'->flowerImage.setImageResource(R.drawable.sp1flower)
            '0'->flowerImage.setImageResource(R.drawable.flowex)
        }
        when(ten){
            '1'->when(month){
                '0'->flowerImage.setImageResource(R.drawable.flowex)
                '1'->flowerImage.setImageResource(R.drawable.flowex)
                '2'->flowerImage.setImageResource(R.drawable.flowex)
            }
        }
    }*/
}