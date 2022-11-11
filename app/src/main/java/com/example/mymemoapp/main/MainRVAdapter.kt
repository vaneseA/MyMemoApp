package com.example.mymemoapp.main

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoapp.DetailActivity

import com.example.mymemoapp.R
import com.example.mymemoapp.utils.FBRef
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private lateinit var postId: String

class MainRVAdapter(
    val context: Context, // 컨텍스트
    val items: MutableList<ContentsModel> = mutableListOf(), // 아이템(postId,message,writeTime,color) 목록
) : RecyclerView.Adapter<MainRVAdapter.Viewholder>() {
    // 리사이클러뷰의 어댑터 -> RecyclerView.Adapter를 상속해서 구현

    // RecyclerView 에서 사용하는 View 홀더 클래스
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRVAdapter.Viewholder {
        // 레이아웃 인플레이터 -> 리사이클러뷰에서 뷰홀더 만들 때 반복적으로 사용
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)

        // 아직 데이터는 들어가있지 않은 껍데기
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: MainRVAdapter.Viewholder, position: Int) {
        val memo = items[position]

        // 껍데기(뷰홀더의 레이아웃)에 출력할 내용물(아이템 목록, 아이템의 키 목록)을 넣어줌
//        holder.bindItems(items[position])

        val intent = Intent(context, DetailActivity::class.java)
        // 선택된 카드의 ID 정보를 intent 에 추가한다.
        intent.putExtra("postId", memo.postId)

        postId = intent.getStringExtra("postId").toString()
        val contentsText = holder.itemView.findViewById<TextView>(R.id.contentsArea)
        val timeTextView = holder.itemView.findViewById<TextView>(R.id.timeTextArea)
        val backgroundColorImageView =
            holder.itemView.findViewById<ImageView>(R.id.backgroundColorImageArea)

        // 카드에 글을 세팅
        contentsText.text = memo.contents
        // 글이 쓰여진 시간
        timeTextView.text = getDiffTimeText(memo.writeTime as Long)
        // 배경색깔
        backgroundColorImageView.setBackgroundColor(Color.parseColor(memo.color.toString()))


        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            Log.d("TAG", memo.postId)
            showDialog(memo.postId, holder.itemView.context)
            return@setOnLongClickListener (true)
        }
    }

    // 아이템들의 총 개수 반환
    override fun getItemCount(): Int = items.size


    // 각 아이템에 데이터 넣어줌
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        // 데이터 매핑(아이템)
//        fun bindItems(item: ContentsModel) {
//
//
//            // 명시적 인텐트 -> 다른 액티비티 호출
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("postId", item.postId)
//
//            postId = intent.getStringExtra("postId").toString()
//
//            // 각 아이템뷰의 내용/시간/배경색깔 영역
//            val contentsTitle = itemView.findViewById<TextView>(R.id.contentsArea)
//            val timeTextViewArea = itemView.findViewById<TextView>(R.id.timeTextArea)
//            val backgroundColorImageView = itemView.findViewById<ImageView>(R.id.backgroundColorImageArea)
//
//            // 카드에 글을 세팅
//            contentsTitle.text= items[position].contents
////            contentsTitle.text= item.contents
//
//            // 글이 쓰여진 시간
//            timeTextViewArea.text= getDiffTimeText(item.writeTime as Long)
//            // 배경색깔
//            backgroundColorImageView.setBackgroundColor(Color.parseColor(item.color.toString()))
//


        // 리사이클러뷰는 setOnItemClickListener 없음 -> 개발자가 직접 구현해야 함
        // 아이템뷰(아이템 영역)를 클릭하면
//            itemView.setOnClickListener {
//
//                // 카드가 클릭되는 경우 DetailActivity 를 실행한다.
//                itemView.context.startActivity(intent)
//
//
//            }

//            itemView.setOnLongClickListener {
////                Log.d("dddpostId", item.postId) item.postId잘들어감
//                val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
//
//
//                // 대화상자 생성
//                val builder = AlertDialog.Builder(context)
//                    .setView(dialogView)
//                    .setTitle("정말 삭제하시겠습니까?")
//
//                // 대화상자 띄움
//                val alertDialog = builder.show()
//                return@setOnLongClickListener (true)
//        }


    }


}

private fun showDialog(postId: String, context: Context) {


    // custom_dialog를 뷰 객체로 반환
    val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)

    // 대화상자 생성
    val builder = AlertDialog.Builder(context)
        .setView(dialogView)
        .setTitle("정말 삭제하시겠습니까?")

//         대화상자 띄움
    val alertDialog = builder.show()

    val yesBtn = alertDialog.findViewById<ConstraintLayout>(R.id.yesBtn)
    val noBtn = alertDialog.findViewById<ConstraintLayout>(R.id.noBtn)


    yesBtn.setOnClickListener {
        Log.d("dddyes", "clicked")
        deletePost(postId, context)
        alertDialog.dismiss()

    }
    noBtn.setOnClickListener {
        Log.d("dddno", "clicked")
        alertDialog.dismiss()
        Toast.makeText(context, "취소되었습니다", Toast.LENGTH_SHORT).show()
    }

}

private fun deletePost(postId: String, context: Context) {
    FBRef.memoRef.child(postId).removeValue()
    Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show()
}

// 글이 쓰여진 시간을 "방금전", " 시간전", "yyyy년 MM월 dd일 HH:mm" 포맷으로 반환해주는 메소드
fun getDiffTimeText(createDateTime: Long): String {
    val nowDateTime = Calendar.getInstance().timeInMillis //현재 시간 to millisecond
    var value = ""
    val differenceValue = nowDateTime - createDateTime //현재 시간 - 비교가 될 시간
    when {
        differenceValue < 60000 -> { //59초 보다 적다면
            value = "방금 전"
        }
        differenceValue < 3600000 -> { //59분 보다 적다면
            value = TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
        }
        differenceValue < 86400000 -> { //23시간 보다 적다면
            value = TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
        }
        else -> {
            val format = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm")
            return format.format(Date(createDateTime))
        }
    }
    return value
}
