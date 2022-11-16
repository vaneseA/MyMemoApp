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

    // 아이템(postId,message,writeTime,color) 목록
    val items: MutableList<ContentsModel> = mutableListOf()
//    val items = ArrayList<ContentsModel>()

    // (전역변수) 바인딩 객체 선언
    private var vBinding: ActivityMainBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    private val TAG = "MainActivity"

    // 메모ID
    private lateinit var memoId: String

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


        binding.addBtn.setOnClickListener {
            // Intent 생성
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            // Intent 로 WirteActivity 실행
            startActivity(intent)
        }

        rvAdapter = MainRVAdapter(baseContext, items)

        // 게시판 프래그먼트에서 게시글의 키 값을 받아옴
        memoId = intent.getStringExtra("memoId").toString()

        getMemoDataForMain()


        // 리사이클러뷰 어댑터 연결
        val rv: RecyclerView = binding.rv
        rv.adapter = rvAdapter

        // RecyclerView 에 LayoutManager 설정
        rv.layoutManager = LinearLayoutManager(baseContext)


    }

    private fun getMemoDataForMain() {
        FirebaseDatabase.getInstance().getReference("/memo")
            .orderByChild("writeTime").addChildEventListener(object : ChildEventListener {
                // 글이 추가된 경우
                override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val memo = snapshot.getValue(ContentsModel::class.java)
                        memo?.let {
                            // 새 글이 마지막 부분에 추가된 경우
                            if (prevChildKey == null) {
                                //글 목록을 저장하는 변수에 post 객체 추가
                                items.add(it)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                rvAdapter?.notifyItemInserted(items.size - 1)
                            } else {
                                // 글이 중간에 삽입된 경우 prevChildKey 로 한단계 앞의 데이터의 위치를 찾은 뒤 데이터를 추가한다.
                                val prevIndex = items.map { it.memoId }.indexOf(prevChildKey)
                                items.add(prevIndex + 1, memo)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                rvAdapter?.notifyItemInserted(prevIndex + 1)
                            }
                        }
                    }
                }


                // 글이 변경된 경우
                override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(ContentsModel::class.java)
                        post?.let { memo ->
                            // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경한다.
                            val prevIndex = items.map { it.memoId }.indexOf(prevChildKey)
                            items[prevIndex + 1] = post
                            rvAdapter.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        // snapshot 의 데이터를 Post 객체로 가져옴
                        val memo = snapshot.getValue(ContentsModel::class.java)
                        //
                        memo?.let { memo ->
                            // 기존에 저장된 인덱스를 찾아서 해당 인덱스의 데이터를 삭제한다.
                            val existIndex = items.map { it.memoId }.indexOf(memo.memoId)
                            items.removeAt(existIndex)
                            rvAdapter?.notifyItemRemoved(existIndex)
                        }
                    }
                }

                // 글의 순서가 이동한 경우
                override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                    // snapshot
                    snapshot?.let {
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val memo = snapshot.getValue(ContentsModel::class.java)
                        memo?.let { memo ->
                            // 기존의 인덱스를 구한다
                            val existIndex = items.map { it.memoId }.indexOf(memo.memoId)
                            // 기존에 데이터를 지운다.
                            items.removeAt(existIndex)
                            rvAdapter?.notifyItemRemoved(existIndex)
                            // prevChildKey 가 없는 경우 맨마지막으로 이동 된 것
                            if (prevChildKey == null) {
                                items.add(memo)
                                rvAdapter?.notifyItemChanged(items.size - 1)
                            } else {
                                // prevChildKey 다음 글로 추가
                                val prevIndex = items.map { it.memoId }.indexOf(prevChildKey)
                                items.add(prevIndex + 1, memo)
                                rvAdapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // 취소가 된경우 에러를 로그로 보여준다
                    error?.toException()?.printStackTrace()
                }
            })
    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}

