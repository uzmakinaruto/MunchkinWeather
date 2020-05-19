package com.hje.jan.munchkinweather.ui.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hje.jan.munchkinweather.R
import kotlinx.android.synthetic.main.fragment_weather.*


class WeatherFragment private constructor() : Fragment() {

    lateinit var name: String

    companion object {
        fun newInstance(bgColor: Int, name: String): WeatherFragment {
            val fragment = WeatherFragment()
            fragment.arguments = Bundle().apply {
                putInt("bg_color", bgColor)
                putString("name", name)
            }
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        name = arguments?.getString("name", null) ?: "null"
        Log.d("MunchkinWeather", "onAttach-${name}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MunchkinWeather", "onCreate-${name}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MunchkinWeather", "onCreateView-${name}")
        val root = inflater.inflate(R.layout.fragment_weather, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MunchkinWeather", "onActivityCreated-${name}")
        tempText.typeface = Typeface.createFromAsset(activity!!.assets, "msyhl.ttc")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MunchkinWeather", "onResume-${name}")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MunchkinWeather", "onPause-${name}")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MunchkinWeather", "onStop-${name}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MunchkinWeather", "onDestroy-${name}")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("MunchkinWeather", "onDetach-${name}")
    }
}