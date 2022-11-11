package com.example.mymemoapp

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
    private var vBinding : ActivityDetailBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!


    var backColor = "#ffF1EAAD"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityDetailBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        //배경 색깔 선택 버튼
        binding.colorUpdate1.setOnClickListener {
            backColor = "#ffF1EAAD"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate2.setOnClickListener {
            backColor = "#ffE3A8ED"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate3.setOnClickListener {
            backColor = "#ffA9D2F3"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate4.setOnClickListener {
            backColor = "#ffA8EFE9"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        binding.colorUpdate5.setOnClickListener {
            backColor = "#ffF3A6C0"
            binding.input.setBackgroundColor(Color.parseColor(backColor))
        }
        val postId = intent.getStringExtra("postId")

        val layoutManager = LinearLayoutManager(this@DetailActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        // 게시글의 ID 로 게시글의 데이터로 다이렉트로 접근한다.
        FirebaseDatabase.getInstance().getReference("/memo/$postId")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot?.let {
                        val post = it.getValue(ContentsModel::class.java)
                        post?.let {
                            //post에 입력한 값 불러오기
                            val currentMessage = binding.input.setText(post.contents)
                            val currentColor =
                                binding.input.setBackgroundColor(Color.parseColor(post.color.toString()))
                            currentMessage
                            currentColor
                        }

                    }
                }
            })

        // 하단 댓글쓰기 버튼에 클릭 이벤트 리스너 설정
        binding.saveButton.setOnClickListener {
            updatePostData(
                postId.toString(),
                binding.input.text.toString(),
                ServerValue.TIMESTAMP,
                backColor
            )
            finish()
        }


    }

    private fun updatePostData(
        postId: String,
        contens: String,
        writeTime: Any = Any(),
        color: String
    ) {
        val dbRef = FBRef.memoRef.child(postId)
        val memoInfo =
            ContentsModel(dbRef.key.toString(), binding.input.text.toString(), ServerValue.TIMESTAMP, backColor)
        dbRef.setValue(memoInfo)
            .addOnSuccessListener {
                Log.d("DetailActivity", "firebase Database에 저장되었습니다.")
                Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show()
            }

    }


}