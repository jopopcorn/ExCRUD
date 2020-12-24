package com.example.excrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.excrud.databinding.ActivityEditBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), TextWatcher {
    companion object {
        private val TAG = "EditActivity"
    }

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO create 모드일 때 toolbar menu icon save로 세팅,
        // edit 모드일 때 DB에서 정보 가져오고(READ) toolbar 아이콘 delete로 세팅
        updateDate()

        binding.contentEditText.addTextChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            //TODO 입력한 메모 정보 DB에 저장하는 코드 넣기
            updateMemo()
            setVisible(false)
            true
        }
        R.id.action_delete -> {
            //TODO 삭제할건지 묻는 다이얼로그 띄우기 deleteDialog (AlertDialog 사용)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun updateDate() {
        val currentTime = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA).format(currentTime)

        binding.dateText.text = date
    }

    private fun updateMemo() {
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "title" to binding.titleEditText.text,
            "content" to binding.contentEditText.text,
            "date" to binding.dateText.text
        )

        db.collection("memos")
            .document("user1")
            .set({ data })
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error and document", e)
            }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        updateDate()
    }

    override fun afterTextChanged(p0: Editable?) {

    }
}