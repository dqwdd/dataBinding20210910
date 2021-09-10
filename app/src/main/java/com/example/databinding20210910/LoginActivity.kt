package com.example.databinding20210910

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityLoginBinding
import java.security.MessageDigest

class LoginActivity : BaseActivity() {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)

        setupEvent()
        setValues()
    }

    override fun setupEvent() {
    }

    override fun setValues() {

//        카톡으로 받은 코드 복붙 => 키 해시값 추출

        val info = packageManager.getPackageInfo(
            "com.example.databinding20210910",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }

    }
}