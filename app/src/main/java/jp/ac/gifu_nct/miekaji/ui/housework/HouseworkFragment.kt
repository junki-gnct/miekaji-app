package jp.ac.gifu_nct.miekaji.ui.housework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R

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
        val textView: TextView = root.findViewById(R.id.text_housework)
        houseworkboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}