package jp.ac.gifu_nct.miekaji.ui.flower

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R
import kotlinx.android.synthetic.main.fragment_flowers.*

class FlowerFragment : Fragment() {

    private lateinit var flowerViewModel: FlowerViewModel
    companion object{
        private const val TAG="FlowerFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        flowerViewModel =
            ViewModelProviders.of(this).get(FlowerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_flowers, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_flower)
        flowerViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Jump=view.findViewById<Button>(R.id.jumpbutton)
        Jump.setOnClickListener {
            Log.d(TAG,"Jump pressed")
            val workFragment=WorkFragment()
            val fragmentTransaction=fragmentManager?.beginTransaction()
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.replace(R.id.nav_host_fragment,workFragment)
            fragmentTransaction?.commit()
        }
    }
}