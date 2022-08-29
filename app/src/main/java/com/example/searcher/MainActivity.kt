package com.example.searcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.example.searcher.databinding.ActivityMainBinding
import com.example.searcher.utils.KeyboardUtils

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