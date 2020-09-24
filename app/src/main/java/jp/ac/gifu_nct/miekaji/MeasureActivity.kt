package jp.ac.gifu_nct.miekaji

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.ac.gifu_nct.miekaji.structures.JobCategory
import jp.ac.gifu_nct.miekaji.structures.SocketCallback
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import kotlinx.android.synthetic.main.activity_measure.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.*

/**
 *　計測時に使用するActivityです。
 * ペアリング済みのBluetoothデバイスに接続し、データの受信を開始します。
 */
class MeasureActivity : AppCompatActivity() {

    var mBluetoothAdapter: BluetoothAdapter? = null
    var mProgressDialog: ProgressDialog? = null

    private val REQUEST_ENABLE_BT = 3
    private var mBTDevice: BluetoothDevice? = null
    private var mBTSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null
    private var mBufferedReader: BufferedReader? = null
    private val uuid = "00001101-0000-1000-8000-00805F9B34FB" //通信規格がSPPであることを示す数字
    private var startTime: Long = System.currentTimeMillis()

    var accelValue = 0.0
    var dataCount = 0
    var category: JobCategory? = null

    private lateinit var callback: CallBackActivity
    class CallBackActivity: SocketCallback {
        private var activity: MeasureActivity
        constructor(activity: MeasureActivity) {
            this.activity = activity
        }

        override fun onMessage(message: String) {
            activity.runOnUiThread {
                if(message.toLowerCase().startsWith("accel_")) {
                    val accel = (message.toLowerCase().replace("accel_", "")).toDouble()
                    activity.accelValue += accel
                    activity.dataCount++

                    val time = (System.currentTimeMillis() - activity.startTime!!) / 1000 / 60.0
                    activity.findViewById<TextView>(R.id.JobValue).text = "%.1f".format(activity.accelValue / activity.dataCount * time * activity.category!!.jobWeight)
                    activity.findViewById<TextView>(R.id.avgSpeed).text = "%.1f".format(activity.accelValue / activity.dataCount)
                    activity.findViewById<TextView>(R.id.measureMinutes).text = "%.1f 分".format(time)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode != KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event)
        } else {
            AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("測定を中止しますか？")
                .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    finish()
                })
                .setNegativeButton("いいえ", null)
                .show()
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)
        setResult(Activity.RESULT_CANCELED)

        val intent = this.intent!!
        category = JobCategory(
            intent.getLongExtra("categoryID", -1),
            intent.getStringExtra("displayName")!!,
            intent.getStringExtra("categoryDetail")!!,
            intent.getDoubleExtra("jobWeight", 0.0)
        )

        measureFinish.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("測定を終了しますか？")
                .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    val timeB = (System.currentTimeMillis() - startTime!!) / 1000;
                    val time = timeB / 60.0  // job time.
                    if(dataCount == 0) dataCount = 1;
                    var job = accelValue / dataCount

