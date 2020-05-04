package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentCustomerProfileBinding
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OutletListAdapter
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

        var newOrderModel = NewOrderModel(
            routeName = null,
            outletName =  customerProfileBinding.autoCompleteOutletName.text.toString(),
            distributorName =  null)
        customerProfileBinding.order=newOrderModel


        activity?.let {
            captureOutletViewModel?.getRouteList()?.observe(it, Observer<List<Route>> {
                val aa = ArrayAdapter(activity!!, android.R.layout.simple_spinner_dropdown_item, it)
                customerProfileBinding.spnRouteList.adapter=aa
            })
        }

        activity?.let {
            captureOutletViewModel?.getOutletList()?.observe(it, Observer<List<Outlet>> {
                Log.e("Outlet List ",""+ it!!.size)
                if (it != null) {
                    val adapter = OutletListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
                    customerProfileBinding.autoCompleteOutletName.setAdapter(adapter)
                    customerProfileBinding.autoCompleteOutletName.threshold = 2
                }
            })
        }


        captureOutletViewModel.nextFragmentNavigate.observe(this, Observer<NewOrderModel> { status ->
            status?.let {

                if (it != null) {
                    (activity as HomePageActivity).commonMethodForFragment(CustomerProfileDetailsFragment(it),true)
                    captureOutletViewModel.nextFragmentNavigate.value = null
                }
            }
        })

        return view
    }

}
