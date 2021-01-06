package com.example.excrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.excrud.databinding.ItemMemoBinding

class MemoAdapter(private val memoList : ArrayList<Memo>) :
RecyclerView.Adapter<MemoAdapter.ViewHolder>(){

    class ViewHolder(viewBinding: ItemMemoBinding) : RecyclerView.ViewHolder(viewBinding.root){
        val content = viewBinding.contentText
        val date = viewBinding.dateText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoAdapter.ViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val memo = memoList[position]
        with(holder){
            content.text = memo.content
            date.text = memo.date
        }

        holder.itemView.setOnClickListener{

        }
    }

    override fun getItemCount(): Int = memoList.size
}