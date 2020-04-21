package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentNewOrderBinding
import com.example.mvvmkotlinapp.model.NewOrderModel
import com.example.mvvmkotlinapp.repository.room.Distributor
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.DistListAdapter
import com.example.mvvmkotlinapp.view.adapter.OutletListAdapter
import com.example.mvvmkotlinapp.view.adapter.RouteListAdapter
import com.example.mvvmkotlinapp.view.fragmets.CityListFragment
import com.example.mvvmkotlinapp.viewmodel.NewOrderViewModel

/**
 * A simple [Fragment] subclass.
 */
class NewOrderFragment : Fragment() {

    lateinit var newOrderViewModel: NewOrderViewModel
    lateinit var newOrderDataBinding : FragmentNewOrderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        newOrderViewModel = ViewModelProviders.of(this).get(NewOrderViewModel::class.java)
        newOrderDataBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_new_order, container, false)
        val view: View = newOrderDataBinding.getRoot()
        newOrderDataBinding.lifecycleOwner = this
        newOrderDataBinding.newOrderViewModel=newOrderViewModel

        var newOrderModel = NewOrderModel(
            routeName = newOrderDataBinding.autoCompleteRouteName.text.toString(),
           outletName =  newOrderDataBinding.autoCompleteOutletName.text.toString(),
           distributorName =  newOrderDataBinding.autoCompleteDistName.text.toString(), isCheck=newOrderDataBinding.chkNewLeadOrder.isChecked)
        newOrderDataBinding.order=newOrderModel

        (getActivity() as HomePageActivity?)?.visibleMenuItems(4)
        newOrderViewModel!!.anotherClass(this)


        activity?.let {
            newOrderViewModel?.getRouteList()?.observe(it, Observer<List<Route>> {
                this.setRouteListAdapter(it)
            })
        }

        activity?.let {
            newOrderViewModel?.getOutletList()?.observe(it, Observer<List<Outlet>> {
                Log.e("Outlet List ",""+ it!!.size)
                if (it != null) {
                    val adapter = OutletListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
                    newOrderDataBinding.autoCompleteOutletName.setAdapter(adapter)
                    newOrderDataBinding.autoCompleteOutletName.threshold = 2
                }
            })
        }

        activity?.let {
            newOrderViewModel?.getDistList()?.observe(it, Observer<List<Distributor>> {
                Log.e("Dist List ",""+ it!!.size)
                if (it != null) {
                    val adapter = DistListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
                    newOrderDataBinding.autoCompleteDistName.setAdapter(adapter)
                    newOrderDataBinding.autoCompleteDistName.threshold = 2
                }
            })
        }


        //Listners
        newOrderDataBinding.autoCompleteRouteName.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as Route?
            newOrderDataBinding.autoCompleteRouteName.setText(selectedPoi?.route_name)
            newOrderDataBinding.autoCompleteOutletName.isEnabled=true
        }

        newOrderDataBinding.autoCompleteOutletName.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as Outlet?
            newOrderDataBinding.autoCompleteOutletName.setText(selectedPoi?.outlet_name)
        }

        newOrderDataBinding.autoCompleteDistName.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as Distributor?
            newOrderDataBinding.autoCompleteDistName.setText(selectedPoi?.dist_name)
        }

        return view
    }

    private fun setRouteListAdapter(it: List<Route>?) {
        Log.e("Route List ",""+ it!!.size)
        if (it != null) {
            val adapter = RouteListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
            newOrderDataBinding.autoCompleteRouteName.setAdapter(adapter)
            newOrderDataBinding.autoCompleteRouteName.threshold = 2
        }
    }

    fun loadFragment(fragment: Fragment){

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()

    }


}
