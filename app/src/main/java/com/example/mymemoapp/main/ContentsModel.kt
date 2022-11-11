package com.example.mymemoapp.main

data class ContentsModel(

    //글의 ID
    var memoId: String = "",

    //글의 메세지
    var contents: String= "",

    //글이 쓰여진 시간
    var writeTime: String="",

    //글의 색깔
    var color: String= ""
)
