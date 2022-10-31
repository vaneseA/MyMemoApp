package com.example.mymemoapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail.input
import kotlinx.android.synthetic.main.activity_detail.saveButton
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.recycler_item.view.*

class DetailActivity : AppCompatActivity() {

    var backColor = "#ffF1EAAD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mymemoapp.R.layout.activity_detail)


        //배경 색깔 선택 버튼
        colorUpdate1.setOnClickListener {
            backColor = "#ffF1EAAD"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        colorUpdate2.setOnClickListener {
            backColor = "#ffE3A8ED"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        colorUpdate3.setOnClickListener {
            backColor = "#ffA9D2F3"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        colorUpdate4.setOnClickListener {
            backColor = "#ffA8EFE9"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        colorUpdate5.setOnClickListener {
            backColor = "#ffF3A6C0"
            input.setBackgroundColor(Color.parseColor(backColor))
        }
        val postId = intent.getStringExtra("postId")

        val layoutManager = LinearLayoutManager(this@DetailActivity)
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
                            val currentMessage = input.setText(post.message)
                            val currentColor =
                                input.setBackgroundColor(Color.parseColor(post.color))
                            currentMessage
                            currentColor
                        }

                    }
                }
            })

        // 하단 댓글쓰기 버튼에 클릭 이벤트 리스너 설정
        saveButton.setOnClickListener {
            updatePostData(
                postId.toString(),
                input.text.toString(),
                ServerValue.TIMESTAMP,
                backColor
            )
            finish()
        }


    }

    private fun updatePostData(
        postId: String,
        message: String,
        writeTime: Any = Any(),
        color: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId)
        val memoInfo =
            Memo(dbRef.key.toString(), input.text.toString(), ServerValue.TIMESTAMP, backColor)
        dbRef.setValue(memoInfo)
            .addOnSuccessListener {
                Log.d("DetailActivity", "firebase Database에 저장되었습니다.")
                Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show()
            }

    }


}