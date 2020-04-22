package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.repository.room.City
import com.example.mvvmkotlinapp.repository.room.tables.ProductCategory
import java.util.ArrayList

class ProductCatListAdapter (ctx: Context, private var arrayListProductCat: List<ProductCategory>?) :
    RecyclerView.Adapter<ProductCatListAdapter.MyViewHolder>() {

    private val inflater: LayoutInflater
    private val arraylist: ArrayList<City>

    private var lastSelectedPosition = -1

    init {
        inflater = LayoutInflater.from(ctx)
        this.arraylist = ArrayList<City>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = inflater.inflate(R.layout.dialog_city_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.txtCityName.setText(this!!.arrayListProductCat!![position]?.prod_cat_name)
        holder.radioCitySelect!!.setChecked(lastSelectedPosition == position);

       /* holder.radioCitySelect!!.setOnClickListener(View.OnClickListener {
            lastSelectedPosition = position
            notifyDataSetChanged()
        })*/

    }

    override fun getItemCount(): Int {
        return arrayListProductCat!!.size
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