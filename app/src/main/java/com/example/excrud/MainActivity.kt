package com.example.excrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.excrud.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var memoViewManager: RecyclerView.LayoutManager
    private var memoList : ArrayList<Memo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // TODO 리싸이클러뷰 클릭 리스너 추가 & 누르면 메모 확장하고 수정 가능하게 만듦. 삭제 버튼도 안에 넣기

        val db = FirebaseFirestore.getInstance()

        initView()

//        binding.addbtn.setOnClickListener {
//            val intent = Intent(this, EditActivity::class.java)
//            startActivity(intent)
//        }

        //문서 이름 안짓고 그냥 막 추가하는 코드
//            db.collection("memos")
//                .add(memo)
//                .addOnSuccessListener { documentReference ->
//                    Log.d(
//                        TAG,
//                        "DocumentSnapshot added with ID: ${documentReference.id}"
//                    )
//                }
//                .addOnFailureListener { e -> Log.w(TAG, "Error and document", e) }

        //create code이자 update code (똑같은 문서에 덮어쓰기하면 됨)
//            db.collection("memos").document("assistant").set(memo)

        /*
        *  유저 목록들 중 조수 유저가 메모를 추가하려고 할 때
        *  db.collection("memos").document("josu").set(memo1)
        *  이렇게 하고 memo1은 Map 유형으로 만들어서 DB에 넣으면 된다
        * */

        /*
        binding.noMemoText.setOnClickListener {
            //read code
            db.collection("memos")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        Toast.makeText(
                            this,
                            "${document.id} => ${document.data}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }

            //delete code
            db.collection("memos").document("assistant")
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!")
                    Toast.makeText(
                        this,
                        "Success!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
         }
        * */

    }

    private fun initView(){
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initRecyclerView() {
        memoViewManager = LinearLayoutManager(this)
        binding.memoRecyclerview.apply {
            layoutManager = memoViewManager
            adapter = MemoAdapter(initMemoData())
            setHasFixedSize(true)
        }
    }

    private fun getDate() : String{
        val currentTime = Calendar.getInstance().time
        return SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentTime)
    }

    private fun initMemoData() : ArrayList<Memo>{
        if(memoList.size == 0){
            val memo = Memo().apply {
                content = "내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요"
                date = getDate()
            }

            for(i in 1..20){
                memoList.add(memo)
            }
        }
        return memoList
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
