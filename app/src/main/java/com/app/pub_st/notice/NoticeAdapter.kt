package com.app.pub_st.notice

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.pub_st.databinding.NoticeItemBinding

class NoticeAdapter(
    private val values: List<NoticeItems>,
) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            NoticeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.binding.run {
            noticeTitle.text = item.title
            noticeDate.text = item.date
        }

    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(var binding: NoticeItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}