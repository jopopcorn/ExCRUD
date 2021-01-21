package com.example.excrud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.excrud.databinding.ItemMemoBinding
import kotlin.collections.ArrayList

class MemoAdapter(private val memoList: ArrayList<Memo>, private val onClick: (Memo) -> Unit) :
    RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    class ViewHolder(viewBinding: ItemMemoBinding, val onClick: (Memo) -> Unit) :
        RecyclerView.ViewHolder(viewBinding.root) {
        private val content = viewBinding.contentText
        private val date = viewBinding.dateText
        private var currentMemo: Memo? = null

        init {
            itemView.setOnClickListener {
                currentMemo?.let {
                    onClick(it)
                }
            }
        }

        fun bind(memo: Memo) {
            currentMemo = memo
            content.text = memo.content
            date.text = memo.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memoList[position]
        holder.bind(memo)
    }

    override fun getItemCount(): Int = memoList.size
}