package com.example.searcher.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.searcher.databinding.FragmentDetailBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { createView(bundleToMap(it)) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun createView(map: Map<String, String?>){
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL(map["logo_image"])
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            launch(Dispatchers.Main) {
                binding.mainImage.setImageBitmap(image)
            }
        }
        binding.menuShopName.text = map["name"]
        binding.menuShopAddress.text = map["address"]
        binding.menuShopAccess.text = map["access"]
        binding.menuShopOpen.text = map["open"]
        binding.menuShopClose.text = map["close"]
    }

    private fun bundleToMap(extras: Bundle): MutableMap<String, String?> {
        val map: MutableMap<String, String?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras.getString(key)
        }
        return map
    }

}