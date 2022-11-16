package com.example.mymemoapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymemoapp.databinding.ActivityDetailBinding
import com.example.mymemoapp.main.ContentsModel
import com.example.mymemoapp.utils.FBRef
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity() {


    // (전역변수) 바인딩 객체 선언
    private var vBinding: ActivityDetailBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    private val TAG = "DetailActivity"


    private lateinit var backColor: String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityDetailBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)


        val memoId = intent.getStringExtra("memoId").toString()
        val layoutManager = LinearLayoutManager(this@DetailActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        // memoId 값을 바탕으로 게시글 하나의 정보를 가져옴
        getMemoDataForDetail(memoId)


        //백그라운드 컬러설정버튼
        selectUpdateBackColor()

        // 메모 업데이트 버튼
        binding.saveButton.setOnClickListener {
            updateMemo(memoId,backColor)
            finish()
        }


    }

    private fun getMemoDataForDetail(memoId: String) {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터 스냅샷 내 데이터모델 형식으로 저장된 아이템(=메모)
                val item = dataSnapshot.getValue(ContentsModel::class.java)
                // 예외 처리
                try {

                    // 제목, 본문 해당 영역에 넣음(작성자 및 시간은 직접 수정하지 않음)
                    binding.contents.setText(item?.contents)
                    binding.contents.setBackgroundColor(Color.parseColor(item?.color.toString()))

                    // 오류 나면
                } catch (e: Exception) {

                    // 로그
                    Log.e(TAG, "getMemoDataForDetail 확인")

                }


            }

            override fun onCancelled(error: DatabaseError) {

                // 로그
                Log.w(TAG, "loadPost:onCancelled", error.toException())
            }

        }
        FBRef.memoRef.child(memoId).addValueEventListener(postListener)
    }

    private fun updateMemo(memoId: String, backColor: String) {
        // 메모의 데이터(memoId, 본문, 컬러, 시간)
        val contents = binding.contents.text.toString()
        val time = FBRef.getTime()
        // 키 값 하위에 데이터 넣음
        FBRef.memoRef
            .child(memoId)
            .setValue(ContentsModel(memoId, contents, time, backColor)).addOnSuccessListener {
                // 등록 확인 메시지 띄움
                Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show()
            }

        // 글쓰기 액티비티 종료
        finish()
    }

    private fun selectUpdateBackColor() {
        //배경 색깔 선택 버튼
        binding.colorUpdate1.setOnClickListener {
            backColor = "#ffF1EAAD"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate2.setOnClickListener {
            backColor = "#ffE3A8ED"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate3.setOnClickListener {
            backColor = "#ffA9D2F3"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate4.setOnClickListener {
            backColor = "#ffA8EFE9"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate5.setOnClickListener {
            backColor = "#ffF3A6C0"
            binding.contents.setBackgroundColor(Color.parseColor(backColor))
        }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}