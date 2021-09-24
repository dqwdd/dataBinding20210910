package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityViewNotificationBinding

class ViewNotificationActivity : BaseActivity() {

    lateinit var binding : ActivityViewNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_notification)
        setValues()
        setupEvent()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

        titleTxt.text = "알림"

   }
}