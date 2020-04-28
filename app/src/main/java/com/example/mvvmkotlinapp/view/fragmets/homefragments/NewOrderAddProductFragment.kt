package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.app.Dialog
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.FragmentNewOrderAddProductBinding

import com.example.mvvmkotlinapp.model.ProductOrderModel
import com.example.mvvmkotlinapp.repository.room.tables.Product
import com.example.mvvmkotlinapp.view.adapter.NewOrderProductListAdapter
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import com.google.android.material.textfield.TextInputEditText


class NewOrderAddProductFragment : Fragment() {

    companion object {
        var arryListproductSelected: ArrayList<ProductOrderModel>? = null
    }

    lateinit var productListViewModel: ProductListViewModel
    lateinit var bindingAddProduct: FragmentNewOrderAddProductBinding
    private var adapter: NewOrderProductListAdapter? = null
    var productPOJO: Product? =null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        bindingAddProduct = DataBindingUtil.inflate(inflater!!, R.layout.fragment_new_order_add_product, container, false)
        val view: View = bindingAddProduct.getRoot()
        bindingAddProduct.lifecycleOwner = this
        bindingAddProduct.productList=productListViewModel
        arryListproductSelected= ArrayList<ProductOrderModel>()

        val viewProductNav = activity!!.findViewById<View>(R.id.navigationProduct)
        viewProductNav.visibility=View.GONE

        activity?.let {
            productListViewModel?.getProductList()?.observe(it, Observer<List<Product>> {
                Log.e("Product list ",""+it.size)
                this.setDataToAdapter(it)
            })
        }

        bindingAddProduct.btnAddProductToCart.setOnClickListener { addProductToCart() }

        return view
    }

    private fun addProductToCart() {
        Log.e("Product add to cart: ",""+ arryListproductSelected)
        //Insert data to tables
        productListViewModel?.deleteProductTable()
        arryListproductSelected?.let { productListViewModel?.insertSelectedProducts(it) }

        onDestroyView()
        onDestroy()
        onDetach()
    }

    private fun setDataToAdapter(arryListCity: List<Product>) {

        adapter = activity?.let { NewOrderProductListAdapter(it, arryListCity) }
        bindingAddProduct.recyclerViewProductCatList.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
        bindingAddProduct.recyclerViewProductCatList!!.adapter = adapter
        bindingAddProduct.recyclerViewProductCatList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        bindingAddProduct.recyclerViewProductCatList.setNestedScrollingEnabled(false);






        activity?.let {
            RecyclerItemClickListenr(it, bindingAddProduct.recyclerViewProductCatList, object : RecyclerItemClickListenr.OnItemClickListener {

                override fun onItemClick(view: View, position: Int) {

                    productPOJO= arryListCity!!.get(position);
                    Toast.makeText(activity,""+ productPOJO!!.product_name, Toast.LENGTH_SHORT).show()
                    showProductQuantityDialog(productPOJO)
                }

                override fun onItemLongClick(view: View?, position: Int) {
                    TODO("do nothing")
                }
            })
        }?.let {
            bindingAddProduct.recyclerViewProductCatList.addOnItemTouchListener(
                it
            )
        }
    }

    private fun showProductQuantityDialog(productPOJO: Product?) {
        val dialog = activity?.let { Dialog(it, R.style.Theme_Dialog) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_product_quantity)
        val edtProductQuantity = dialog?.findViewById(R.id.edtProductQuantity) as TextInputEditText

        val txtSaveProductQuantity = dialog?.findViewById(R.id.txtSaveProductQuantity) as TextView
        val txtCancelDialog = dialog?.findViewById(R.id.txtCancelDialog) as TextView
        txtSaveProductQuantity.setOnClickListener {
            //dialog?.dismiss()
            selectProduct(productPOJO,dialog,edtProductQuantity.text.toString().toInt())
        }
        txtCancelDialog.setOnClickListener { dialog .dismiss() }
        dialog?.show()
    }

    private fun selectProduct(productPOJO: Product?, dialog: Dialog,productQuantity:Int) {

        var productSelected= ProductOrderModel(0,0,productPOJO?.product_id,
            productPOJO?.product_name,productPOJO?.prod_cat_id,productPOJO?.route_id,productPOJO?.outlet_id,
            productPOJO?.dist_id, productPOJO?.product_price?.toDouble()!!,productQuantity* productPOJO?.product_price?.toDouble()!!,productQuantity.toInt(),productPOJO?.product_compony,productPOJO?.status)
        arryListproductSelected!!.add(productSelected!!)
        Log.e("Product dialog: ",""+ arryListproductSelected!!.size)
        dialog.dismiss()
    }

}
