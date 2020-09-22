package jp.ac.gifu_nct.miekaji.ui.friend

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.User
import jp.ac.gifu_nct.miekaji.ui.detail.DetailFragment
import jp.ac.gifu_nct.miekaji.ui.home.KindFragment
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.HashMap


class FriendListFragment:Fragment() {
    val FriendList = ArrayList<User>()
    lateinit var loadingR: LinearLayout
    lateinit var adapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("lifeCycle","onViewCreated")

        val handler = Handler(Looper.getMainLooper())
        view.findViewById<LinearLayout>(R.id.loadingOverlay).visibility = View.VISIBLE

        val recyclerView = recycler_list
        adapter = FriendAdapter(
            FriendList,
            object : FriendAdapter.ListListener {
                override fun onClickRow(tappedView: View, friendData: User) {
                    this@FriendListFragment.onClickRow(tappedView, friendData)
                }
            })

        Thread() {
            val cat = DataUtil.fetchFriends()
            FriendList.clear()
            if(cat.isNotEmpty()) {
                FriendList.addAll(cat)
            }
            handler.post {
                adapter.notifyDataSetChanged()
                view.findViewById<LinearLayout>(R.id.loadingOverlay).visibility = View.GONE
                // TODO: start icon loading.
            }
        }.start()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    fun onClickRow(tappedView: View, friendData: User){
        AlertDialog.Builder(tappedView.context)
            .setTitle("確認")
            .setMessage("${friendData.userName} をフレンドから削除しますか？")
            .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                val handler = Handler(Looper.myLooper()!!)
                val mProgressDialog = ProgressDialog(tappedView.context)
                mProgressDialog!!.setTitle("通信中...")
                mProgressDialog!!.setMessage("通信しています...")
                mProgressDialog!!.setCancelable(false)
                mProgressDialog!!.show()

                Thread() {
                    val args = HashMap<String, Any>()
                    args["token"] = AuthUtil.token!!
                    args["id"] = friendData.userID
                    val res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/friends/remove", args);
                    if (res.getInt("status") != 200) {
                        handler.post {
                            mProgressDialog.dismiss()
                            AlertDialog.Builder(tappedView.context)
                                .setTitle("エラー")
                                .setMessage("処理を完了できませんでした。")
                                .setPositiveButton("OK", null)
                                .show()
                        }
                        return@Thread
                    }

                    handler.post {
                        mProgressDialog!!.dismiss()
                        FriendList.remove(friendData)
                        adapter.notifyDataSetChanged()
                    }
                }.start()
            })
            .setNegativeButton("キャンセル", null)
            .show()
    }
}