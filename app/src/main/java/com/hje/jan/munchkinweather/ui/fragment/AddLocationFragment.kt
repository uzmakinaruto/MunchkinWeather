package com.hje.jan.munchkinweather.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hje.jan.munchkinweather.R

class AddLocationFragment : Fragment() {

    companion object {

        fun newInstance(): AddLocationFragment {
            return AddLocationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_locaton, container, false)
    }
}