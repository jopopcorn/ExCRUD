package com.example.excrud

import android.app.AlertDialog
import android.content.Intent
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
            //Edit memo
            initMemo()
        } else {
            //Create new memo
            currentDate()
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val deleteItem : MenuItem = menu.findItem(R.id.action_delete)
        val exportItem : MenuItem = menu.findItem(R.id.action_export)
        deleteItem.isVisible = intent.hasExtra("content")
        exportItem.isVisible = intent.hasExtra("content")
        return true
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

    private fun currentDate() {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(Date())
        binding.dateText.text = date
    }

    private fun updateMemo() {
        if (binding.contentEditText.text.trim().toString().isEmpty() && !intent.hasExtra("date")) {
            finish()
        } else if ((intent.hasExtra("content") && binding.dateText.text != intent.extras?.get("date")
                .toString() && binding.contentEditText.text.trim().toString().isNotEmpty())
            || (!intent.hasExtra("content") && binding.contentEditText.text.trim().toString()
                .isNotEmpty())
        ) {
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
        }
        finish()
    }

    override fun onBackPressed() {
        updateMemo()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        currentDate()
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
            createShareIntent()
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

    private fun createShareIntent() {
        val sendIntent : Intent = Intent().apply{
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, binding.contentEditText.text.toString())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
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