package com.app.pub_st.dashboard

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.pub_st.databinding.RoutineItemBinding

class RoutineAdapter(
    private val values: List<RoutineItems>,
) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            RoutineItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.binding.run {
            time.text = item.time
            courseCode.text = item.course_code
            courseName.text = item.course_title
        }

    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(var binding: RoutineItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}