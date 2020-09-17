package jp.ac.gifu_nct.miekaji.ui.friend

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R
import jp.ac.gifu_nct.miekaji.ui.home.KindFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_friend.*
import kotlinx.android.synthetic.main.recycle_item.*

class FriendFragment : Fragment() {

    private lateinit var friendViewModel: FriendViewModel

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
        val Rank=view.findViewById<Button>(R.id.rankingbutton)
        setFragment(FriendListFragment())
        Friend.setOnClickListener {
            //フレンドリストに切り替絵
            setFragment(FriendListFragment())
        }
        Group.setOnClickListener {
            //グループリストに切り替え
            setFragment(GroupFragment())
        }
        Rank.setOnClickListener {
            //家事量でソートする
            /**/
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