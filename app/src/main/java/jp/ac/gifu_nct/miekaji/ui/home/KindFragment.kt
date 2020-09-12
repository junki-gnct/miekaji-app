package jp.ac.gifu_nct.miekaji.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.housework.HouseworkViewModel

class KindFragment:Fragment() {
    private lateinit var kindViewModel: HouseworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kindViewModel =
            ViewModelProviders.of(this).get(HouseworkViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_housework, container, false)
        val textView: TextView = root.findViewById(R.id.text_housework)
        kindViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}