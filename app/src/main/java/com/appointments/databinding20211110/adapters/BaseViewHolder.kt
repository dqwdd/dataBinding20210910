package com.appointments.databinding20211110.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appointments.databinding20211110.utils.FontChanger

abstract class BaseViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        FontChanger.setGlobalFont(context, itemView)
    }

}