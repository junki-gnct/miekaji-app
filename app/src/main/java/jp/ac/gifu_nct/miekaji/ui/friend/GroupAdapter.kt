package jp.ac.gifu_nct.miekaji.ui.friend

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.structures.User
import jp.ac.gifu_nct.miekaji.utils.AuthUtil
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import jp.ac.gifu_nct.miekaji.utils.http.HTTPClient
import java.util.HashMap

class GroupAdapter(private val friend: ArrayList<User>, private val listener: GroupListener):
    RecyclerView.Adapter<GroupHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        Log.d("Adapter","onCreateViewHolder")
        val rowView:View=LayoutInflater.from(parent.context).inflate(R.layout.recycle_item_group, parent,false)
        return GroupHolder(rowView)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        Log.d("Adapter","onBindViewHolder")
        if(DataUtil.me!! == friend[position]) {
            holder.nameView.text = "${friend[position].userName} (自分)"
            holder.fWorkValue.text = "%.1f".format(friend[position].jobSum)
            holder.toGroup.text = "グループから離脱"
            holder.toGroup.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("確認")
                    .setMessage("グループから離脱しますか？")
                    .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                        val handler = Handler(Looper.myLooper()!!)
                        val mProgressDialog = ProgressDialog(holder.toGroup.context)
                        mProgressDialog!!.setTitle("通信中...")
                        mProgressDialog!!.setMessage("通信しています...")
                        mProgressDialog!!.setCancelable(false)
                        mProgressDialog!!.show()

                        Thread() {
                            val args = HashMap<String, Any>()
                            args["token"] = AuthUtil.token!!
                            args["name"] = "グループ"
                            var res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/leave", args);
                            if (res.getInt("status") != 200) {
                                handler.post {
                                    mProgressDialog.dismiss()
                                    AlertDialog.Builder(holder.toGroup.context)
                                        .setTitle("エラー")
                                        .setMessage("処理を完了できませんでした。")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                                return@Thread
                            }
                            res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/create", args);
                            if (res.getInt("status") != 200) {
                                handler.post {
                                    mProgressDialog.dismiss()
                                    AlertDialog.Builder(holder.toGroup.context)
                                        .setTitle("エラー")
                                        .setMessage("処理を完了できませんでした。")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                                return@Thread
                            }

                            handler.post {
                                mProgressDialog!!.dismiss()
                                // TODO: Refresh screen.
                                friend.removeAll { it2 -> it2 != friend[position] }
                                this.notifyDataSetChanged()
                            }
                        }.start()
                    })
                    .setNegativeButton("キャンセル", null)
                    .show()
            }
        } else {
            holder.nameView.text=friend[position].userName
            holder.fWorkValue.text = "%.1f".format(friend[position].jobSum)
            holder.toGroup.setOnClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("確認")
                    .setMessage("${friend[position].userName} をグループから削除しますか？")
                    .setPositiveButton("はい", DialogInterface.OnClickListener { _, _ ->
                        val handler = Handler(Looper.myLooper()!!)
                        val mProgressDialog = ProgressDialog(holder.toGroup.context)
                        mProgressDialog!!.setTitle("通信中...")
                        mProgressDialog!!.setMessage("通信しています...")
                        mProgressDialog!!.setCancelable(false)
                        mProgressDialog!!.show()

                        Thread() {
                            val groupID = DataUtil.fetchData("/fun/info", "").getLong("ID")

                            val args = HashMap<String, Any>()
                            args["token"] = AuthUtil.token!!
                            args["id"] = friend[position].userID
                            args["group_id"] = groupID
                            args["name"] = "グループ"
                            var res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/create", args);
                            if (res.getInt("status") != 200) {
                                handler.post {
                                    mProgressDialog.dismiss()
                                    AlertDialog.Builder(holder.toGroup.context)
                                        .setTitle("エラー")
                                        .setMessage("処理を完了できませんでした。")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                                return@Thread
                            }

                            res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/add", args);
                            if (res.getInt("status") != 200) {
                                handler.post {
                                    mProgressDialog.dismiss()
                                    AlertDialog.Builder(holder.toGroup.context)
                                        .setTitle("エラー")
                                        .setMessage("処理を完了できませんでした。")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                                return@Thread
                            }

                            res = HTTPClient.postRequest("${AuthUtil.API_BASE_URL}/fun/join", args);
                            if (res.getInt("status") != 200) {
                                handler.post {
                                    mProgressDialog.dismiss()
                                    AlertDialog.Builder(holder.toGroup.context)
                                        .setTitle("エラー")
                                        .setMessage("処理を完了できませんでした。")
                                        .setPositiveButton("OK", null)
                                        .show()
                                }
                                return@Thread
                            }

                            handler.post {
                                mProgressDialog!!.dismiss()
                                friend.removeAt(position)
                                this.notifyDataSetChanged()
                            }
                        }.start()
                    })
                    .setNegativeButton("キャンセル", null)
                    .show()
            }
        }
        holder.itemView.setOnClickListener {
            listener.onClickRow(it,friend[position])
        }
    }

    override fun getItemCount():Int{
        Log.d("Adapter","getItemCount")
        return friend.size
    }

    interface GroupListener{
        fun onClickRow(tappedView: View, friendData: User)
    }
}