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
//    var color = ""


    //배경 리스트 데이터
    //res/drawable 디렉토리에 있는 배경 이미지를 uri 주소로 사용한다
    //uri 주소로 사용하면 추후 웹에 있는 이미지 URL도 바로 사용이 가능하다.


    // 글쓰기 모드를 저장하는 변수
    var mode = "post"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        // 전달받은 intent 에서 댓글 모드인지 확인한다.
        intent.getStringExtra("mode")?.let {
            mode = intent.getStringExtra("mode").toString()
        }

        saveButton.setOnClickListener {
            // 메세지가 없는 경우 토스트 메세지로 알림.
            if (TextUtils.isEmpty(input.text)) {
                Toast.makeText(applicationContext, "메세지를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mode == "post") {
                // Post 객체 생성
                val post = Post()
                // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
//                val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
//                // 글이 쓰여진 시간은 Firebase 서버의 시간으로 설정
//                post.writeTime = ServerValue.TIMESTAMP
//                // 메세지는 input EditText 의 텍스트 내용을 할당
//                post.message = input.text.toString()
//                // 글쓴 사람의 ID 는 디바이스의 아이디로 할당
//                post.postId = newRef.key.toString()
//                // 글배경색깔

//                if (intent.getStringExtra("color") != null) {
//                    post.color = intent.getStringExtra("color")!!
//                    input.setBackgroundColor(Color.parseColor(post.color))
//                }
//                // Post 객체를 새로 생성한 참조에 저장
//                newRef.setValue(post)
//                // 저장성공 토스트 알림을 보여주고 Activity 종료
//                Toast.makeText(applicationContext, "저장완료.", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    fun clickColor(view: View) {
        when (view.id) {
            R.id.color1 -> {
                val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()

                val backColor = "#ffF1EAAD"
                val memo = Memo(input.text.toString(), ServerValue.TIMESTAMP, backColor)
                newRef.setValue(memo)
                    .addOnSuccessListener {
                        Log.d("SignupActivity", "firebase Database에 저장되었습니다.")
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    }
                input.setBackgroundColor(Color.parseColor("#ffF1EAAD"))
//                color = "#ffF1EAAD"


            }
            R.id.color2 -> {
                val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()

                val color = "#ffE3A8ED"
                val memo = Memo(input.text.toString(), ServerValue.TIMESTAMP, color)
                newRef.setValue(memo)
                    .addOnSuccessListener {
                        Log.d("SignupActivity", "firebase Database에 저장되었습니다.")
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    }
                input.setBackgroundColor(Color.parseColor(color))
//                color = "#ffE3A8ED"
            }
            R.id.color3 -> {
                input.setBackgroundColor(Color.parseColor("#ffA9D2F3"))
//                color = "#ffA9D2F3"
            }
            R.id.color4 -> {
                input.setBackgroundColor(Color.parseColor("#ffA8EFE9"))
//                color = "#ffA8EFE9"
            }
            R.id.color5 -> {
                input.setBackgroundColor(Color.parseColor("#ffF3A6C0"))
//                color = "#ffF3A6C0"
            }
        }
    }
}