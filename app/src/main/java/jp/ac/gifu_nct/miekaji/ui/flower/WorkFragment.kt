package jp.ac.gifu_nct.miekaji.ui.flower

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import jp.ac.gifu_nct.miekaji.R

class WorkFragment:Fragment() {
    companion object{
        private val TAG="WorkFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_work,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backbutton=view.findViewById<Button>(R.id.backbutton)
        backbutton.setOnClickListener {
            Log.d(TAG,"BackButton pressed")
            fragmentManager?.popBackStack()
        }
    }
}