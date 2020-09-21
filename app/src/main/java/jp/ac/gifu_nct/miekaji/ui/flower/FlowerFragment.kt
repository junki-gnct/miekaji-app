package jp.ac.gifu_nct.miekaji.ui.flower

import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.detail.DetailFragment
import jp.ac.gifu_nct.miekaji.utils.DataUtil


class FlowerFragment : Fragment() {

    private lateinit var flowerViewModel: FlowerViewModel

    private var flowerValue = mutableListOf(0.0, 15000.0, 30000.0, 40000.0)
    private var flowerLabel = mutableListOf("種植えまであと", "発芽まであと", "蕾がつくまであと", "花が咲くまであと")
    private var flowerImageList = mutableListOf(
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 1月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 2月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 3月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 4月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 5月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 6月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 7月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 8月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 9月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 10月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower"),  // 11月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower")   // 12月
    )
    private var flowerIndex = 0

    fun getImage(c: Context, ImageName: String?): Drawable? {
        return c.resources
            .getDrawable(c.resources.getIdentifier(ImageName, "drawable", c.packageName))
    }

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
        if (activity != null) {
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
                    root.findViewById<TextView>(R.id.editTextTextPersonName2).text =
                        "%.1f".format(sum)
                    run loop@{
                        flowerValue.forEachIndexed { index, value ->
                            Log.d("TAG", "[%d] %.1f".format(index, value))
                            flowerIndex = index
                            if (value > sum) {
                                return@loop
                            }
                        }
                    }
                    val remain =
                        maxOf(0.0, flowerValue[minOf(flowerIndex, flowerValue.size - 1)] - sum)
                    root.findViewById<TextView>(R.id.editTextTextPersonName3).text =
                        "%.1f".format(remain)
                    root.findViewById<TextView>(R.id.textView3).text =
                        flowerLabel[minOf(flowerIndex, flowerValue.size - 1)]
                    var imageIndex = flowerIndex
                    if (remain <= 0.0) {
                        imageIndex++
                    }
                    val month =
                        Integer.parseInt(TimeCatcher().Ten.toString() + TimeCatcher().Month.toString())
                    root.findViewById<ImageView>(R.id.flowerImage).setImageDrawable(
                        getImage(
                            activity!!,
                            flowerImageList[month - 1][imageIndex]
                        )
                    )
                    loading.visibility = View.GONE
                }
            }.start()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flowerImg = view.findViewById<ImageView>(R.id.flowerImage)
        val Jump = view.findViewById<Button>(R.id.jumpbutton)
        //月で分ける
        //Flowerset(TimeCatcher().Ten,TimeCatcher().Month)

        Jump.setOnClickListener {
            (activity!!.findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId =
                R.id.navigation_housework
            val detailFragment = DetailFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment, detailFragment)
            fragmentTransaction?.commit()
        }
    }

/*fun Flowerset(ten:Char,month:Char){
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

fun Flowerset(ten:Char,month:Char){
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

