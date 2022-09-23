package com.example.mymemoapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri.parse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView

import android.widget.TextView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_item.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import java.net.HttpCookie.parse
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level.parse


class MainActivity : AppCompatActivity() {
    // 글 목록을 저장하는 변수
    val posts: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.mymemoapp.R.layout.activity_main)
        addBtn.setOnClickListener {
            // Intent 생성
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            // Intent 로 WirteActivity 실행
            startActivity(intent)
        }
        // RecyclerView 에 LayoutManager 설정
        val layoutManager = LinearLayoutManager(this@MainActivity)
        // 리사이클러뷰의 아이템을 역순으로 정렬하게 함
        layoutManager.reverseLayout = true
        // 리사이클려뷰의 아이템을 쌓는 순서를 끝부터 쌓게 함
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter()
        // Firebase 에서 Post 데이터를 가져온 후 posts 변수에 저장
        FirebaseDatabase.getInstance().getReference("/Posts")
            .orderByChild("writeTime").addChildEventListener(object : ChildEventListener {
                // 글이 추가된 경우
                override fun onChildAdded(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        post?.let {
                            // 새 글이 마지막 부분에 추가된 경우
                            if (prevChildKey == null) {
                                //글 목록을 저장하는 변수에 post 객체 추가
                                posts.add(it)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                recyclerView.adapter?.notifyItemInserted(posts.size - 1)
                            } else {
                                // 글이 중간에 삽입된 경우 prevChildKey 로 한단계 앞의 데이터의 위치를 찾은 뒤 데이터를 추가한다.
                                val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                                posts.add(prevIndex + 1, post)
                                // RecyclerView 의 adapter 에 글이 추가된 것을 알림
                                recyclerView.adapter?.notifyItemInserted(prevIndex + 1)
                            }
                        }
                    }
                }



                // 글이 변경된 경우
                override fun onChildChanged(snapshot: DataSnapshot, prevChildKey: String?) {
                    snapshot?.let { snapshot ->
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        post?.let { post ->
                            // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경한다.
                            val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                            posts[prevIndex + 1] = post
                            recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                // 글의 순서가 이동한 경우
                override fun onChildMoved(snapshot: DataSnapshot, prevChildKey: String?) {
                    // snapshot
                    snapshot?.let {
                        // snapshop 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        post?.let { post ->
                            // 기존의 인덱스를 구한다
                            val existIndex = posts.map { it.postId }.indexOf(post.postId)
                            // 기존에 데이터를 지운다.
                            posts.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)
                            // prevChildKey 가 없는 경우 맨마지막으로 이동 된 것
                            if (prevChildKey == null) {
                                posts.add(post)
                                recyclerView.adapter?.notifyItemChanged(posts.size - 1)
                            } else {
                                // prevChildKey 다음 글로 추가
                                val prevIndex = posts.map { it.postId }.indexOf(prevChildKey)
                                posts.add(prevIndex + 1, post)
                                recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                }

                // 글이 삭제된 경우
                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        // snapshot 의 데이터를 Post 객체로 가져옴
                        val post = snapshot.getValue(Post::class.java)
                        //
                        post?.let { post ->
                            // 기존에 저장된 인덱스를 찾아서 해당 인덱스의 데이터를 삭제한다.
                            val existIndex = posts.map { it.postId }.indexOf(post.postId)
                            posts.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)
                        }
                    }
                }

                // 취소된 경우
                override fun onCancelled(databaseError: DatabaseError) {
                    // 취소가 된경우 에러를 로그로 보여준다
                    databaseError?.toException()?.printStackTrace()
                }
            })
    }

    // RecyclerView 에서 사용하는 View 홀더 클래스
    inner class MyViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 글의 배경 이미지뷰
        val backgrounColorImageView: ImageView = itemView.backgrounColorImageView
        // 글의 내용 텍스트뷰
        val contentsText: TextView = itemView.contentsText

        // 글쓴 시간 텍스트뷰
        val timeTextView: TextView = itemView.timeTextView
        // 메모지 바탕화면
//        val layoutBackground: CardView = itemView.layoutBackground
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHodler>() {
        // RecyclerView 에서 각 Row(행)에서 그릴 ViewHolder 를 생성할때 불리는 메소드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHodler {
            return MyViewHodler(
                LayoutInflater.from(this@MainActivity).inflate(
                    com.example.mymemoapp.R.layout.recycler_item,
                    parent, false
                )
            )
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            return posts.size
        }

        // 각 행의 포지션에서 그려야할 ViewHolder UI 에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHodler, position: Int) {
            val post = posts[position]
//            // 배경 이미지 설정
//            Picasso.get().load(Color.parse(post.bgUri)).fit().centerCrop().into(holder.imageView)
            // 카드에 글을 세팅
            holder.itemView.contentsText.text = post.message
            // 글이 쓰여진 시간
            holder.itemView.timeTextView.text = getDiffTimeText(post.writeTime as Long)
            // 배경색깔
            holder.itemView.backgrounColorImageView.setBackgroundColor(Color.parseColor(post.color))

//            holder.backgrounColorImageView.setBackgroundColor(Color.parseColor(post.color))
            // 카드가 클릭되는 경우 DetailActivity 를 실행한다.
            holder.itemView.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성한다.
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                // 선택된 카드의 ID 정보를 intent 에 추가한다.
                intent.putExtra("postId", post.postId)
                // intent 로 상세화면을 시작한다.
                startActivity(intent)
            }
        }
    }

    // 글이 쓰여진 시간을 "방금전", " 시간전", "yyyy년 MM월 dd일 HH:mm" 포맷으로 반환해주는 메소드
    fun getDiffTimeText(targetTime: Long): String {
        val curDateTime = DateTime()
        val targetDateTime = DateTime().withMillis(targetTime)
        val diffDay = Days.daysBetween(curDateTime, targetDateTime).days
        val diffHours = Hours.hoursBetween(targetDateTime, curDateTime).hours
        val diffMinutes = Minutes.minutesBetween(targetDateTime, curDateTime).minutes
        if (diffDay == 0) {
            if (diffHours == 0 && diffMinutes == 0) {
                return "방금 전"
            }
            return if (diffHours > 0) {
                "" + diffHours + "시간 전"
            } else "" + diffMinutes + "분 전"
        } else {
            val format = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
            return format.format(Date(targetTime))
        }
    }


}