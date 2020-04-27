package com.example.mvvmkotlinapp.view.fragmets.homefragments

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentOrderDeliveryBinding
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OrderDeliveryListAdapter
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel


/**
 * A simple [Fragment] subclass.
 */
class OrderDeliveryFragment : Fragment() {

    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel
    lateinit var binding: FragmentOrderDeliveryBinding
    private var adapter: OrderDeliveryListAdapter? = null
    private var masterProductOrderPOJO: MasterProductOrder? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        orderDeliveryViewModel = ViewModelProviders.of(this).get(OrderDeliveryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_order_delivery, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.orderDelivery=orderDeliveryViewModel

        // Inflate the layout for this fragment
        (getActivity() as HomePageActivity?)?.visibleMenuItems(5)


        activity?.let {
            orderDeliveryViewModel?.getOrderDeliveryList()?.observe(it, Observer<List<MasterProductOrder>> {
                Log.e("order list ",""+it.size)
                setDataToAdapter(it)
            })
        }

        return view
    }

    private fun setDataToAdapter(arryListOrders: List<MasterProductOrder>?) {

        adapter = activity?.let { OrderDeliveryListAdapter(it,arryListOrders,orderDeliveryViewModel) }!!
        binding.recyclerViewOrderDeliveryList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.recyclerViewOrderDeliveryList!!.adapter = adapter
        binding.recyclerViewOrderDeliveryList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewOrderDeliveryList.setNestedScrollingEnabled(false)


        activity?.let {
            RecyclerItemClickListenr(it, binding.recyclerViewOrderDeliveryList, object : RecyclerItemClickListenr.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    masterProductOrderPOJO= arryListOrders!!.get(position);
                    Toast.makeText(activity,""+ masterProductOrderPOJO!!.uid, Toast.LENGTH_SHORT).show()
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

        val ldf = OrderDeliveryDetailsFragment()
        val args = Bundle()
        args.putSerializable("masterProductOrderPOJO", masterProductOrder)
        ldf.setArguments(args)
        //Inflate the fragment
        fragmentManager?.beginTransaction()?.add(R.id.container, ldf)?.commit()
    }

}