                    mProgressDialog = ProgressDialog(this)
                    mProgressDialog!!.setTitle("通信中...")
                    mProgressDialog!!.setMessage("測定結果をサーバーへ送信しています...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.show()

                    Thread() {
                        // TODO: Send To Server.
                        val args = HashMap<String, Any>()
                        args["token"] = AuthUtil.token!!
                        args["category"] = category!!.categoryID
                        args["motion"] = job
                        args["time"] = time
                        val res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/job/new", args);
                        if (res.getInt("status") != 200) {
                            showErrorDialog(res.getString("message"))
                            return@Thread
                        }

                        runOnUiThread() {
                            mProgressDialog!!.dismiss()
                            val resultIntent = Intent()
                            resultIntent.putExtra("categoryID", category!!.categoryID)
                            resultIntent.putExtra("displayName", category!!.displayName)
                            resultIntent.putExtra("categoryDetail", category!!.categoryDetail)
                            resultIntent.putExtra("jobWeight", category!!.jobWeight)

                            resultIntent.putExtra("jobValue", job)
                            resultIntent.putExtra("jobTime", time)

                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                    }.start()
                })
                .setNegativeButton("いいえ", null)
                .show()
        }

        callback = CallBackActivity(this)
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            // Bluetooth is not supported on device.
            showErrorDialog("このデバイスではBluetoothがサポートされていません。")
            return
        }

        if (!mBluetoothAdapter!!.isEnabled) {
            // Request bluetooth.
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        } else if (mBTSocket == null) {
            Thread() {
                connectToDevice(callback)
            }.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mBTSocket != null) {
            mBTSocket!!.close()
        }
    }

    private fun showErrorDialog(text: String) {
        if(!isFinishing) {
            this.runOnUiThread {
                AlertDialog.Builder(this)
                    .setTitle("エラー")
                    .setMessage(text)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                        if(!AuthUtil.isDebug){
                            finish()
                        }
                    })
                    .setCancelable(false)
                    .show()
            }
        }
    }

    private fun connectToDevice(callback: SocketCallback) {
        if (mBluetoothAdapter == null) return
        val pairedDevices =
            mBluetoothAdapter!!.bondedDevices

        var deviceAddress: String? = null
        var deviceName: String? = null

        pairedDevices.forEach{
            if(it.name.toLowerCase().contains("miekaji-")) {
                deviceAddress = it.address
                deviceName = it.name
            }
        }

        if(deviceAddress == null) {
            runOnUiThread() {
                showErrorDialog("デバイスがペアリングされていません。")
            }
            return
        }

        runOnUiThread() {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setTitle("接続中...")
            mProgressDialog!!.setMessage(deviceName + "に接続しています...")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }

        mBTDevice = mBluetoothAdapter!!.getRemoteDevice(deviceAddress)
        mBTSocket = try {
            mBTDevice!!.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch(e: IOException) {
            runOnUiThread {
                mProgressDialog!!.dismiss()
                showErrorDialog("接続中にエラーが発生しました。")
                finish()
            }
            null
        }

        if(mBTSocket != null) {
            try {
                mBTSocket!!.connect()
            } catch(e: IOException) {
                try {
                    mBTSocket!!.close()
                    runOnUiThread {
                        mProgressDialog!!.dismiss()
                        mBTSocket = null
                        showErrorDialog("デバイスに接続できませんでした。")
                    }
                    return
                } catch(e2: IOException) {
                    runOnUiThread {
                        mProgressDialog!!.dismiss()
                        mBTSocket = null
                        showErrorDialog("接続中にエラーが発生しました。")
                    }
                    return
                }
            }

            if(mBTSocket == null) return
            mOutputStream = mBTSocket!!.outputStream
            if(mOutputStream == null) return
            mBufferedReader = BufferedReader(InputStreamReader(mBTSocket!!.inputStream))
            runOnUiThread {
                mProgressDialog!!.dismiss()
            }
            startTime = System.currentTimeMillis()
            startReading(callback)
        } else {
            runOnUiThread {
                mProgressDialog!!.dismiss()
                finish()
            }
        }

    }

    private fun startReading(callback: SocketCallback) {
        mBufferedReader = try {
            while(mBufferedReader != null) {
                callback.onMessage(mBufferedReader!!.readLine())
            }
            mBufferedReader!!.close()
            null
        } catch(e: IOException) {
            showErrorDialog("通信が切断されました。")
            null
        }
    }

    private fun sendMessage(message: String) {
        var bytes = byteArrayOf()
        bytes = message.toByteArray()
        try {
            mOutputStream!!.write(bytes)
        } catch (e: IOException) {
            try {
                mBTSocket!!.close()
            } catch (e1: IOException) { /*ignore*/
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Thread() {
                    connectToDevice(callback)
                }.start()
            } else {
                showErrorDialog("Bluetoothがオフのため測定を開始できません。")
            }
        }
    }


}