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

    private var flowerValue = mutableListOf(0.0, 15000.0, 30000.0, 40000.0, 50000.0)
    private var flowerLabel = mutableListOf("種植えまであと", "発芽まであと", "蕾がつくまであと", "花が咲くまであと", "満開まであと")
    private var flowerImageList = mutableListOf(
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 1月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 2月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 3月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 4月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 5月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 6月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 7月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 8月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 9月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 10月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full"),  // 11月
        mutableListOf(null, "sp1seed", "sp1leaf", "sp1bud", "sp1flower", "sp1full")   // 12月
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
                requireActivity().runOnUiThread {
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
                            requireActivity(),
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
            (requireActivity().findViewById(R.id.nav_view) as BottomNavigationView).selectedItemId =
                R.id.navigation_housework
            val detailFragment = DetailFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment, detailFragment)
            fragmentTransaction?.commit()
        }
    }

}

