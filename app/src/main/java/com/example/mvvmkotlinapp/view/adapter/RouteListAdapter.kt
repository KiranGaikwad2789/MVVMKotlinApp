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
import com.example.mvvmkotlinapp.repository.room.Route

class RouteListAdapter (context: Context, @LayoutRes private val layoutResource: Int, private val allPois: List<Route>):
    ArrayAdapter<Route>(context, layoutResource, allPois),
    Filterable {
    private var mPois: List<Route> = allPois

    override fun getCount(): Int {
        return mPois.size
    }

    override fun getItem(p0: Int): Route? {
        return mPois.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        // Or just return p0
        return mPois.get(p0).route_id!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context).inflate(layoutResource, parent, false) as TextView
        view.text = "${mPois[position].route_name} | ${mPois[position].route_id} "
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {
                mPois = filterResults.values as List<Route>
                notifyDataSetChanged()
            }

            override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
                val queryString = charSequence?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allPois
                else
                    allPois.filter {
                        it.route_name!!.toLowerCase().contains(queryString) ||
                                it.route_id!!.equals(queryString)
                    }
                return filterResults
            }
        }
    }
}