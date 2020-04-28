package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentOrderDeliveryDetailsBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OrderedProductsListAdapter
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel


/**
 * A simple [Fragment] subclass.
 */
class OrderDeliveryDetailsFragment(var masterProductOrder: MasterProductOrder) : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        masterProductOrderPOJO=masterProductOrder
    }


    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel
    lateinit var binding: FragmentOrderDeliveryDetailsBinding
    private var masterProductOrderPOJO: MasterProductOrder? = null

    private var adapter: OrderedProductsListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        orderDeliveryViewModel = ViewModelProviders.of(this).get(OrderDeliveryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_order_delivery_details, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.orderDelivery=orderDeliveryViewModel

        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (getActivity() as HomePageActivity?)?.visibleMenuItems(5)
        Log.e("masterProductOrderPO: ",""+masterProductOrderPOJO)

        setUIData()

        activity?.let {
            orderDeliveryViewModel.getOrderedProductList(masterProductOrder.uid)?.observe(it, Observer<List<ProductOrderModel>> {
                Log.e("Ordered Product cart list ",""+it.size)
                setDataToAdapter(it)
            })
        }

        binding.txtCompleteDeliverOrder.setOnClickListener {

        }

        return view
    }

    private fun setUIData() {

        orderDeliveryViewModel.getOutletNameFromID(masterProductOrderPOJO?.outlet_id.toString())!!.observe((context as FragmentActivity),
            Observer<String>{
                binding.txtOrderoutletId.text= it
        })
        orderDeliveryViewModel.getDistributorNameFromID(masterProductOrderPOJO?.dist_id.toString())!!.observe((context as FragmentActivity),Observer<String>{
            binding.txtOrderDistributortId.text= it
        })


        binding.txtTotalPrice.text= masterProductOrderPOJO!!.order_total_price.toString()
        binding.txtTotalProductCount.text= masterProductOrderPOJO!!.order_total_quantity.toString()
        binding.txtOrderStatus.text= masterProductOrderPOJO!!.order_status
        binding.txtOrderDateTime.text= masterProductOrderPOJO!!.order_date
        binding.txtmasterOrderId.text="Order Id: "+ masterProductOrderPOJO!!.uid.toString()
        binding.txtMasterOrderTotal.text= masterProductOrderPOJO!!.order_total_price.toString()
        binding.txtMasterOrderProducts.text= masterProductOrderPOJO!!.order_total_quantity.toString()
    }

    private fun setDataToAdapter(arryListCity: List<ProductOrderModel>) {

        adapter = activity?.let { OrderedProductsListAdapter(it, arryListCity) }
        binding.recyclerProductCartList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.recyclerProductCartList.adapter = adapter
        binding.recyclerProductCartList.layoutManager = LinearLayoutManager(activity)
        binding.recyclerProductCartList.setNestedScrollingEnabled(false);
    }

}
