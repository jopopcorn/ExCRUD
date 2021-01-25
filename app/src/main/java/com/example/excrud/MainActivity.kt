package com.example.excrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.excrud.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var memoViewManager: RecyclerView.LayoutManager
    private var memoList: ArrayList<Memo> = arrayListOf()
    private lateinit var memoAdapter: MemoAdapter
    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection("memos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        initToolbar()
        listenToDocument()
        initRecyclerView()
        initFAB()
    }

    private fun initFAB() {
        binding.fab.setOnClickListener {
            val createMemoIntent = Intent(this, EditActivity()::class.java).apply {
                putExtra("id", (memoList.maxOf { x -> x.id }) + 1)
            }
            startActivity(createMemoIntent)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initRecyclerView() {
        memoViewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        memoAdapter = MemoAdapter(memoList) { memo -> adapterOnClick(memo) }
        binding.memoRecyclerview.apply {
            layoutManager = memoViewManager
            adapter = memoAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, R.drawable.line_divider, 20))
        }
    }

    private fun adapterOnClick(memo: Memo) {
        val memoIntent = Intent(this, EditActivity()::class.java).apply {
            putExtra("id", memo.id)
            putExtra("content", memo.content)
            putExtra("date", memo.date)
            putExtra("bookmark", memo.bookmark)
        }
        startActivity(memoIntent)
    }

    private fun listenToDocument() {
        memoList.clear()
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                snapshot.documentChanges.forEach { item ->
                    when (item.type) {
                        DocumentChange.Type.ADDED -> {
                            memoList.add(
                                item.document.toObject(Memo::class.java)
                            )
                            memoList.sort()
                            memoAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.MODIFIED -> {

                            (0 until memoList.size)
                                .filter { memoList[it].id == item.document.id.toInt() }
                                .forEach { memoList.removeAt(it) }


                            memoList.add(item.document.toObject(Memo::class.java))
                            memoAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED -> {
                            memoList.remove(item.document.toObject(Memo::class.java))
                            memoAdapter.notifyDataSetChanged()
                        }
                    }
                }
            } else {
                Log.d(TAG, "snapshot data null")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                //TODO 다중 삭제 / 북마크 (즐찾) 가능한 메서드
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
