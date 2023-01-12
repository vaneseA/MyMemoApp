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


private lateinit var memoId: String

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


        val intent = Intent(context, DetailActivity::class.java)
        // 선택된 카드의 ID 정보를 intent 에 추가한다.
        intent.putExtra("memoId", memo.memoId)

        memoId = intent.getStringExtra("memoId").toString()
        val contentsText = holder.itemView.findViewById<TextView>(R.id.contentsArea)
        val timeTextView = holder.itemView.findViewById<TextView>(R.id.timeTextArea)
        val backgroundColorImageView =
            holder.itemView.findViewById<ImageView>(R.id.backgroundColorImageArea)

        // 카드에 글을 세팅
        contentsText.text = memo.contents
        // 글이 쓰여진 시간
        timeTextView.text = memo.writeTime
        // 배경색깔
        backgroundColorImageView.setBackgroundColor(Color.parseColor(memo.color.toString()))


        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            Log.d("TAG", memo.memoId)
            showDialog(memo.memoId, holder.itemView.context)
            return@setOnLongClickListener (true)
        }
    }

    // 아이템들의 총 개수 반환
    override fun getItemCount(): Int = items.size


    // 각 아이템에 데이터 넣어줌
    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

