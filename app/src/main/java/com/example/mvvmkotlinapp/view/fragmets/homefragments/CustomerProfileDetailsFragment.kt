package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        customerProfileViewModel = ViewModelProviders.of(this).get(CustomerProfileViewModel::class.java)
        customerProfileBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_customer_profile_details, container, false)
        val view: View = customerProfileBinding.getRoot()
        customerProfileBinding.lifecycleOwner = this
        customerProfileBinding.customerProfileViewModel=customerProfileViewModel

        var outletValNID= orderModel.outletName?.split(" | ")
        Log.e("outlet name: ",""+ outletValNID!![0])
        Log.e("outlet id: ",""+ outletValNID[1])


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
            customerProfileViewModel?.getOutletDetails(outletValNID[1])?.observe(it, Observer<OutletDetails> {
                Log.e("Outlet details ",""+ it)
                if (it != null) {

                }
            })
        }


        var outletDetails = OutletDetails(
            outlet_details_id = 0,
            outlet_id=outletValNID[1].toInt(),
            outlet_name =outletValNID[0],
            route_id =0,
            dist_id =0,
           /* route_id =customerProfileBinding.edtRouteName.getText().toString().toInt(),
            dist_id =customerProfileBinding.edtDistributorName.getText().toString().toInt(),*/
            outlet_type = customerProfileBinding.spnOutletType.selectedItem as Int?,
            outlet_subtype = customerProfileBinding.spnOutletSubType.selectedItem as Int?,
            outlet_address=customerProfileBinding.edtOutletAddress.text.toString(),
            outlet_owner_name=customerProfileBinding.edtOwnerName.text.toString(),
            outlet_contact_number =customerProfileBinding.edtContactNumber.text.toString(),
            outlet_contact_alt_number =customerProfileBinding.edtContactAltNumber.text.toString(),
            outlet_email_id =customerProfileBinding.edtOutletEmailID.text.toString(),
            outlet_contact_person =customerProfileBinding.edtOutletContactPerson.text.toString(),
            outlet_sizeof_facility =customerProfileBinding.edtOutletSixeOfFacility.text.toString(),
            outlet_GST_number =customerProfileBinding.edtOutletGSTNumber.text.toString(),
            outlet_license_details =customerProfileBinding.edtOutletLicenseDetails.text.toString(),
            outlet_business_monthly =customerProfileBinding.edtAppxBusinessMonthly.text.toString(),
            outlet_day_footprint =customerProfileBinding.edtAppxDayFootprint.text.toString(),
            outlet_competitor_remarks =customerProfileBinding.edtCompetitorRemarks.text.toString(),
            outlet_date_of_established =customerProfileBinding.edtDateOfEstablished.text.toString(),
            outlet_business_segment =customerProfileBinding.edtBussinessSegment.text.toString(),
            outlet_primary_business =customerProfileBinding.edtPrimaryBussiness.text.toString(),
            outlet_status ="1")
        customerProfileBinding.outletDetails=outletDetails

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

        val adapter1 = ArrayAdapter<OutletSubType>(this.activity!!, android.R.layout.simple_list_item_1, arrayListOutletSubType)
        customerProfileBinding.spnOutletSubType.setAdapter(adapter1)

        return view
    }


}
