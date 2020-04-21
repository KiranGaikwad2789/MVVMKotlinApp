package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.FragmentCaptureOutletBinding
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.repository.room.Outlet
import com.example.mvvmkotlinapp.repository.room.Route
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.OutletListAdapter
import com.example.mvvmkotlinapp.view.adapter.RouteListAdapter
import com.example.mvvmkotlinapp.viewmodel.CaptureOutletViewModel


class CaptureOutletFragment : Fragment() {

    lateinit var captureOutletViewModel: CaptureOutletViewModel
    lateinit var captureOutletBinding : FragmentCaptureOutletBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        captureOutletViewModel = ViewModelProviders.of(this).get(CaptureOutletViewModel::class.java)
        captureOutletBinding = DataBindingUtil.inflate(inflater!!, R.layout.fragment_capture_outlet, container, false)
        val view: View = captureOutletBinding.getRoot()
        captureOutletBinding.lifecycleOwner = this
        captureOutletBinding.captureOutletViewModel=captureOutletViewModel

        (getActivity() as HomePageActivity?)?.visibleMenuItems(1)

        activity?.let {
            captureOutletViewModel?.getRouteList()?.observe(it, Observer<List<Route>> {
                this.setRouteListAdapter(it)
            })
        }

        activity?.let {
            captureOutletViewModel?.getOutletList()?.observe(it, Observer<List<Outlet>> {
                Log.e("Outlet List ",""+ it!!.size)
                if (it != null) {
                    val adapter = OutletListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
                    captureOutletBinding.autoCompleteOutletName.setAdapter(adapter)
                    captureOutletBinding.autoCompleteOutletName.threshold = 2
                }
            })
        }

        captureOutletBinding.autoCompleteRouteName.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as Route?
            captureOutletBinding.autoCompleteRouteName.setText(selectedPoi?.route_name)
            captureOutletBinding.autoCompleteOutletName.isEnabled=true
        }

        captureOutletBinding.autoCompleteOutletName.setOnItemClickListener() { parent, _, position, id ->
            val selectedPoi = parent.adapter.getItem(position) as Outlet?
            captureOutletBinding.autoCompleteOutletName.setText(selectedPoi?.outlet_name)
        }

        return view
    }

    private fun setRouteListAdapter(it: List<Route>?) {
        Log.e("Route List ",""+ it!!.size)
            if (it != null) {
                val adapter = RouteListAdapter(this!!.activity!!, android.R.layout.simple_list_item_1, it)
                captureOutletBinding.autoCompleteRouteName.setAdapter(adapter)
                captureOutletBinding.autoCompleteRouteName.threshold = 2
            }
    }

}
