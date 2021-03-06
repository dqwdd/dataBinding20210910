package com.appointments.databinding20211110.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.appointments.databinding20211110.R
import com.appointments.databinding20211110.ViewMyPlaceListActivity
import com.appointments.databinding20211110.datas.BasicResponse
import com.appointments.databinding20211110.datas.PlaceData
import com.appointments.databinding20211110.web.ServerAPI
import com.appointments.databinding20211110.web.ServerAPIService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlaceRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlaceData>) : RecyclerView.Adapter<MyPlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view : View) : BaseViewHolder(mContext, view) {

        val placeNameTxt = view.findViewById<TextView>(R.id.placeNameTxt)
        val isPrimaryTxt = view.findViewById<TextView>(R.id.isPrimaryTxt)
        val viewPlaceMapBtn = view.findViewById<ImageView>(R.id.viewPlaceMapBtn)
        val backgroundLayout = view.findViewById<LinearLayout>(R.id.backgroundLayout)

        fun setRealData( data: PlaceData ) {

            placeNameTxt.text = data.name

            if (data.isPrimary) {
                isPrimaryTxt.visibility = View.VISIBLE
            }
            else {
                isPrimaryTxt.visibility = View.GONE
            }


        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.my_place_list_item, parent, false)
        return MyViewHolder(view)

    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//          위에 fun setRealData( data: PlaceData ) 만들어서 여기에 쓸 거 대신 넣어도 됨
////        실제 데이터를 뿌려주는 부분
        val data = mList[position]
//
////        예시 : 실제 장소 이름만 출력
//        holder.placeNameTxt.text = data.name

        holder.setRealData(mList[position])

//        이벤트처리는 mContext 변수때문에, 이 onBindViewHolder에서 처리하자
        holder.viewPlaceMapBtn.setOnClickListener {

            Toast.makeText(mContext, "지도 버튼을 눌렀습니다", Toast.LENGTH_SHORT).show()

        }

        holder.backgroundLayout.setOnClickListener {

            Toast.makeText(mContext, "${mList[position].name}을 클릭", Toast.LENGTH_SHORT).show()

        }

        holder.backgroundLayout.setOnLongClickListener {

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("${data.name}를 삭제하시겠습니까?")
            val DeletePlace = data.name

            alert.setNegativeButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                val apiService = ServerAPI.getRetrofit(mContext).create(ServerAPIService::class.java)

                apiService.getRequestFrequentPlaceDelete(data.id).enqueue(object :
                    Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                mContext, DeletePlace + "가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                            (mContext as ViewMyPlaceListActivity)
                                .getMyPlaceListFromServer()
                        }
                        else {
                            val errorBodyStr = response.errorBody()!!.string()

//                        단순 JSON 형태의 String으로 내려옴 => JSONObject 형태로 가공
                            Log.d("에러인 경우의 데이터 : ", errorBodyStr)
                            val jsonObj = JSONObject(errorBodyStr)
                            val message = jsonObj.getString("message")
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }


                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }
                })
            })
            alert.setPositiveButton("취소", null)
            alert.show()

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount() = mList.size

}