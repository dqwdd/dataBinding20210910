package com.example.databinding20210910.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.databinding20210910.R
import com.example.databinding20210910.datas.AppointmentData
import org.json.JSONObject
import java.text.SimpleDateFormat

class AppointmentAdapter(
    val mContext : Context,
    resId : Int,
    val mList : List<AppointmentData>) : ArrayAdapter<AppointmentData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = mInflater.inflate(R.layout.appointment_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]


        return row
    }


}