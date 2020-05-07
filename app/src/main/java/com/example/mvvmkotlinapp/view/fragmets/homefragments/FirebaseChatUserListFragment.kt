package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentFirebaseChatUserListBinding
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.ChatUsersAdapter
import com.example.mvvmkotlinapp.view.fragmets.ChatFragment
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class FirebaseChatUserListFragment : Fragment() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentFirebaseChatUserListBinding

    private var arryListUsersList = ArrayList<String>()
    private var userSession: UserSession? =null
    private val skyggeProgressDialog = CustomProgressDialog()
    private var adapter: ChatUsersAdapter? =  null


    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_firebase_chat_user_list, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        getObjectsInitialize()

        activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }
        activity?.let {
            firebaseChatViewModel?.getChatUsersList()?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    setAdapter(it.toString())
            })
        }

        binding.usersList.addOnItemTouchListener(activity?.let {
            RecyclerItemClickListenr(it, binding.usersList, object : RecyclerItemClickListenr.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {
                    userSession!!.setChatWith(arryListUsersList[position])
                    (getActivity() as HomePageActivity?)?.commonMethodForFragment(ChatFragment(),true)
                }
                override fun onItemLongClick(view: View?, position: Int) {
                }
            })
        }!!)
        return view
    }

    private fun getObjectsInitialize() {
        userSession=UserSession(activity)
        (getActivity() as HomePageActivity?)?.visibleMenuItems(14)

    }

    fun setAdapter(s: String?) {
        try {
            arryListUsersList.clear()
            val obj = JSONObject(s)
            val i: Iterator<*> = obj.keys()
            var key = ""
            while (i.hasNext()) {
                key = i.next().toString()
                if (!key.equals(userSession?.getMobile())) {
                    arryListUsersList.add(key)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (arryListUsersList.size == 0) {
            binding.noUsersText.setVisibility(View.VISIBLE)
            binding.usersList.setVisibility(View.GONE)
        } else {
            binding.noUsersText.setVisibility(View.GONE)
            binding.usersList.setVisibility(View.VISIBLE)

            adapter = activity?.let { ChatUsersAdapter(it,arryListUsersList) }
            binding.usersList!!.adapter = adapter
            binding.usersList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            val dividerItemDecoration = DividerItemDecoration(binding.usersList.getContext(), LinearLayoutManager.VERTICAL)
            binding.usersList.addItemDecoration(dividerItemDecoration)
        }
        skyggeProgressDialog?.dialog?.dismiss()
    }
}
