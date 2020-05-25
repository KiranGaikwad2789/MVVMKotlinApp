package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.graphics.ColorSpace
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentFirebaseChatUserListBinding
import com.example.mvvmkotlinapp.interfaces.DrawerLocker
import com.example.mvvmkotlinapp.model.FirebaseUser
import com.example.mvvmkotlinapp.utils.CustomProgressDialog
import com.example.mvvmkotlinapp.view.AESUtils
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.ChatUsersAdapter
import com.example.mvvmkotlinapp.view.fragmets.ChatFragment
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class FirebaseChatUserListFragment : Fragment() {


    //https://github.com/topics/firebase-database?l=kotlin

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentFirebaseChatUserListBinding

    private var arryListUsersList = ArrayList<FirebaseUser>()
    private var userSession: UserSession? =null
    private val skyggeProgressDialog = CustomProgressDialog()
    private var adapter: ChatUsersAdapter? =  null
    private var aesEncryptions: AESUtils? =null




    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_firebase_chat_user_list, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        getObjectsInitialize(view)

        activity?.let { skyggeProgressDialog.show(it,"Please Wait...") }
        activity?.let {
            firebaseChatViewModel?.getChatUsersList()?.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it!=null){
                    setAdapter(it.toString())
                }
            })
        }

        binding.usersList.addOnItemTouchListener(activity?.let {
            RecyclerItemClickListenr(it, binding.usersList, object : RecyclerItemClickListenr.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {
                    userSession!!.setChatWith(aesEncryptions?.decrypt(arryListUsersList[position].imeinumber.toString()))
                    Log.e("IMEI selected ",""+aesEncryptions?.decrypt(arryListUsersList[position].imeinumber.toString()))
                    userSession!!.setChatWithUser(aesEncryptions?.decrypt(arryListUsersList[position].username.toString()))
                    (getActivity() as HomePageActivity?)?.commonMethodForFragment(ChatFragment(),true)
                }
                override fun onItemLongClick(view: View?, position: Int) {
                }
            })
        }!!)
        return view
    }

    private fun getObjectsInitialize(view: View) {
        userSession=UserSession(activity)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as DrawerLocker?)!!.setDrawerEnabled(false)
        (getActivity() as HomePageActivity?)?.visibleMenuItems(14)
        val toolbar: Toolbar = view?.findViewById(R.id.toolbar) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        toolbar.setTitle("Chat")
        aesEncryptions= AESUtils()

    }

    fun setAdapter(s: String?) {
        try {
            arryListUsersList.clear()
            val obj = JSONObject(s)
            Log.e("JSON ",""+obj)

            val i: Iterator<*> = obj.keys()
            var key = ""
            while (i.hasNext()) {
                key = i.next().toString()
                var jsonarray_info:JSONObject= obj.getJSONObject(key)
                Log.e("jsonarray_info ",""+jsonarray_info)

                if (!key.equals(userSession?.getIMEI())) {
                    var user=FirebaseUser(jsonarray_info.getString("user_id"),jsonarray_info.getString("username"),jsonarray_info.getString("mobilenumber"),jsonarray_info.getString("imeinumber"))
                    arryListUsersList.add(user)
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
