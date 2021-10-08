package com.appointment.databinding20210910.utils

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FontChanger {

    companion object { //static같음

        // 1. 원하는 폰트를 꺼내주는 함수
        // 2. 폰트를 모든 텍스트뷰를 찾아서 적용해주는 함수

        fun setGlobalFont(context: Context, view: View){

            // 뷰 하나를 받아서 모든 하위 뷰를 검색해보자
            // 그릇 (layout)으로써 하위 뷰들을 들고 있나?

            if (view is ViewGroup) {
                //뷰가 뷰그룹이면(하위뷰들이 있으면)
                // for문으로 뽑아보자

                for (i in 0 until view.childCount) {

                    val childView = view.getChildAt(i)

                    // 만약 뽑아낸 뷰가 textview면 폰트를 적용시키겠다
                    if (childView is TextView) {

                        //asset에 추가한 파일을 적용
                        childView.typeface = Typeface.createFromAsset(context.assets, "GowunDodum_Regular.ttf")
                        //typeface==폰트를 먹이는 기능
                        //createFromeAsset == Asset에서 뽑아주세요~
                    }

                    //자녀뷰에도 또 다른 하위 뷰가 있는지 확인해서 폰트 적용
                    //얘들은 자기 바로 밑의 자녀 뷰만 인식함(자녀의 자녀뷰나 밑에 뷰는 모른대)
                    //그래서 재귀함수로 다른 얘들도 확인하는 코드 짜야 함
                    setGlobalFont(context,childView)//함수 안에서 자신을 다시 호출 == 재귀함수

                }


            }

         }
    }

}