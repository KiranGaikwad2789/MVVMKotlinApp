package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentOrderDeliveryDetailsBinding
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel


/**
 * A simple [Fragment] subclass.
 */
class OrderDeliveryDetailsFragment : Fragment() {


    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel
    lateinit var binding: FragmentOrderDeliveryDetailsBinding
    private var masterProductOrderPOJO: MasterProductOrder? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        orderDeliveryViewModel = ViewModelProviders.of(this).get(OrderDeliveryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_order_delivery_details, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.orderDelivery=orderDeliveryViewModel

        // Inflate the layout for this fragment
        (getActivity() as HomePageActivity?)?.visibleMenuItems(5)

        masterProductOrderPOJO = arguments!!.getSerializable("masterProductOrderPOJO") as MasterProductOrder?
        Log.e("masterProductOrderPOJO details: ",""+masterProductOrderPOJO)

        return view
    }

}
