package jp.ac.gifu_nct.miekaji.ui.flower

import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimeCatcher {
    @RequiresApi(Build.VERSION_CODES.O)
    val Date= LocalDate.now()
    val SDate:String=Date.toString()
    val CDate:CharArray=SDate.toCharArray()
    val Ten=CDate[5]
    val Month=CDate[6]
}