package com.example.databinding20210910.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.databinding20210910.utils.FontChanger

class BaseViewHolder(context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        FontChanger.setGlobalFont(context, itemView)
    }

}