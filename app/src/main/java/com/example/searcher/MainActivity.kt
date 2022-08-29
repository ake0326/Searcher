package com.example.searcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager
import androidx.navigation.findNavController
import com.example.searcher.databinding.ActivityMainBinding
import com.example.searcher.utils.KeyboardUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val focusView = currentFocus ?: return false
        KeyboardUtils.hideKeyboard(focusView)

        return false
    }

    override fun onBackPressed() {
        findNavController(R.id.nav_host_fragment).run {
            when (currentDestination!!.id) {
                R.id.select_fragment-> {}
                else -> popBackStack()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        findNavController(R.id.nav_host_fragment).run {
            when (currentDestination!!.id) {
                R.id.select_fragment-> {}
                else -> popBackStack()
            }
        }
        return super.onSupportNavigateUp()
    }
}