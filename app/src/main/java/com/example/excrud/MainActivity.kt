package com.example.excrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
//        binding.memoRecyclerview
        setContentView(binding.root)
        //TODO Adapter 만들어서 연결하기
        // 클릭 리스너 추가하고 누르면 메모 확장하고 수정 가능하게 만듦. 삭제 버튼도 안에 넣기

        val currentTime = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentTime)

        val db = FirebaseFirestore.getInstance()
        val memo = hashMapOf(
            "title" to "제목1",
            "content" to "내용 블라블라 안녕하세요 저는 팝콘 주인 조수입닌다 반가방가룽~ 일요일에 공부하니 기분이 좃내요 ㅋㅋ",
            "date" to date
        )

        binding.addMemoBtn.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }




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
}
