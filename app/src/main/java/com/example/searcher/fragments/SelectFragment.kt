package com.example.searcher.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.searcher.BuildConfig
import com.example.searcher.R
import com.example.searcher.databinding.FragmentSelectBinding
import com.example.searcher.utils.PERMISSION_REQUEST_CODE
import com.example.searcher.utils.logI
import com.google.android.gms.location.*

class SelectFragment : Fragment() {

    private lateinit var binding: FragmentSelectBinding
    private lateinit var locationClient: FusedLocationProviderClient


    private var searchText: String = ""
    private var range: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioButton = view.findViewById<RadioButton>(binding.menuMiddle.id)
        radioButton?.isChecked = true
        val listener = RadioGroupOnCheckedChangeListener()
        val radioGroup = view.findViewById<RadioGroup>(binding.menuSelector.id)
        radioGroup?.setOnCheckedChangeListener(listener)

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.menuSearch.setOnClickListener {
            searchApi()
        }

    }

    @SuppressLint("MissingPermission")
    private fun searchApi(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
            return
        }
        locationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                logI("LOCATION", "Get location :: $location")
                location?.let {
                    createQuery(location)
                } ?: run {
                    val request = LocationRequest.create().apply {
                        interval = 5000
                        fastestInterval = 3000
                        priority = Priority.PRIORITY_HIGH_ACCURACY
                    }
                    locationClient.requestLocationUpdates(
                        request,
                        object : LocationCallback() {
                            override fun onLocationResult(result: LocationResult) {
                                super.onLocationResult(result)
                                val lastLocation = result.lastLocation
                                lastLocation?.let { createQuery(it) }
                                locationClient.removeLocationUpdates(this)
                            }
                        },
                        Looper.getMainLooper()
                    )
                }
            }
    }

    private fun createQuery(location: Location){
        var query = ""
        query += "key=${BuildConfig.API_KEY}&"
        query += "lat=${location.latitude}&"
        query += "lng=${location.longitude}&"
        if(searchText != ""){ query += "keyword=${searchText.replace("　", " ")}&" }
        query += "range=$range&"
        when(binding.menuEat.isChecked){true -> query += "free_food=1&" else -> {} }
        when(binding.menuDrink.isChecked){true -> query += "free_drink=1&" else -> {} }
        when(binding.menuPrivate.isChecked){true -> query += "private_room=1&" else -> {} }
        when(binding.menuSmoking.isChecked){true -> query += "non_smoking=1&" else -> {} }
        query += "format=json"

        val bundle = bundleOf().apply {
            putString("key", BuildConfig.API_KEY)
            putString("lat", "${location.latitude}")
            putString("lng", "${location.longitude}")
            if(searchText != ""){ putString("keyword", searchText.replace("　", " ")) }
            putString("range", "$range")
            when(binding.menuEat.isChecked){true -> putString("free_food", "1") else -> {} }
            when(binding.menuDrink.isChecked){true -> putString("free_drink", "1") else -> {} }
            when(binding.menuPrivate.isChecked){true -> putString("private_room", "1") else -> {} }
            when(binding.menuSmoking.isChecked){true -> putString("non_smoking", "1") else -> {} }
            putString("format", "json")
        }
        findNavController().navigate(R.id.action_select_fragment_to_result_fragment, bundle)
    }


    private inner class RadioGroupOnCheckedChangeListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(radioGroup: RadioGroup?, checkedRadioButtonId: Int) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            when (checkedRadioButtonId){
                R.id.menu_short  -> range = 1
                R.id.menu_middle -> range = 3
                R.id.menu_large  -> range = 4
            }
        }
    }

    private fun requestPermission() {
        //permission
        val permissions : Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSION_REQUEST_CODE)
    }
}