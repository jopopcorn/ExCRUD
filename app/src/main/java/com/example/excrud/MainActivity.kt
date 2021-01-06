package com.example.excrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.excrud.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //TODO Adapter 만들어서 연결하기
        // 리싸이클러뷰 클릭 리스너 추가 & 누르면 메모 확장하고 수정 가능하게 만듦. 삭제 버튼도 안에 넣기

        val currentTime = Calendar.getInstance().time
        val currentDate = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentTime)

        val db = FirebaseFirestore.getInstance()

        val memo = hashMapOf(
            "content" to "내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 내용 블라블라 안녕하세요 ",
            "date" to currentDate
        )

        //TODO 툴바 달기. 버튼 아이콘 변경, 오른쪽 버튼 클릭시 메모 작성 화면으로 이동

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }


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
