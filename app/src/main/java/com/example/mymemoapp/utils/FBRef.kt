package com.example.mymemoapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

// 클래스 인스턴스 없이 클래스 내부에 접근하려면
class FBRef {

    // -> 클래스 내부에 객체 선언할 때 동반 객체로 선언
    companion object {

        // 파이어베이스
        private val database = Firebase.database

        // .getReference() -> 데이터베이스의 루트 폴더 주소 값을 반환

        // 메모
        val memoRef = database.getReference("memo")

        // 현재 시간 받아옴
        fun getTime(): String {

            // 캘린더 인스턴스 생성
            val currentDateTime = Calendar.getInstance().time

            // SimpleDateFormat() -> 사용자가 임의로 표기 형식 지정 가능
            // Locale.KOREA -> 지역설정 한국
            val dateFormat = SimpleDateFormat("yy년MM월dd일 HH:mm", Locale.KOREA).format(currentDateTime)

            // 원하는 포맷 및 한국어로 시간 반환
            return dateFormat

        }
    }


}