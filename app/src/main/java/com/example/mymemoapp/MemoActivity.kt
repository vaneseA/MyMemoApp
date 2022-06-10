package com.example.mymemoapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_memo.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat
import java.util.*

class MemoActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase
    var txt: String = ""
    var time: String = ""
    var no :Int = 0
    var color : String = "#ffF1EAAD"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo)
        setSupportActionBar(toolbar_memo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)




        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase
        if (intent.getStringExtra("txt") != null) {
            txt = intent.getStringExtra("txt")!!

        }
        if (intent.getStringExtra("time") != null) {
            time = intent.getStringExtra("time")!!
        }
        if (intent.getStringExtra("color") != null) {
            color = intent.getStringExtra("color")!!
            etMemo.setBackgroundColor(Color.parseColor(color))
        }
        if (intent != null){
            no = intent.getIntExtra("no", 0)
        }
        if (txt != null) {
            txt = txt.replace("||", "\n")
            etMemo.setText(txt)
        }
    }

    @SuppressLint("ResourceAsColor")
    fun clickColor(view: View) {
        when (view.id) {
            R.id.color1 -> {
                etMemo.setBackgroundColor(Color.parseColor("#ffF1EAAD"))
                color = "#ffF1EAAD"
            }
            R.id.color2 -> {
                etMemo.setBackgroundColor(Color.parseColor("#ffE3A8ED"))
                color = "#ffE3A8ED"
            }
            R.id.color3 -> {
                etMemo.setBackgroundColor(Color.parseColor("#ffA9D2F3"))
                color = "#ffA9D2F3"
            }
            R.id.color4 -> {
                color = "#ffA8EFE8"
                etMemo.setBackgroundColor(Color.parseColor(color))
            }
            R.id.color5 -> {
                etMemo.setBackgroundColor(Color.parseColor("#ffF3A6C0"))
                color = "#ffF3A6C0"
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SimpleDateFormat")
    fun clickSave(view: View) {
        var txt :String = etMemo.text.toString();
        var re = "\n".toRegex()
        txt = txt.replace(re,"||" )
        var query : String = if (no == 0) {
            "INSERT INTO mytable('txt', 'time', 'color') values('$txt','" + SimpleDateFormat(
                "yyyy.MM.dd HH:mm:ss"
            ).format(Date()) + "', '$color')"

        } else{
            "UPDATE mytable SET txt='$txt', time='" + SimpleDateFormat(
                "yyyy.MM.dd HH:mm:ss"
            ).format(Date()) + "', color='$color' WHERE _id='"+ no + "'"
        }
            database.execSQL(query)
            finish()
    }
}