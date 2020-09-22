package jp.ac.gifu_nct.miekaji.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import jp.ac.gifu_nct.miekaji.R

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
        val Rank=view.findViewById<Button>(R.id.friendbutton)
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
            // フレンド追加画面にいく
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