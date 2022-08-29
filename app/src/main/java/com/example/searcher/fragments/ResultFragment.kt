package com.example.searcher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.searcher.BuildConfig
import com.example.searcher.R
import com.example.searcher.databinding.FragmentResultBinding
import com.example.searcher.models.adapters.ResultAdapter
import com.example.searcher.models.items.Shop
import com.example.searcher.models.responses.SearchResponse
import com.example.searcher.network.Retrofit
import com.example.searcher.utils.logI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { requestApi(bundleToMap(it)) }
    }

    private fun requestApi(query: Map<String,String?>){
        Retrofit.instance.getSearch(query)
            .enqueue(object : Callback<SearchResponse>, AdapterView.OnItemClickListener {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    logI("NETWORK", "Get Search :: ${response.body()?.results?.shop}")

                    val adapter = response.body()?.let { ResultAdapter(requireContext(), it.results.shop) }
                    binding.listView.adapter = adapter
                    binding.listView.onItemClickListener = this
                    logI("NETWORK", "Create")
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    logI("NETWORK", "Error Search :: $t")
                }

                override fun onItemClick(root: AdapterView<*>?, view: View?, pos: Int, pos1: Long) {
                    val shop = binding.listView.adapter.getItem(pos) as Shop
                    val bundle = bundleOf().apply {
                        shop.let {
                            putString("name", it.name)
                            putString("lat", it.lat)
                            putString("lng", it.lng)
                            putString("logo_image", it.logo_image)
                            putString("name_kana", it.name_kana)
                            putString("address", it.address)
                            putString("access", it.access)
                            putString("open", it.open)
                            putString("close", it.close)
                        }
                    }
                    findNavController().navigate(R.id.action_result_fragment_to_detail_fragment, bundle)
                }
            })
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