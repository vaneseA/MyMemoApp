package com.example.mymemoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.mymemoapp.adapters.MemoAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var database: SQLiteDatabase
    lateinit var memos: ArrayList<MemoVO>
    lateinit var adapter: MemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        dbHelper = DBHelper(this, "newdb.db", null, 1)
        database = dbHelper.writableDatabase

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            var intent = Intent(this, MemoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onResume() {
//        super.onResume()
//        memos = ArrayList()
//        adapter = MemoAdapter(this, memos)
//        recycler_main.adapter = adapter
//
//        var query = "SELECT * FROM mytable"
//        var c = database.rawQuery(query, null)
//        while (c.moveToNext()) {
//            val no = c.getInt(c.getColumnIndex("_id"))
//            val txt = c.getString(c.getColumnIndex("txt")).replace("||","\n")
//            val time = c.getString(c.getColumnIndex("time"))
//            val color = c.getString(c.getColumnIndex("color"))
//
//            memos.add(MemoVO(no, txt, time, color))
//            adapter.notifyItemInserted(memos.size - 1)
//        }
//    }
}