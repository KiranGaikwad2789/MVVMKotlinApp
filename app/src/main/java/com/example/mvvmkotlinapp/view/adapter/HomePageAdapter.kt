package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.BR
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.databinding.RowFeaturesMenuBinding
import com.example.mvvmkotlinapp.repository.room.Features
import com.example.mvvmkotlinapp.view.activities.HomePageActivity
import com.example.mvvmkotlinapp.view.adapter.HomePageAdapter.ViewHolder
import com.example.mvvmkotlinapp.viewmodel.FeatureViewModel


class HomePageAdapter (private val context: Context, private val features: List<Features>?) : RecyclerView.Adapter<ViewHolder>(){

    //https://cdn.journaldev.com/wp-content/uploads/2018/11/android-reyclerview-databinding-output.gif

    var featureViewModel: FeatureViewModel? =null
    // Inflating Layout and ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        featureViewModel = ViewModelProviders.of((context as FragmentActivity)).get(FeatureViewModel::class.java)
        val binding: RowFeaturesMenuBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_features_menu, parent, false)
        binding.featuresModel=featureViewModel
        featureViewModel!!.anotherClass(context as HomePageActivity)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return features?.size!!
    }

    // Bind data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(features!!.get(position))
    }

    // Creating ViewHolder
    class ViewHolder(val binding: RowFeaturesMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Features) {
            binding.setVariable(BR.features, data) //BR - generated class; BR.user - 'user' is variable name declared in layout
            binding.executePendingBindings()
        }
    }

    fun loadFragment(fragment: Fragment){
        val transaction = (context as HomePageActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}