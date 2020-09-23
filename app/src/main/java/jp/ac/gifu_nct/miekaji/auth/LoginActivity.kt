package jp.ac.gifu_nct.miekaji.auth

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import jp.ac.gifu_nct.miekaji.MainActivity
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setResult(Activity.RESULT_CANCELED)

        button2.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        button.setOnClickListener {
            val user = editTextTextPersonName5.text.toString()
            val pass = editTextTextPassword2.text.toString()
            val mProgressDialog = ProgressDialog(it.context)
            mProgressDialog!!.setTitle("通信中...")
            mProgressDialog!!.setMessage("通信しています...")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()

            Thread() {
                if(user.isEmpty() || pass.isEmpty()) {
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
                args["ID"] = user
                args["pass"] = pass

                val res= HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/auth/login", args)

                val token = res.getString("token")
                if(token == null || token == "null") {
                    runOnUiThread {
                        mProgressDialog.dismiss()
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("ユーザーIDまたはパスワードが違います。")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    return@Thread
                }

                AuthUtil.token = token
                runOnUiThread {
                    val sp = getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE)
                    val editor = sp.edit()
                    editor.putString("miekaji-id", user)
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
            false
        }
    }
}