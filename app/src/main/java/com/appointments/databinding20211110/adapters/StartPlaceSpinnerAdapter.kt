package com.appointments.databinding20211110.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.datas.PlaceData
import com.appointments.databinding20211110.utils.FontChanger

class StartPlaceSpinnerAdapter(
    val mContext : Context,
    resId : Int,
    val mList : List<PlaceData>) : ArrayAdapter<PlaceData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.my_place_list_item, null)
        }
        row!!

        val data = mList[position]

        val placeNameTxt = row.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = row.findViewById<TextView>(R.id.isPrimaryTxt)
        val viewPlaceMapBtn = row.findViewById<ImageView>(R.id.viewPlaceMapBtn)

        placeNameTxt.text = data.name

        if (data.isPrimary) {
            isPrimaryTxt.visibility = View.VISIBLE
        }
        else {
            isPrimaryTxt.visibility = View.GONE
        }

        FontChanger.setGlobalFont(mContext, row)

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        if (row == null) {
            row = mInflater.inflate(R.layout.my_place_list_item, null)
        }
        row!!

        val data = mList[position]

        val placeNameTxt = row.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = row.findViewById<TextView>(R.id.isPrimaryTxt)
        val viewPlaceMapBtn = row.findViewById<ImageView>(R.id.viewPlaceMapBtn)

        placeNameTxt.text = data.name

        if (data.isPrimary) {
            isPrimaryTxt.visibility = View.VISIBLE
        }
        else {
            isPrimaryTxt.visibility = View.GONE
        }

        FontChanger.setGlobalFont(mContext, row)

        return row
    }


}