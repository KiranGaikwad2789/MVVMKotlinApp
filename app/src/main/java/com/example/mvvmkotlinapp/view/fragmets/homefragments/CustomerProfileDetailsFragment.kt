package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentCustomerProfileDetailsBinding
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.model.OutletSubType
import com.example.mvvmkotlinapp.model.OutletType
import com.example.mvvmkotlinapp.repository.room.tables.OutletDetails
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.viewmodel.CustomerProfileViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class CustomerProfileDetailsFragment(var orderModel: NewOrderModel) : Fragment() {

    lateinit var customerProfileViewModel: CustomerProfileViewModel
    lateinit var customerProfileBinding: FragmentCustomerProfileDetailsBinding
    private var outletDetails: OutletDetails? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        customerProfileViewModel = ViewModelProviders.of(this).get(CustomerProfileViewModel::class.java)
        customerProfileBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_customer_profile_details, container, false)
        val view: View = customerProfileBinding.getRoot()
        customerProfileBinding.lifecycleOwner = this
        customerProfileBinding.customerProfileViewModel=customerProfileViewModel

        var outletValNID= orderModel.outletName?.split(" | ")
        var routeValNID= orderModel.routeName?.split(" | ")
        Log.e("Order model ",orderModel.toString())


        customerProfileBinding.edtDateOfEstablished.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))

        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var selectedDate:String = sdf.format(cal.time)
            customerProfileBinding.edtDateOfEstablished.setText(selectedDate)
            view.maxDate=cal.timeInMillis
        }
        customerProfileBinding.edtDateOfEstablished.setOnClickListener {
            activity?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        activity?.let {
            customerProfileViewModel?.getOutletDetails(outletValNID!![1])?.observe(it, Observer<OutletDetails> {
                if (it != null) {
                    Log.e("Outlet fragment details ",""+ it)
                    outletDetails=it

                    var outletDetails = OutletDetails(
                        uid = outletDetails?.uid,
                        outlet_id = outletDetails?.outlet_id,
                        outlet_name = outletDetails?.outlet_name,
                        route_name = outletDetails?.route_name,
                        dist_name = outletDetails?.dist_name,
                        outlet_type = outletDetails?.outlet_type,
                        outlet_subtype = outletDetails?.outlet_subtype,
                        outlet_address =outletDetails?.outlet_address,
                        outlet_owner_name = outletDetails?.outlet_owner_name,
                        outlet_contact_number = outletDetails?.outlet_contact_number,
                        outlet_contact_alt_number = outletDetails?.outlet_contact_alt_number,
                        outlet_email_id = outletDetails?.outlet_email_id,
                        outlet_contact_person = outletDetails?.outlet_contact_person,
                        outlet_sizeof_facility = outletDetails?.outlet_sizeof_facility,
                        outlet_GST_number = outletDetails?.outlet_GST_number,
                        outlet_license_details =outletDetails?.outlet_license_details,
                        outlet_business_monthly =outletDetails?.outlet_business_monthly,
                        outlet_day_footprint =outletDetails?.outlet_day_footprint,
                        outlet_competitor_remarks =outletDetails?.outlet_competitor_remarks,
                        outlet_date_of_established =outletDetails?.outlet_date_of_established,
                        outlet_business_segment =outletDetails?.outlet_business_segment,
                        outlet_primary_business =outletDetails?.outlet_primary_business,
                        outlet_status =outletDetails?.outlet_status)

                    customerProfileBinding.outletDetails=outletDetails
                }else{

                    var outletDetails = OutletDetails(
                        uid = outletDetails?.uid,
                        outlet_id = outletValNID!![1].toInt(),
                        outlet_name = outletValNID!![0],
                        route_name = routeValNID?.get(0),
                        dist_name = outletDetails?.dist_name,
                        outlet_type = outletDetails?.outlet_type,
                        outlet_subtype = outletDetails?.outlet_subtype,
                        outlet_address =outletDetails?.outlet_address,
                        outlet_owner_name = outletDetails?.outlet_owner_name,
                        outlet_contact_number = outletDetails?.outlet_contact_number,
                        outlet_contact_alt_number = outletDetails?.outlet_contact_alt_number,
                        outlet_email_id = outletDetails?.outlet_email_id,
                        outlet_contact_person = outletDetails?.outlet_contact_person,
                        outlet_sizeof_facility = outletDetails?.outlet_sizeof_facility,
                        outlet_GST_number = outletDetails?.outlet_GST_number,
                        outlet_license_details =outletDetails?.outlet_license_details,
                        outlet_business_monthly =outletDetails?.outlet_business_monthly,
                        outlet_day_footprint =outletDetails?.outlet_day_footprint,
                        outlet_competitor_remarks =outletDetails?.outlet_competitor_remarks,
                        outlet_date_of_established =outletDetails?.outlet_date_of_established,
                        outlet_business_segment =outletDetails?.outlet_business_segment,
                        outlet_primary_business =outletDetails?.outlet_primary_business,
                        outlet_status =outletDetails?.outlet_status)
                    customerProfileBinding.outletDetails=outletDetails
                }

            })
        }

        customerProfileViewModel.updateStatus.observe(this, Observer { status ->
            status?.let {
                if(it=="1"){
                    Toast.makeText(activity , "Outlet details updated Sucessfully" , Toast.LENGTH_LONG).show()

                    val manager: FragmentManager = activity!!.supportFragmentManager
                    val trans: FragmentTransaction = manager.beginTransaction()
                    trans.remove(CustomerProfileDetailsFragment(orderModel))
                    trans.commit()
                    manager.popBackStack()
                } else
                    Toast.makeText(activity , "Error occured: "+it , Toast.LENGTH_LONG).show()
                customerProfileViewModel.updateStatus.postValue(null)
            }
        })



        (getActivity() as HomePageActivity?)?.visibleMenuItems(3)

        val arrayListOutletType: MutableList<OutletType> = ArrayList()
        val o1 = OutletType(1,"Class A")
        val o2 = OutletType(1,"Class B")
        val o3 = OutletType(1,"Class C")
        arrayListOutletType.add(o1)
        arrayListOutletType.add(o2)
        arrayListOutletType.add(o3)

        val arrayListOutletSubType: MutableList<OutletSubType> = ArrayList()
        val oo1 = OutletSubType(1,"Premium Outlet")
        val oo2 = OutletSubType(1,"Mixed Outlet")
        if (arrayListOutletSubType != null) {
            arrayListOutletSubType!!.add(oo1)
            arrayListOutletSubType!!.add(oo2)
        }

        val adapter = ArrayAdapter<OutletType>(this.activity!!, android.R.layout.simple_list_item_1, arrayListOutletType)
        customerProfileBinding.spnOutletType.setAdapter(adapter)
        //val routesPos: Int = routes.indexOf(outletDetails?.outlet_type)


        val adapter1 = ArrayAdapter<OutletSubType>(this.activity!!, android.R.layout.simple_list_item_1, arrayListOutletSubType)
        customerProfileBinding.spnOutletSubType.setAdapter(adapter1)

        //selectSpinnerItemByValue(customerProfileBinding.spnOutletType, outletDetails?.outlet_type,adapter)
        //selectSpinnerItemByValue(customerProfileBinding.spnOutletSubType, outletDetails?.outlet_subtype, adapter)

        return view
    }


    fun selectSpinnerItemByValue(spnr: Spinner, value: String?, adapter: ArrayAdapter<OutletType>) {
        //val adapter = spnr.adapter as ArrayAdapter<String>
        var currentItem: String?
        for (i in 0 until adapter.count) {
            currentItem = adapter.getItem(i)?.outletTypeName
            if (currentItem == value) {
                spnr.setSelection(i)
                return
            }
        }
    }

}
