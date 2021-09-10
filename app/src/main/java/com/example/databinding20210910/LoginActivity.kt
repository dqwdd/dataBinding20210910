package com.example.databinding20210910

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {
    }
}