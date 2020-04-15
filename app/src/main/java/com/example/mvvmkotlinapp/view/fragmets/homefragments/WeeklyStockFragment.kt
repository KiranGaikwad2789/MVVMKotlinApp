package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.view.activities.HomePageActivity

/**
 * A simple [Fragment] subclass.
 */
class WeeklyStockFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (getActivity() as HomePageActivity?)?.visibleMenuItems(7)
        return inflater.inflate(R.layout.fragment_weekly_stock, container, false)
    }

}
