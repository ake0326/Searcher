package com.example.searcher

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.searcher.models.responses.SearchResponse
import com.example.searcher.network.Retrofit
import com.example.searcher.utils.PERMISSION_REQUEST_CODE
import com.example.searcher.utils.logI
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var locationClient     : FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
        }
    }

    override fun onStart() {
        super.onStart()
    }


    private fun searchApi(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission()
            return
        }
        locationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                logI("LOCATION", "Get location :: $location")
                location?.let {
                    requestApi(location)
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
                                lastLocation?.let { requestApi(it) }
                                locationClient.removeLocationUpdates(this)
                            }
                        },
                        Looper.getMainLooper()
                    )
                }
            }
    }

    private fun requestApi(location : Location){
        Retrofit.instance.getSearch(key = BuildConfig.API_KEY, lat = location.latitude.toString(), lng = location.longitude.toString(), range = 3, format = "json")
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    logI("NETWORK", "Get Search :: ${response.body()?.results?.shop}")
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    logI("NETWORK", "Error Search :: $t")
                }

            })
    }

    private fun requestPermission() {
        //permission
        val permissions : Array<String> = arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }
}