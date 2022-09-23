package com.example.mymemoapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.recycler_item.*


class WriteActivity : AppCompatActivity() {

    var mode = "post"
    //                 Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
    val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
    var backColor = "#ffF1EAAD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        // 전달받은 intent 에서 댓글 모드인지 확인한다.
        intent.getStringExtra("mode")?.let {
            mode = intent.getStringExtra("mode").toString()
        }

        //배경 색깔 선택 버튼
        color1.setOnClickListener {
            backColor = "#ffF1EAAD"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        color2.setOnClickListener {
            backColor = "#ffE3A8ED"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        color3.setOnClickListener {
            backColor = "#ffA9D2F3"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        color4.setOnClickListener {
            backColor = "#ffA8EFE9"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        color5.setOnClickListener {
            backColor = "#ffF3A6C0"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        saveButton.setOnClickListener {
            // 메세지가 없는 경우 토스트 메세지로 알림.
            if (TextUtils.isEmpty(input.text)) {
                Toast.makeText(applicationContext, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mode == "post") {

                val memo = Memo(newRef.key.toString(),input.text.toString(), ServerValue.TIMESTAMP, backColor)
                newRef.setValue(memo)
                    .addOnSuccessListener {
                        Log.d("SignupActivity", "firebase Database에 저장되었습니다.")
                        Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show()
                    }

                finish()

            }
        }
    }

}