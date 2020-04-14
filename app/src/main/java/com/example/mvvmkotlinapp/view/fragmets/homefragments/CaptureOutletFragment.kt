package com.example.mvvmkotlinapp.view.fragmets.homefragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mvvmkotlinapp.R

/**
 * A simple [Fragment] subclass.
 */
class CaptureOutletFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_outlet, container, false)
    }

}
