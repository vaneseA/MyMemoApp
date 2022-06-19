package com.example.mymemoapp

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    lateinit var mContext : Context

    lateinit var addBtn : ImageView

    lateinit var titleTxt : TextView

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this

//        액션바가 존재하는지 확인후(?) > 있다면 let으로 함수 진행
        supportActionBar?.let {
            setCustomActionBar()
        }
    }

    abstract fun setupEvents()
    abstract fun setValues()

    fun setCustomActionBar () {
//        기본 액션바를 담아줄 변수 생성
        val defaultActionBar = supportActionBar!!

//        커스텀 모드로 변경 > 우리가 만든 xml로 적용
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultActionBar.setCustomView(R.layout.my_custom_actionbar)

//        양 옆의 여백 제거 -> 모든 영역이 커스텀 뷰
        val myToolbar = defaultActionBar.customView.parent as androidx.appcompat.widget.Toolbar
        myToolbar.setContentInsetsAbsolute(0,0)

        addBtn = defaultActionBar.customView.findViewById<ImageView>(R.id.addBtn)
        titleTxt = defaultActionBar.customView.findViewById(R.id.titleTxt)
        addBtn.setOnClickListener {
            MemoActivity()

//           메모장 새페이지
        }
    }

}