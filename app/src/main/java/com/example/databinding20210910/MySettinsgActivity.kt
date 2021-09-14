package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityMySettinsgBinding

class MySettinsgActivity : BaseActivity() {

    lateinit var binding : ActivityMySettinsgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_settinsg)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {
        titleTxt.text = "프로필설정"
    }
}