package com.dyte.kotlinsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
* A helper class to build an adapter to populate
* an Android Recycler View with the list of Dyte meetings.
* */
// TODO : use view binding here
class MeetingAdapter(
    private val context: Context,
    private val meetings: List<Meeting>
): RecyclerView.Adapter<MeetingAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.meeting_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meeting = meetings[position]
        // Populate each list item with the meeting title, room name, and
        // join the corresponding meeting on clicking the button.
        holder.itemView.findViewById<TextView>(R.id.meetingTitle).text = meeting.title
        holder.itemView.findViewById<TextView>(R.id.roomName).text = meeting.roomName
        holder.itemView.findViewById<TextView>(R.id.joinMeetingButton).setOnClickListener {
            (context as MainActivity).showMeetingJoinAlert(meeting)
        }
    }

    override fun getItemCount() = meetings.size
}