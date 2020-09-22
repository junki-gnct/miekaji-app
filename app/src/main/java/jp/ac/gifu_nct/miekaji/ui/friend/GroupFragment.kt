package jp.ac.gifu_nct.miekaji.ui.friend

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
import jp.ac.gifu_nct.miekaji.utils.DataUtil
import kotlinx.android.synthetic.main.fragment_list.*

class GroupFragment:Fragment() {
    val MemberList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.fragment_list,container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler(Looper.getMainLooper())
        view.findViewById<LinearLayout>(R.id.loadingOverlay).visibility = View.VISIBLE

        val recyclerView = recycler_list
        val adapter = GroupAdapter(
            MemberList,
            object : GroupAdapter.GroupListener {
                override fun onClickRow(tappedView: View, friendData: User) {
                    this@GroupFragment.onClickRow(tappedView, friendData)
                }
            })

        Thread() {
            if(DataUtil.me == null) DataUtil.me = DataUtil.fetchMe()
            val cat = DataUtil.fetchGroupMembers()
            MemberList.clear()
            MemberList.addAll(cat)
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
    }
}