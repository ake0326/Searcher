package com.example.searcher

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import com.example.searcher.databinding.ActivityMainBinding
import com.example.searcher.databinding.FragmentResultBinding
import com.example.searcher.models.responses.SearchResponse
import com.example.searcher.network.Retrofit
import com.example.searcher.utils.KeyboardUtils
import com.example.searcher.utils.PERMISSION_REQUEST_CODE
import com.example.searcher.utils.logI
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val focusView = currentFocus ?: return false
        KeyboardUtils.hideKeyboard(focusView)

        return false
    }
}