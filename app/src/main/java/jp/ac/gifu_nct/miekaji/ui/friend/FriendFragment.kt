package jp.ac.gifu_nct.miekaji.ui.friend

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import java.util.HashMap

class FriendFragment : Fragment() {

    private lateinit var friendViewModel: FriendViewModel
    private var isFriend = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendViewModel =
            ViewModelProviders.of(this).get(FriendViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_friend, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_friend)
        friendViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Friend=view.findViewById<Button>(R.id.frilistbutton)
        val Group=view.findViewById<Button>(R.id.grolistbutton)
        val Rank=view.findViewById<Button>(R.id.friendbutton)
        setFragment(FriendListFragment())
        Friend.setOnClickListener {
            //フレンドリストに切り替絵
            isFriend = true
            setFragment(FriendListFragment())
        }
        Group.setOnClickListener {
            //グループリストに切り替え
            isFriend = false
            setFragment(GroupFragment())
        }
        Rank.setOnClickListener {
            val box = EditText(it.context)
            box.hint = "ユーザーID"
            AlertDialog.Builder(it.context)
                .setTitle("フレンド追加")
                .setMessage("フレンドに追加したい相手のユーザーIDを入力してください。")
                .setView(box)
                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    if(box.text.isEmpty()) {
                        AlertDialog.Builder(it.context)
                            .setTitle("エラー")
                            .setMessage("ユーザーIDが入力されていません。")
                            .show()
                        return@OnClickListener
                    }

                    val handler = Handler(Looper.myLooper()!!)
                    val mProgressDialog = ProgressDialog(it.context)
                    mProgressDialog!!.setTitle("通信中...")
                    mProgressDialog!!.setMessage("通信しています...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.show()
                    Thread() {
                        val args = HashMap<String, Any>()
                        args["token"] = AuthUtil.token!!
                        args["id"] = box.text.toString()

                        var res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/friends/add", args);
                        if (res.getInt("status") != 200) {
                            handler.post {
                                mProgressDialog.dismiss()
                                var message = "処理を完了できませんでした。"
                                when(res.getString("message")) {
                                    "Not enough arguments." -> {
                                        message = "IDが入力されていません。"
                                    }
                                    "Query failed." -> {
                                        message = "サーバーエラーが発生しました。"
                                    }
                                    "User not found." -> {
                                        message = "ユーザーが見つかりませんでした。"
                                    }
                                    "User is same." -> {
                                        message = "自分を追加することはできません。"
                                    }
                                    "User is already in friends." -> {
                                        message = "すでにフレンドに追加済みです。"
                                    }
                                }

                                AlertDialog.Builder(it.context)
                                    .setTitle("エラー")
                                    .setMessage(message)
                                    .setPositiveButton("OK", null)
                                    .show()
                            }
                            return@Thread
                        }

                        handler.post {
                            mProgressDialog!!.dismiss()
                            if(isFriend) {
                                // update screen.
                                setFragment(FriendListFragment())
                            }
                        }
                    }.start()
                })
                .setNegativeButton("キャンセル", null)
                .show()
        }
    }

    fun setFragment(fragment:Fragment) {
        val moveFragment = fragment
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.replace(R.id.listfragment, moveFragment)
        fragmentTransaction?.commit()
    }
}