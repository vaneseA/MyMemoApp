package com.example.mymemoapp.main

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoapp.WriteActivity
import com.example.mymemoapp.databinding.ActivityMainBinding
import com.example.mymemoapp.utils.FBRef
import com.google.firebase.database.*

import java.util.*


class MainActivity : AppCompatActivity() {

    // (전역변수) 바인딩 객체 선언
    private var vBinding: ActivityMainBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!


    // 리사이클러뷰 어댑터 선언
    lateinit var rvAdapter: MainRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityMainBinding.inflate(layoutInflater)


        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 아이템(postId,message,writeTime,color) 목록
        val items = ArrayList<ContentsModel>()


        rvAdapter = MainRVAdapter(baseContext, items)


        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터 스냅샷 내 데이터모델 형식으로 저장된
                for (dataModel in dataSnapshot.children) {
                    // 아이템을 받아
                    val item = dataModel.getValue(ContentsModel::class.java)
                    Log.d("asddataModel", dataModel.toString())
                    // 아이템 목록에 넣음
                    items.add(item!!)

                }
                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                rvAdapter.notifyDataSetChanged()
            }

            // 오류 나면
            override fun onCancelled(databaseError: DatabaseError) {

                // 로그
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())

            }
        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FBRef.memoRef.addValueEventListener(postListener)

        // 리사이클러뷰 어댑터 연결
        val rv: RecyclerView = binding.rv
        rv.adapter = rvAdapter

        // RecyclerView 에 LayoutManager 설정
        rv.layoutManager = LinearLayoutManager(baseContext)


        binding.addBtn.setOnClickListener {
            // Intent 생성
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            // Intent 로 WirteActivity 실행
            startActivity(intent)
        }
    }


    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}

