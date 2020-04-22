package com.example.mvvmkotlinapp.view.activities

import android.os.Bundle
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
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory
import com.example.mvvmkotlinapp.view.adapter.ProductCatListAdapter
import com.example.mvvmkotlinapp.view.fragmets.homefragments.NewOrderAddProductFragment
import com.example.mvvmkotlinapp.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.app_bar_product_list.*
import kotlinx.android.synthetic.main.nav_header_product_list.view.*
import java.util.*

class ProductListActivity : AppCompatActivity() {


    companion object {
        var array_sort: ArrayList<City>? = null
    }

    lateinit var productListViewModel: ProductListViewModel
    lateinit var activityProductListBinding: ActivityProductSelectBinding
    private var adapter: ProductCatListAdapter? = null
    var productCatArrayList: ArrayList<ProductCategory>? = null
    var productCategory: ProductCategory? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productListViewModel = ViewModelProviders.of(this).get(ProductListViewModel::class.java)
        activityProductListBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_select)
        activityProductListBinding.lifecycleOwner = this
        activityProductListBinding.productList=productListViewModel

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, activityProductListBinding.drawerLayout, toolbar, 0, 0)
        activityProductListBinding.drawerLayout!!.addDrawerListener(toggle)
        toggle.syncState()

        val headerView = activityProductListBinding!!.navView.getHeaderView(0)

        productCatArrayList = ArrayList<ProductCategory>()

        activityProductListBinding.actionBar.fab.setOnClickListener { view ->
            activityProductListBinding.drawerLayout.openDrawer(activityProductListBinding.navView)
        }

        this?.let {
            productListViewModel?.getProductCatList()?.observe(it, Observer<List<ProductCategory>> {
                Log.e("Cate list ",""+it.size)
                this.setDataToAdapter(it)
            })
        }


        /*activityProductListBinding.navView.edtSearchCategory!!.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                textlength = activityProductListBinding.navView.edtSearchCategory!!.text!!.length
                CityListFragment.array_sort?.clear()
                for (i in CityListFragment.cityArrayList!!.indices) {
                    if (textlength <= CityListFragment.cityArrayList!![i].cityName!!.length) {
                        Log.d("ertyyy", CityListFragment.cityArrayList!![i].cityName!!.toLowerCase().trim())
                        if (CityListFragment.cityArrayList!![i].cityName!!.toLowerCase().trim().contains(
                                activityProductListBinding.navView.edtSearchCategory!!.text.toString().toLowerCase().trim { it <= ' ' })
                        ) {
                            CityListFragment.array_sort!!.add(CityListFragment.cityArrayList!![i])
                        }
                    }
                }

                adapter = activity?.let { CityListAdapter(it, CityListFragment.array_sort) }
                cityBinding.recyclerViewCityList!!.adapter = adapter
                cityBinding.recyclerViewCityList!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                val dividerItemDecoration = DividerItemDecoration(cityBinding.recyclerViewCityList.getContext(), LinearLayoutManager.VERTICAL)
                cityBinding.recyclerViewCityList.addItemDecoration(dividerItemDecoration)
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
                Toast.makeText(this@ProductListActivity,""+ productCategory!!.prod_cat_name,Toast.LENGTH_SHORT).show()
                loadFragment(NewOrderAddProductFragment())
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
}
