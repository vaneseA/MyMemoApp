package com.example.mymemoapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.database.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.input
import kotlinx.android.synthetic.main.activity_detail.saveButton
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.recycler_item.view.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mymemoapp.R.layout.activity_detail)
// actionbar 의 타이틀을 "글쓰기" 로 변경
        supportActionBar?.title = "메모 수정하기"
        val postId = intent.getStringExtra("postId")

        val  layoutManager = LinearLayoutManager(this@DetailActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        // 게시글의 ID 로 게시글의 데이터로 다이렉트로 접근한다.
        FirebaseDatabase.getInstance().getReference("/Posts/$postId")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot?.let {
                        val post = it.getValue(Post::class.java)
                        post?.let {
                            //post에 입력한 값 불러오기
                            input.setText(post.message)
                            input.setBackgroundColor(Color.parseColor(post.color))
                        }
                    }
                }
            })

        // 하단 댓글쓰기 버튼에 클릭 이벤트 리스너 설정
        saveButton.setOnClickListener {
            // 글쓰기 화면으로 이동할 Intent 생성
            val intent = Intent(this@DetailActivity, WriteActivity::class.java)
            // 글쓰기 화면에서 댓글쓰기 인것을 인식할수 있도록 글쓰기 모드를 comment 로 전달
            intent.putExtra("mode", "comment")
            // 글의 ID 를 전달
            intent.putExtra("postId", postId)
            // 글쓰기 화면 시작
            startActivity(intent)
        }
    }

}
