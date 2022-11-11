package com.example.mymemoapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Toast
import com.example.mymemoapp.databinding.ActivityWriteBinding
import com.example.mymemoapp.main.ContentsModel
import com.example.mymemoapp.utils.FBRef
import com.example.mymemoapp.utils.FBRef.Companion.getTime


class WriteActivity : AppCompatActivity() {
    // (전역변수) 바인딩 객체 선언
    private var vBinding: ActivityWriteBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!


    //                 Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
    private lateinit var memoId: String
    var backColor = "#ffF1EAAD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityWriteBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)
        // memoId 값 기반으로 데이터를 받아오기 위해 키 값 선생성
        memoId = FBRef.memoRef.push().key.toString()


        //배경 색깔 선택 버튼
        binding.color1.setOnClickListener {
            backColor = "#ffF1EAAD"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color2.setOnClickListener {
            backColor = "#ffE3A8ED"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color3.setOnClickListener {
            backColor = "#ffA9D2F3"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color4.setOnClickListener {
            backColor = "#ffA8EFE9"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.color5.setOnClickListener {
            backColor = "#ffF3A6C0"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.saveButton.setOnClickListener {
            // 메세지가 없는 경우 토스트 메세지로 알림.
            if (TextUtils.isEmpty(binding.contents.text)) {
                Toast.makeText(applicationContext, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                setMemo(memoId, backColor)
                finish()
            }
        }
    }

    // 작성한 글을 등록
    private fun setMemo(memoId: String, backColor: String) {
        // 메모의 데이터(memoId, 본문, 컬러, 시간)
        val contents = binding.contents.text.toString()
        val time = getTime()

        // 키 값 하위에 데이터 넣음
        FBRef.memoRef
            .child(memoId)
            .setValue(ContentsModel(memoId, contents, time, backColor.toString()))


        // 등록 확인 메시지 띄움
        Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show()

        // 글쓰기 액티비티 종료
        finish()

        // 내가 원하는 것 //
        // 글쓰기 액티비티 종료 -> 방금 쓴 글(글읽기 액티비티)로 이동

    }
}