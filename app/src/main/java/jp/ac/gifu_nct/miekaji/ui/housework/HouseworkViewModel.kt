package jp.ac.gifu_nct.miekaji.ui.housework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HouseworkViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is housework Fragment"
    }
    val text: LiveData<String> = _text
}