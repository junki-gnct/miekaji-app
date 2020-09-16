package jp.ac.gifu_nct.miekaji.ui.flower

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlowerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is flowers Fragment"
    }
    val text: LiveData<String> = _text
}