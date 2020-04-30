package com.example.mvvmkotlinapp.view.adapter

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.BR
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.RowOrderedProductsListBinding
import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


class OrderedProductsListAdapter (private val context: Context, private val arrayListProduct: List<ProductOrderModel>?,
                                  private var order_status:String?,private var order_deliver_date:String?) : RecyclerView.Adapter<OrderedProductsListAdapter.ViewHolder>(){

    lateinit var productListViewModel: ProductListViewModel
    lateinit var binding: RowOrderedProductsListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        productListViewModel = ViewModelProviders.of((context as FragmentActivity)).get(ProductListViewModel::class.java)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_ordered_products_list, parent, false)
        binding.productList=productListViewModel
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return arrayListProduct?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(arrayListProduct!!.get(position))

        if (order_status.equals("Pending,ShortClose")){
            binding.imgEditProduct.visibility=View.VISIBLE
        }
        binding.imgEditProduct.setOnClickListener { showOrderDeliveredDateDialog(arrayListProduct!!.get(position)) }
    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowOrderedProductsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductOrderModel) {
            binding.setVariable(BR.orderedProducts, data) //BR - generated class; BR.product - 'product' is variable name declared in layout
            binding.executePendingBindings()
        }
    }


    private fun showOrderDeliveredDateDialog(productPOJO:ProductOrderModel?) {

        val dialog = context?.let { Dialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_order_short_close)

        val edtOrderDeliveredQuantity = dialog?.findViewById(R.id.edtOrderDeliveredQuantity) as EditText
        val edtOrderDeliveredDate = dialog?.findViewById(R.id.edtOrderDeliveredDate) as EditText

        val txtUpdateDeliveredDate = dialog?.findViewById(R.id.txtUpdateDeliveredDate) as TextView
        val txtCancelDialog = dialog?.findViewById(R.id.txtCancelDialog) as TextView

        edtOrderDeliveredQuantity.setText(productPOJO?.product_delivered_quantity.toString())
        edtOrderDeliveredDate.setText(order_deliver_date)

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var selectedDate:String = sdf.format(cal.time)
            edtOrderDeliveredDate.setText(selectedDate)
            view.minDate=cal.timeInMillis
        }

        edtOrderDeliveredDate.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        txtUpdateDeliveredDate.setOnClickListener {

            var deleivedredQuantity: String =edtOrderDeliveredQuantity.text.toString()

            if (deleivedredQuantity.toInt()>= productPOJO?.product_delivered_quantity!!){
                Toast.makeText(context,"Enter valid quantity.",Toast.LENGTH_SHORT).show()
            }else{

                var deleivedredQuantity: String =edtOrderDeliveredQuantity.text.toString()
                productListViewModel.updateShortCloseDeliveredQuantity(productPOJO.uid, deleivedredQuantity.toInt())
                notifyDataSetChanged()
                dialog.dismiss()

            }
        }
        txtCancelDialog.setOnClickListener { dialog .dismiss() }
        dialog?.show()
    }

}