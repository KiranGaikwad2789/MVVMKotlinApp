package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.DateTime
import com.example.mvvmkotlinapp.databinding.FragmentOrderDeliveryDetailsBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.MasterProductOrder
import com.example.mvvmkotlinapp.utils.AlertDialog
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OrderedProductsListAdapter
import com.example.mvvmkotlinapp.viewmodel.OrderDeliveryViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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
    private var currentDate: DateTime? =null
    private var arryListProducts: ArrayList<ProductOrderModel>? =null

    private var adapter: OrderedProductsListAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        orderDeliveryViewModel = ViewModelProviders.of(this).get(OrderDeliveryViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_order_delivery_details, container, false)
        val view: View = binding.getRoot()
        binding.lifecycleOwner = this
        binding.orderDelivery=orderDeliveryViewModel

        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (getActivity() as HomePageActivity?)?.visibleMenuItems(5)
        currentDate= DateTime()
        arryListProducts=ArrayList<ProductOrderModel>()
        Log.e("masterProductOrderPOJO: ",""+masterProductOrderPOJO)

        setUIData()

        activity?.let {
            orderDeliveryViewModel.getOrderedProductList(masterProductOrder.uid)?.observe(it, Observer<List<ProductOrderModel>> {
                arryListProducts?.addAll(it)
                setDataToAdapter(it)
            })
        }

        binding.txtCompleteDeliverOrder.setOnClickListener {
            showOrderDeliveredDateDialog("Deliver")
        }

        binding.txtShortDeliverOrder.setOnClickListener {

            if (masterProductOrderPOJO?.order_delivered_quantity ==0){
                showOrderDeliveredDateDialog("ShortClose")
            }else{

                for (productCart in arryListProducts!!){

                    if (productCart.product_quantity!! > 1){

                        if(productCart.product_quantity==productCart.product_delivered_quantity){
                            context?.let { AlertDialog.basicAlert(it, context!!.getString(R.string.shortdelivery_title),context!!.getString(R.string.shortdelivery_message),"1") }
                            break
                        }else{
                            showOrderDeliveredDateDialog("ConfirmShortClose")
                        }
                    }
                }


                //context?.let { AlertDialog.basicAlert(it, context!!.getString(R.string.shortdelivery_title),context!!.getString(R.string.shortdelivery_message),"1") }
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        binding.txtOrderDateTime.text= currentDate!!.getSelectedDateForamt(masterProductOrderPOJO!!.order_date!!).toString()
        binding.txtmasterOrderId.text="Order Id: "+ masterProductOrderPOJO!!.uid.toString()
        binding.txtMasterOrderTotal.text= masterProductOrderPOJO!!.order_total_price.toString()
        binding.txtMasterOrderProducts.text= masterProductOrderPOJO!!.order_total_quantity.toString()

        if(masterProductOrderPOJO!!.order_status.equals("Deliver") || masterProductOrderPOJO!!.order_status.equals("ShortClose")){
            binding.linearDeliveredDate.visibility=View.VISIBLE
            binding.llDeliveredOrderTotal.visibility=View.VISIBLE
            binding.txtDeliveredOrderTotal.setText(masterProductOrderPOJO!!.order_total_delivered_price.toString())
            binding.txtOrderDeliveredDateTime.setText(currentDate!!.getSelectedDateForamt(masterProductOrderPOJO!!.order_deliver_date!!).toString())
            binding.txtOrderDeliveredQuantity.setText(masterProductOrderPOJO!!.order_delivered_quantity.toString())
            binding.linearLayoutOrder.visibility=View.GONE
        }
    }

    private fun setDataToAdapter(arryListCity: List<ProductOrderModel>) {

        adapter = activity?.let { OrderedProductsListAdapter(it, arryListCity, masterProductOrderPOJO?.order_status,masterProductOrderPOJO?.order_deliver_date) }
        //binding.recyclerProductCartList.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.recyclerProductCartList.adapter = adapter
        binding.recyclerProductCartList.layoutManager = LinearLayoutManager(activity)
        binding.recyclerProductCartList.setNestedScrollingEnabled(false);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showOrderDeliveredDateDialog(orderStatus:String) {

        val dialog = activity?.let { Dialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_order_deliver_date)

        val edtOrderDeliveredDate = dialog?.findViewById(R.id.edtOrderDeliveredDate) as TextInputEditText
        val txtUpdateDeliveredDate = dialog?.findViewById(R.id.txtUpdateDeliveredDate) as TextView
        val txtCancelDialog = dialog?.findViewById(R.id.txtCancelDialog) as TextView

        edtOrderDeliveredDate.setText(SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()))

        var cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var selectedDate:String = sdf.format(cal.time)
            edtOrderDeliveredDate.setText(selectedDate)
            view.maxDate=cal.timeInMillis
        }
        edtOrderDeliveredDate.setOnClickListener {
            activity?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        txtUpdateDeliveredDate.setOnClickListener {

            val sdf = SimpleDateFormat("HH:mm:ss")
            val currentDate = sdf.format(Date())
            var delivereDate=edtOrderDeliveredDate.text.toString()+" "+currentDate

            var deliveryStatus: String? =null
            var order_total_quantity: Int? = masterProductOrder.order_total_quantity
            var order_total_price: Double? = masterProductOrder.order_total_price

            if(orderStatus.equals("Deliver")){
                deliveryStatus="Deliver"
             }else if (orderStatus.equals("ShortClose")){
                deliveryStatus="Pending,ShortClose"
             }else if (orderStatus.equals("ConfirmShortClose")){
                deliveryStatus="ShortClose"
                order_total_quantity = masterProductOrder.order_delivered_quantity
                order_total_price = masterProductOrder.order_total_delivered_price
            }

            activity?.let {
                orderDeliveryViewModel?.updateMasterProductOrderToDeliver(masterProductOrder.uid, delivereDate, deliveryStatus!!,order_total_quantity,order_total_price)?.observe(it, Observer<Int?> {

                    if(orderStatus.equals("Deliver")){
                        orderDeliveryViewModel.updateShortCloseDeliveredQuantity(this!!.arryListProducts!!)
                        adapter?.notifyDataSetChanged()
                        effect()
                    }else if (orderStatus.equals("ShortClose")){
                        orderDeliveryViewModel.updateShortCloseDeliveredQuantity(this!!.arryListProducts!!)
                        adapter?.notifyDataSetChanged()
                    }else if (orderStatus.equals("ConfirmShortClose")){
                        binding.linearLayoutOrder.visibility=View.GONE
                        binding.txtOrderPlacedMessage.visibility=View.VISIBLE
                        effect()
                    }
                })
                orderDeliveryViewModel.resultMasterProductOrder.value = null
                dialog.dismiss()
            }
        }
        txtCancelDialog.setOnClickListener { dialog .dismiss() }
        dialog?.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun effect(){

        if(masterProductOrderPOJO!!.order_status.equals("Deliver") || masterProductOrderPOJO!!.order_status.equals("ShortClose")){

            binding.linearDeliveredDate.visibility=View.VISIBLE
            binding.llDeliveredOrderTotal.visibility=View.VISIBLE
            binding.linearLayoutOrder.visibility=View.GONE
            binding.txtOrderPlacedMessage.visibility=View.VISIBLE

            binding.txtDeliveredOrderTotal.setText(masterProductOrderPOJO!!.order_total_delivered_price.toString())
            binding.txtOrderDeliveredDateTime.setText(currentDate!!.getSelectedDateForamt(masterProductOrderPOJO!!.order_deliver_date!!).toString())
            binding.txtOrderDeliveredQuantity.setText(masterProductOrderPOJO!!.order_delivered_quantity.toString())

        }

      var anim: ObjectAnimator= ObjectAnimator.ofInt(binding.txtOrderPlacedMessage, "backgroundColor", Color.LTGRAY, Color.RED, Color.LTGRAY);
      anim.setDuration(1500);
      anim.setEvaluator(ArgbEvaluator());
      anim.setRepeatMode(ValueAnimator.REVERSE);
      anim.setRepeatCount(Animation.INFINITE);
      anim.start();
    }

}
