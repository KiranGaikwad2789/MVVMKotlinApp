package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentCustomerProfileBinding
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.viewmodel.CaptureOutletViewModel

/**
 * A simple [Fragment] subclass.
 */
class CustomerProfileFragment : Fragment() {

    lateinit var captureOutletViewModel: CaptureOutletViewModel
    lateinit var customerProfileBinding: FragmentCustomerProfileBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        captureOutletViewModel = ViewModelProviders.of(this).get(CaptureOutletViewModel::class.java)
        customerProfileBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_customer_profile, container, false)
        val view: View = customerProfileBinding.getRoot()
        customerProfileBinding.lifecycleOwner = this
        customerProfileBinding.captureOutletViewModel=captureOutletViewModel

        (getActivity() as HomePageActivity?)?.visibleMenuItems(3)


        captureOutletViewModel.nextFragmentNavigate.observe(this, Observer<NewOrderModel> { status ->
            status?.let {

                if (it != null) {
                    Log.e("Outlet id ",""+ it)
                    (activity as HomePageActivity).commonMethodForFragment(CustomerProfileDetailsFragment(it),true)
                    captureOutletViewModel.nextFragmentNavigate.value = null
                }
            }
        })

        return view
    }

}
