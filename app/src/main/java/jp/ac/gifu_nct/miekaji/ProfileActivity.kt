package jp.ac.gifu_nct.miekaji

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import jp.ac.gifu_nct.miekaji.auth.LoginActivity
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.HashMap

class ProfileActivity : AppCompatActivity() {
    private fun showDialog(title: String, text: String) {
        if(!isFinishing) {
            this.runOnUiThread {
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(text)
                    .setPositiveButton("OK", null)
                    .setCancelable(false)
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        loadingOverlay_profile.visibility = View.VISIBLE
        Thread() {
            val id = getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE).getString("miekaji-id", null)
            DataUtil.me = DataUtil.fetchMe()
            val name = DataUtil.me!!.userName
            runOnUiThread() {
                textView16.text = id
                editTextTextPersonName6.setText(name)
                loadingOverlay_profile.visibility = View.GONE
            }
        }.start()

        button5.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("プロフィールを更新しますか？")
                .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    val name = editTextTextPersonName6.text.toString()
                    if(name.isEmpty()){
                        showDialog("エラー", "名前が入力されていません。")
                        return@OnClickListener
                    }

                    val mProgressDialog = ProgressDialog(this)
                    mProgressDialog!!.setTitle("通信中...")
                    mProgressDialog!!.setMessage("サーバーと通信しています...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.show()

                    Thread() {
                        val args = HashMap<String, Any>()
                        args["token"] = AuthUtil.token!!
                        args["name"] = name
                        val res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/user/me", args);
                        if (res.getInt("status") != 200) {
                            mProgressDialog!!.dismiss()
                            showDialog("エラー", "プロフィールの更新ができませんでした。")
                            return@Thread
                        }

                        runOnUiThread() {
                            showDialog("完了", "プロフィールを更新しました。")
                            mProgressDialog!!.dismiss()
                        }
                    }.start()
                })
                .setNegativeButton("いいえ", null)
                .show()
        }

        button6.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("ログアウトしますか？")
                .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                    val mProgressDialog = ProgressDialog(this)
                    mProgressDialog!!.setTitle("通信中...")
                    mProgressDialog!!.setMessage("サーバーと通信しています...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.show()

                    Thread() {
                        val args = HashMap<String, Any>()
                        args["token"] = AuthUtil.token!!
                        val res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/auth/logout", args);

                        runOnUiThread() {
                            mProgressDialog!!.dismiss()

                            val editor = getSharedPreferences("miekaji-auth", Context.MODE_PRIVATE).edit()
                            editor.remove("miekaji-id")
                            editor.remove("miekaji-pass")
                            editor.commit()

                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }.start()
                })
                .setNegativeButton("いいえ", null)
                .show()
        }
    }
}