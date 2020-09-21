package jp.ac.gifu_nct.miekaji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_result.*
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        findViewById<TextView>(R.id.categoryView).text = "${intent.getStringExtra("displayName")} (重みづけ係数: ${intent.getDoubleExtra("jobWeight", 0.0)})"
        findViewById<TextView>(R.id.dateView).text = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
            Date()
        )
        findViewById<TextView>(R.id.workValueBox).text = "%.1f".format(intent.getDoubleExtra("jobValue", 0.0) * intent.getDoubleExtra("jobTime", 0.0) * intent.getDoubleExtra("jobWeight", 0.0))     // 家事量
        findViewById<TextView>(R.id.avgSpeed_2).text = "%.1f".format(intent.getDoubleExtra("jobValue", 0.0))       // 平均加速度
        findViewById<TextView>(R.id.measureMinutes_2).text = "%.1f".format(intent.getDoubleExtra("jobTime", 0.0)) // 測定時間

        ResultFinish.setOnClickListener {
            finish()
        }
    }
}