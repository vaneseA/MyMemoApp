package com.example.mymemoapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.mymemoapp.databinding.ActivityWriteBinding
import com.example.mymemoapp.main.ContentsModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue


class WriteActivity : AppCompatActivity() {
    // (전역변수) 바인딩 객체 선언
    private var vBinding: ActivityWriteBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!


    //                 Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
    val newRef = FirebaseDatabase.getInstance().getReference("memo").push()
    var backColor = "#ffF1EAAD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityWriteBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)


        //배경 색깔 선택 버튼
        binding.color1.setOnClickListener {
            backColor = "#ffF1EAAD"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color2.setOnClickListener {
            backColor = "#ffE3A8ED"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color3.setOnClickListener {
            backColor = "#ffA9D2F3"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color4.setOnClickListener {
            backColor = "#ffA8EFE9"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color5.setOnClickListener {
            backColor = "#ffF3A6C0"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.saveButton.setOnClickListener {
            // 메세지가 없는 경우 토스트 메세지로 알림.
            if (TextUtils.isEmpty(binding.input.text)) {
                Toast.makeText(applicationContext, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val memo = ContentsModel(
                newRef.key.toString(),
                binding.input.text.toString(),
                ServerValue.TIMESTAMP,
                backColor
            )
            newRef.setValue(memo)
                .addOnSuccessListener {
                    Log.d("WriteActivity", "firebase Database에 저장되었습니다.")
                    Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show()
                }

            finish()


        }
    }

}