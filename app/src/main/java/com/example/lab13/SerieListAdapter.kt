package com.example.lab13

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SerieListAdapter(context: Context, series: List<SerieModel>) :
    ArrayAdapter<SerieModel>(context, 0, series) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val serie = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_serie, parent, false)

        val lvID = view.findViewById<TextView>(R.id.lvID)
        val lvName = view.findViewById<TextView>(R.id.lvName)
        val lvReleaseDate = view.findViewById<TextView>(R.id.lvReleaseDate)
        val lvRating = view.findViewById<TextView>(R.id.lvRating)
        val lvCategory = view.findViewById<TextView>(R.id.lvCategory)

        lvID.text = serie?.id.toString()
        lvName.text = serie?.name
        lvReleaseDate.text = serie?.release_date
        lvRating.text = serie?.rating.toString()
        lvCategory.text = serie?.category

        return view
    }
}