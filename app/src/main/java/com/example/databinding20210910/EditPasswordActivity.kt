package com.example.databinding20210910

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.databinding20210910.databinding.ActivityEditPasswordBinding
import com.example.databinding20210910.databinding.ActivityMySettinsgBinding
import com.example.databinding20210910.datas.BasicResponse
import com.example.databinding20210910.utils.ContextUtil
import com.example.databinding20210910.utils.GlobalData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : BaseActivity() {

    lateinit var binding : ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password)
        setupEvent()
        setValues()
    }

    override fun setupEvent() {

        binding.editBtn.setOnClickListener {

            val currentPassword = binding.currentPasswordEdt.text.toString()
            val currentPasswordTxt = binding.currentPasswordTxt.text.toString()
            val newPassword = binding.newPasswordEdt.text.toString()
            val againTxt = binding.againTxt.text.toString()
            val checkTxt = binding.checkTxt.text.toString()

            if (newPassword != againTxt) {
                binding.checkTxt.text = "같은 비밀번호를 입력해 주세요"
            }
            else {
                apiService.getRequestUserPasswordChange(currentPassword, newPassword).enqueue(object :
                    Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            val basicResponse = response.body()!!
                            val alert = AlertDialog.Builder(mContext)
                            alert.setMessage("비밀번호가 변경되었습니다")
                            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                                finish()
                            })
                            alert.show()
                            ContextUtil.setToken(mContext, basicResponse.data.token)
                        }
                        else {
                            val jsonObj = JSONObject(response.errorBody()!!.string())
                            val message = jsonObj.getString("message")
                            Log.d("응답 실패", jsonObj.toString())
                            binding.currentPasswordTxt.text = "현재 비밀번호가 일치하지 않습니다"
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Toast.makeText(mContext, "응답 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }


        }

    }

    override fun setValues() {
    }
}