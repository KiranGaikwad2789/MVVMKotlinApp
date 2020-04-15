package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mvvmkotlinapp.R
import com.example.mvvmkotlinapp.view.activities.HomePageActivity


class CaptureOutletFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (getActivity() as HomePageActivity?)?.visibleMenuItems(1)
        return inflater.inflate(R.layout.fragment_capture_outlet, container, false)
    }

}
