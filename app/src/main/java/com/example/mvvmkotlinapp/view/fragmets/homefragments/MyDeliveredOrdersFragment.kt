package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentMyDeliveredOrdersBinding
import com.example.mvvmkotlinapp.databinding.FragmentOrderDeliveryBinding
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OrderDeliveryListAdapter
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel


class MyDeliveredOrdersFragment : Fragment() {

    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel
    lateinit var binding: FragmentMyDeliveredOrdersBinding
    private var adapter: OrderDeliveryListAdapter? = null
    private var masterProductOrderPOJO: MasterProductOrder? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        orderDeliveryViewModel = ViewModelProviders.of(this).get(OrderDeliveryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_my_delivered_orders, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.orderDelivery=orderDeliveryViewModel

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (getActivity() as HomePageActivity?)?.visibleMenuItems(5)
        // Inflate the layout for this fragment

        activity?.let {
            orderDeliveryViewModel?.getOrderDeliveryList("Deliver")?.observe(it, Observer<List<MasterProductOrder>> {
                Log.e("order list ",""+it.size)
                setDataToAdapter(it)
            })
        }

        return view
    }

    private fun setDataToAdapter(arryListOrders: List<MasterProductOrder>?) {

        adapter = activity?.let { OrderDeliveryListAdapter(it,arryListOrders,orderDeliveryViewModel) }!!
        //binding.recyclerViewOrderDeliveryList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.recyclerViewOrderDeliveryList!!.adapter = adapter
        binding.recyclerViewOrderDeliveryList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewOrderDeliveryList.setNestedScrollingEnabled(false)


        activity?.let {
            RecyclerItemClickListenr(it, binding.recyclerViewOrderDeliveryList, object : RecyclerItemClickListenr.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    masterProductOrderPOJO= arryListOrders!!.get(position);
                    navigateToNextFragment(masterProductOrderPOJO!!)
                }

                override fun onItemLongClick(view: View?, position: Int) {
                    TODO("do nothing")
                }
            })
        }?.let {
            binding.recyclerViewOrderDeliveryList.addOnItemTouchListener(
                it
            )
        }
    }

    private fun navigateToNextFragment(masterProductOrder: MasterProductOrder) {

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, OrderDeliveryDetailsFragment(masterProductOrder),"OrderDeliveryDetailsFragment")
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
