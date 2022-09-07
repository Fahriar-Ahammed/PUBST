package com.app.pub.attendance

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.pub_st.databinding.RoutineItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AttendanceAdapter(
    private val values: List<AttendanceItems>,
) : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {

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
            time.text = item.date
        }

    }

    override fun getItemCount(): Int = values.size

    class ViewHolder(var binding: RoutineItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}