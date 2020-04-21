package com.example.mvvmkotlinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.mvvmkotlinapp.repository.room.Distributor

class DistListAdapter (context: Context, @LayoutRes private val layoutResource: Int, private val allPois: List<Distributor>):
    ArrayAdapter<Distributor>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<Distributor> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): Distributor? {
        return mPois.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mPois.get(p0).route_id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${mPois[position].dist_name} | ${mPois[position].dist_id} "
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPois = filterResults.values as List<Distributor>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.dist_name!!.toLowerCase().contains(queryString) ||
                                it.dist_id!!.equals(queryString)
                    }
                return filterResults
            }
        }
    }
}