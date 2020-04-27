package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel

/*class OrderDeliveryListAdapter (private val context: Context, private val arrayListProduct: List<MasterProductOrder>?) : RecyclerView.Adapter<ViewHolder>(){


    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel

    // Inflating Layout and ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        orderDeliveryViewModel = ViewModelProviders.of((context as FragmentActivity)).get(OrderDeliveryViewModel::class.java)
        val binding: RowOrderDeliveryListBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_order_delivery_list, parent, false)
        binding.orderDelivery=orderDeliveryViewModel
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayListProduct?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(arrayListProduct!!.get(position))
        holder.binding.txtOrderStatus.text=arrayListProduct!!.get(position).order_status
        //holder.binding.txtOrderDate.text=arrayListProduct!!.get(position).order_date
        //holder.binding.txtOrderoutletId.text=arrayListProduct!!.get(position).outlet_id.toString()
        //holder.binding.txtOrderdistId.text=arrayListProduct!!.get(position).dist_id.toString()
        //holder.binding.txtOrderuid.text=arrayListProduct!!.get(position).uid.toString()
    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowOrderDeliveryListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MasterProductOrder) {
            binding.setVariable(BR.orderDeliveryProduct, data) //BR - generated class; BR.user - 'user' is variable name declared in layout
            binding.executePendingBindings()
        }
    }
}*/


class OrderDeliveryListAdapter(private val context: Context, private var arrayListProduct: List<MasterProductOrder>?,orderDeliveryViewModel: OrderDeliveryViewModel) :
    RecyclerView.Adapter<OrderDeliveryListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    lateinit var orderDeliveryViewModel: OrderDeliveryViewModel

    init {
        inflater = LayoutInflater.from(context)
        this.orderDeliveryViewModel =orderDeliveryViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.row_order_delivery_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        orderDeliveryViewModel.getOutletNameFromID(arrayListProduct!!.get(position).outlet_id.toString())!!.observe((context as FragmentActivity),Observer<String>{
            holder.txtOrderoutletId.text= it
        })
        orderDeliveryViewModel.getDistributorNameFromID(arrayListProduct!!.get(position).dist_id.toString())!!.observe((context as FragmentActivity),Observer<String>{
            holder.txtOrderdistId.text= it
        })

        holder.txtOrderStatus.text=arrayListProduct!!.get(position).order_status
        holder.txtOrderDate.text=arrayListProduct!!.get(position).order_date
        holder.txtOrderDateTime.text=arrayListProduct!!.get(position).order_date
        holder.txtOrderuid.text="Order Id: "+arrayListProduct!!.get(position).uid.toString()
        holder.txtOrderTotal.text=arrayListProduct!!.get(position).order_total_price.toString()
        holder.txtOrderproducts.text=arrayListProduct!!.get(position).order_total_quantity.toString()
    }

    override fun getItemCount(): Int {
        return arrayListProduct!!.size
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtOrderStatus: TextView
        var txtOrderDate: TextView
        var txtOrderoutletId: TextView
        var txtOrderdistId: TextView
        var txtOrderuid: TextView
        var txtOrderDateTime: TextView
        var txtOrderTotal: TextView
        var txtOrderproducts: TextView

        init {

            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus) as TextView
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate) as TextView
            txtOrderoutletId = itemView.findViewById(R.id.txtOrderoutletId) as TextView
            txtOrderdistId = itemView.findViewById(R.id.txtOrderdistId) as TextView
            txtOrderuid = itemView.findViewById(R.id.txtOrderuid) as TextView
            txtOrderDateTime = itemView.findViewById(R.id.txtOrderDateTime) as TextView
            txtOrderTotal = itemView.findViewById(R.id.txtOrderTotal) as TextView
            txtOrderproducts = itemView.findViewById(R.id.txtOrderproducts) as TextView

        }

    }
}