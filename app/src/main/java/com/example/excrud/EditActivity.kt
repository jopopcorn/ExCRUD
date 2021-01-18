package com.example.excrud

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.excrud.databinding.ActivityEditBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), TextWatcher {
    companion object {
        private const val TAG = "EditActivity"
    }

    private lateinit var binding: ActivityEditBinding
    private val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection("memos")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        if (intent.hasExtra("content")) {
            initMemo()
        } else {
            updateDate()
        }

    }


    private fun initView() {
        initToolbar()
        binding.contentEditText.addTextChangedListener(this)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initMemo() {
        binding.contentEditText.setText(intent.extras?.get("content").toString())
        binding.dateText.text = intent.extras?.get("date").toString()
    }

    private fun updateDate() {
        val currentTime = Calendar.getInstance().time
        val date = SimpleDateFormat("yyyy년 MM월 dd일 HH:mm", Locale.KOREA).format(currentTime)

        binding.dateText.text = date
    }

    private fun updateMemo() {
        val data = Memo(intent.extras?.get("id") as Int).apply {
            content = binding.contentEditText.text.toString()
            date = binding.dateText.text.toString()
            bookmark = intent.getBooleanExtra("bookmark", false)
        }

        docRef.document("${data.id}")
            .set(data)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error and document", e)
            }

        finish()
    }

    override fun onBackPressed() {
        updateMemo()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        updateDate()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            updateMemo()
            true
        }
        R.id.action_export -> {
            //TODO 공유하는 bottomSheet 추가
            true
        }
        R.id.action_delete -> {
            showDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showDialog() {
        val dialog: AlertDialog.Builder =
            AlertDialog.Builder(
                this,
                android.R.style.Theme_Material_Light_Dialog_NoActionBar_MinWidth
            )

        dialog.apply {
            setMessage("메모를 삭제하시겠습니까?")
            setPositiveButton("삭제") { dialog, _ ->
                deleteMemo()
                dialog.dismiss()
                finish()
            }
            setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
        }

        dialog.show()
    }

    private fun deleteMemo() {
        docRef.document("${intent.extras?.get("id") as Int}")
            .delete()
            .addOnSuccessListener {
                Log.d(
                    TAG,
                    "DocumentSnapshot successfully deleted!"
                )
                Toast.makeText(this, "데이터를 성공적으로 삭제했습니다.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }

}