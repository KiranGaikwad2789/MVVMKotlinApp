package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.UserSession
import com.example.mvvmkotlinapp.databinding.FragmentFirebaseChatUserListBinding
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.fragmets.ChatFragment
import com.example.mvvmkotlinapp.viewmodel.FirebaseChatViewModel
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class FirebaseChatUserListFragment : Fragment() {

    lateinit var firebaseChatViewModel: FirebaseChatViewModel
    lateinit var binding: FragmentFirebaseChatUserListBinding

    var al = ArrayList<String>()
    var totalUsers = 0
    var pd: ProgressDialog? = null
    private var userSession: UserSession? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        firebaseChatViewModel = ViewModelProviders.of(this).get(FirebaseChatViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_firebase_chat_user_list, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.firebaseChatViewModel=firebaseChatViewModel

        userSession=UserSession(activity)


        /*activity?.let {
            firebaseChatViewModel?.getChatUsersList()?.observe(it, Observer<String?> {
                Log.e("Retrofit response ",it)
                //this.setDataToAdapter(it)
            })
        }*/


        (getActivity() as HomePageActivity?)?.visibleMenuItems(14)
        pd = ProgressDialog(activity)
        pd!!.setMessage("Loading...")
        pd!!.show()

        val url = "https://chatapp-72cf4.firebaseio.com/users.json"

        val request =
            StringRequest(
                Request.Method.GET, url,
                Response.Listener { s -> doOnSuccess(s) },
                Response.ErrorListener { volleyError -> println("" + volleyError) })

        val rQueue = Volley.newRequestQueue(activity)
        rQueue.add(request)

        binding.usersList.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            userSession!!.setChatWith(al[position])
            (getActivity() as HomePageActivity?)?.commonMethodForFragment(ChatFragment(),true)
        })


        // Inflate the layout for this fragment
        return view
    }

    fun doOnSuccess(s: String?) {

        Log.e("User lists: ",s.toString())
        try {
            al.clear()
            val obj = JSONObject(s)
            val i: Iterator<*> = obj.keys()
            var key = ""
            Log.e("username : ", userSession?.getMobile())
            while (i.hasNext()) {
                key = i.next().toString()
                if (!key.equals(userSession?.getMobile())) {
                    Log.e("key : ",key.toString())
                    al.add(key)
                }
                totalUsers++
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (totalUsers <= 1) {
            binding.noUsersText.setVisibility(View.VISIBLE)
            binding.usersList.setVisibility(View.GONE)
        } else {
            binding.noUsersText.setVisibility(View.GONE)
            binding.usersList.setVisibility(View.VISIBLE)
            binding.usersList.adapter=null
            binding.usersList.setAdapter(
                activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_list_item_1,
                        al
                    )
                }
            )
        }
        pd!!.dismiss()
    }
}
