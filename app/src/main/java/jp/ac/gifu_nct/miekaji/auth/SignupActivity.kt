package jp.ac.gifu_nct.miekaji.auth

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import jp.ac.gifu_nct.miekaji.MainActivity
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setResult(Activity.RESULT_CANCELED)

        button3.setOnClickListener {
            finish()
        }

        button.setOnClickListener {
            val user = editTextTextPersonName.text.toString()
            val id = editTextTextPersonName4.text.toString()
            val pass = editTextTextPassword.text.toString()

            val mProgressDialog = ProgressDialog(it.context)
            mProgressDialog!!.setTitle("通信中...")
            mProgressDialog!!.setMessage("通信しています...")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()

            Thread() {
                if(user.isEmpty() || pass.isEmpty() || id.isEmpty()) {
                    runOnUiThread {
                        mProgressDialog.dismiss()
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("未入力の項目があります。")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    return@Thread
                }

                val args = HashMap<String, Any>()
                args["ID"] = id
                args["pass"] = pass

                var res= HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/auth/create", args)

                val token = res.getString("token")
                if(token == null || token == "null") {
                    runOnUiThread {
                        mProgressDialog.dismiss()
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("ユーザー名が既に使用されています。")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    return@Thread
                }

                AuthUtil.token = token

                args.clear()
                args["token"] = token
                args["name"] = user
                res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/user/me", args)
                if(res.getInt("status") != 200) {
                    runOnUiThread {
                        mProgressDialog.dismiss()
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("エラーが発生しました。")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    return@Thread
                }

                args.clear()
                args["token"] = token
                args["name"] = "グループ"
                res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/create", args)
                if(res.getInt("status") != 200) {
                    runOnUiThread {
                        mProgressDialog.dismiss()
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("エラーが発生しました。")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    return@Thread
                }

                runOnUiThread {
                    val sp = getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE)
                    val editor = sp.edit()
                    editor.putString("miekaji-id", id)
                    editor.putString("miekaji-pass", pass)
                    editor.commit()
                    mProgressDialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }.start()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode != KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(keyCode, event)
        } else {
            finish()
            false
        }
    }
}