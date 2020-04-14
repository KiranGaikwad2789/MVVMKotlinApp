package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.view.fragmets.CityListFragment
import java.util.*


class CityListAdapter(ctx: Context, private var arrayListCity: List<City>?) :
    RecyclerView.Adapter<CityListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<City>

    private var lastSelectedPosition = -1

    init {
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<City>()
        this.arraylist.addAll(CityListFragment.cityArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListAdapter.MyViewHolder {

        val view = inflater.inflate(R.layout.dialog_city_list, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityListAdapter.MyViewHolder, position: Int) {

        holder.txtCityName.setText(this!!.arrayListCity!![position]?.cityName)

        holder.radioCitySelect!!.setChecked(lastSelectedPosition == position);

        holder.radioCitySelect!!.setOnClickListener(View.OnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
        })

    }

    override fun getItemCount(): Int {
        return arrayListCity!!.size
    }

    fun filterList(filterdNames: List<City>) {
        this.arrayListCity = filterdNames
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtCityName: TextView
        var radioCitySelect: RadioButton? = null

        init {

            txtCityName = itemView.findViewById(R.id.txtCityName) as TextView
            radioCitySelect = itemView.findViewById(R.id.radioCitySelect) as RadioButton

        }

    }
}