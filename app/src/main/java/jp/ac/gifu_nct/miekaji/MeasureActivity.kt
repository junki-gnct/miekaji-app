package jp.ac.gifu_nct.miekaji

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Measure
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import jp.ac.gifu_nct.miekaji.structures.SocketCallback
import java.io.*
import java.net.Socket
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

    private lateinit var callback: CallBackActivity
    class CallBackActivity: SocketCallback {
        private lateinit var activity: MeasureActivity
        constructor(activity: MeasureActivity) {
            this.activity = activity
        }

        override fun onMessage(message: String) {
            activity.runOnUiThread {
                Toast.makeText(activity, "受信: ${message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)

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
        this.runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle("エラー")
                .setMessage(text)
                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    finish()
                })
                .show()
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
                        Log.d("TAG", "error1")
                        showErrorDialog("デバイスに接続できませんでした。")
                    }
                    return
                } catch(e2: IOException) {
                    runOnUiThread {
                        mProgressDialog!!.dismiss()
                        mBTSocket = null
                        Log.d("TAG", "error2")
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
            startReading(callback)
        } else {
            runOnUiThread {
                mProgressDialog!!.dismiss()
                Log.d("TAG", "error3")
                finish()
            }
        }

    }

    private fun startReading(callback: SocketCallback) {
        Log.d("TAG", "Start.")
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