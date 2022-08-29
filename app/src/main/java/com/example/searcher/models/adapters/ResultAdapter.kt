package com.example.searcher.models.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.searcher.R
import com.example.searcher.models.items.Shop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL


class ResultAdapter(context: Context, private var data: List<Shop>) : BaseAdapter() {

    private var layoutInflater : LayoutInflater

    init {
        this.layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return data.count()
    }

    override fun getItem(pos: Int): Any {
        return data[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(pos: Int, view: View?, root: ViewGroup?): View {
        val v: View = layoutInflater.inflate(R.layout.list_result, root, false)
        val url = URL(data[pos].logo_image)
        GlobalScope.launch(Dispatchers.IO) {
            val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            launch(Dispatchers.Main) {
                v.findViewById<ImageView>(R.id.image).setImageBitmap(image)
            }
        }
        v.findViewById<TextView>(R.id.name).text = data[pos].name
        v.findViewById<TextView>(R.id.access).text = data[pos].access
        return v
    }
}