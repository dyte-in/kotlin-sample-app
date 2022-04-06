package com.dyte.kotlinsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dyte.kotlinsample.databinding.MeetingListItemBinding

/*
* A helper class to build an adapter to populate
* an Android Recycler View with the list of Dyte meetings.
* */
class MeetingAdapter(
    private val context: Context,
    private val meetings: List<Meeting>
): RecyclerView.Adapter<MeetingAdapter.ItemViewHolder>() {

    class ItemViewHolder(private val context: Context, private val binding: MeetingListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(meeting: Meeting) {
            // Populate each list item with the meeting title, room name, and
            // join the corresponding meeting on clicking the button.
            binding.meetingTitle.text = meeting.title
            binding.roomName.text = meeting.roomName
            binding.joinMeetingButton.setOnClickListener {
                (context as MainActivity).showMeetingJoinAlert(meeting)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MeetingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.bindView(meeting)
    }

    override fun getItemCount() = meetings.size
}