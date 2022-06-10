package com.example.mymemoapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemoapp.DBHelper
import com.example.mymemoapp.MemoActivity
import com.example.mymemoapp.MemoVO
import com.example.mymemoapp.R
import kotlinx.android.synthetic.main.recycler_item.view.*

class MemoAdapter constructor(val context: Context, val items:ArrayList<MemoVO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //val vh:VH = holder as VH //코트린에서 클래스들의 형변환 연산자 as
        holder.itemView.tvTitle.text = items[position].time
        holder.itemView.tvMsg.text = items[position].txt
        holder.itemView.containerView.setBackgroundColor(Color.parseColor(items[position].color))
    }

    @Suppress("UNREACHABLE_CODE")
    inner class VH(itemView : View) : RecyclerView.ViewHolder(itemView){

        //이렇게 find 하는 작업 필요없음. why? xml 의 id 가 바로 변수명으로 사용
        //var iv:ImageView = itemView.findViewById(R.id.iv)
        init {
            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    //클릭한 itemView의 위치
                    //val position = layoutPosition
                    val item = items[layoutPosition]

                    val intent = Intent(context, MemoActivity::class.java)
                    intent.putExtra("no", item.no)
                    intent.putExtra("txt", item.txt)
                    intent.putExtra("time", item.time)
                    intent.putExtra("color", item.color)

                    context.startActivity(intent)
                }

            })

            itemView.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    var dbHelper = DBHelper(context, "newdb.db", null, 1)
                    var database = dbHelper.writableDatabase
                    val item = items[layoutPosition]
                    val no = item.no
                    val alertDialog = AlertDialog.Builder(context)
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("네") {dialog, which ->
                            var query = "DELETE FROM mytable WHERE _id='$no'"
                            database.execSQL(query)
                            itemView.visibility = View.GONE
                        }
                        .setNegativeButton("아니오",null)
                        .create()
                    alertDialog.show()
                    return true
                }
            })
        }

    }
}