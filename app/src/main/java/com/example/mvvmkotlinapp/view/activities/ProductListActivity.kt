package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.common.RecyclerItemClickListenr
import com.example.mvvmkotlinapp.databinding.ActivityProductSelectBinding
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory
import com.example.mvvmkotlinapp.utils.ModelPreferencesManager
import com.example.mvvmkotlinapp.view.adapter.ProductCatListAdapter
import com.example.mvvmkotlinapp.view.fragmets.homefragments.NewOrderAddProductFragment
import com.example.mvvmkotlinapp.view.fragmets.homefragments.ProductCartFragment
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.app_bar_product_list.*
import kotlinx.android.synthetic.main.nav_header_product_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class ProductListActivity : AppCompatActivity() {

    companion object {
        var cityArrayList: List<ProductCategory>? = ArrayList<ProductCategory>()
        var array_sort: ArrayList<ProductCategory>? = ArrayList<ProductCategory>()
    }
    internal var textlength = 0
    lateinit var productListViewModel: ProductListViewModel
    lateinit var activityProductListBinding: ActivityProductSelectBinding
    private var adapter: ProductCatListAdapter? = null
    var productCatArrayList: ArrayList<ProductCategory>? = null
    var productCategory: ProductCategory? =null
    var orderModel: NewOrderModel? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        activityProductListBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_select)
        activityProductListBinding.lifecycleOwner = this
        activityProductListBinding.productList=productListViewModel

        setSupportActionBar(toolbar)

        activityProductListBinding!!.navigationProduct!!.isItemHorizontalTranslationEnabled = true
        activityProductListBinding!!.navigationProduct.labelVisibilityMode= LabelVisibilityMode.LABEL_VISIBILITY_LABELED


        val toggle = ActionBarDrawerToggle(this, activityProductListBinding.drawerLayout, toolbar, 0, 0)
        activityProductListBinding.drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        activityProductListBinding.navigationProduct.visibility=View.VISIBLE

        productCatArrayList = ArrayList<ProductCategory>()
        orderModel = ModelPreferencesManager.get<NewOrderModel>("orderModel")

        var delimiter = "| "
        val routeNameSplit = orderModel?.routeName?.split(delimiter)
        val outletNameSplit = orderModel?.outletName?.split(delimiter)
        val distNameSplit = orderModel?.distributorName?.split(delimiter)


        activityProductListBinding.actionBar.fab.setOnClickListener { view ->
            activityProductListBinding.drawerLayout.openDrawer(activityProductListBinding.navView)
        }

        this?.let {
            productListViewModel?.getProductCatList(routeNameSplit?.get(1)?.toInt(),
                outletNameSplit?.get(1)?.toInt(),
                distNameSplit?.get(1)?.toInt())?.observe(it, Observer<List<ProductCategory>> {
                this.setDataToAdapter(it)
            })
        }

        loadFragment(ProductCartFragment())

        /*activityProductListBinding.navView.edtSearchCategory!!.addTextChangedListener(object :
            TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = activityProductListBinding.navView.edtSearchCategory!!.text!!.length
                array_sort?.clear()
                for (i in cityArrayList!!.indices) {
                    if (textlength <= cityArrayList!![i].prod_cat_name!!.length) {
                        Log.d("ertyyy", cityArrayList!![i].prod_cat_name!!.toLowerCase().trim())
                        if (cityArrayList!![i].prod_cat_name!!.toLowerCase().trim().contains(
                                activityProductListBinding.navView.edtSearchCategory!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            array_sort!!.add(cityArrayList!![i])
                        }
                    }
                }

                array_sort?.let { setDataToAdapter(it) }
            }
        })*/

    }

    private fun setDataToAdapter(arryListCity: List<ProductCategory>) {


        productCatArrayList!!.addAll(arryListCity)

        adapter = ProductCatListAdapter(this, productCatArrayList)
        activityProductListBinding.navView.recyclerViewProductCatList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        activityProductListBinding.navView.recyclerViewProductCatList!!.adapter = adapter
        activityProductListBinding.navView.recyclerViewProductCatList!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        activityProductListBinding.navView.recyclerViewProductCatList.addOnItemTouchListener(RecyclerItemClickListenr(this, activityProductListBinding.navView.recyclerViewProductCatList, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {

                productCategory= productCatArrayList!!.get(position);
                Toast.makeText(this@ProductListActivity,""+ productCategory!!.prod_cat_id,Toast.LENGTH_SHORT).show()
                loadFragment(NewOrderAddProductFragment(productCategory,orderModel))
            }
            override fun onItemLongClick(view: View?, position: Int) {
                TODO("do nothing")
            }
        }))
    }


    fun loadFragment(fragment: Fragment){

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment,fragment.javaClass.simpleName)
        //transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.addToBackStack(null)
        transaction.commit()

        if (activityProductListBinding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            activityProductListBinding!!.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    override fun onBackPressed() {

        val myFragment: Fragment = supportFragmentManager.findFragmentByTag("ProductCartFragment")!!

        if (myFragment != null && myFragment.isVisible()) { // add your code here
            finish()
        }else{
            loadFragment(ProductCartFragment())
        }
    }
}
