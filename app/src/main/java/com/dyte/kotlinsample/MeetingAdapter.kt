package com.dyte.kotlinsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class MeetingAdapter(
    private val context: Context,
    private val meetings: List<Meeting>
): RecyclerView.Adapter<MeetingAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val itemView: LinearLayout = view.findViewById(R.id.meeting_item_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.meeting_list_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.itemView.findViewById<TextView>(R.id.meetingTitle).text = meeting.title
        holder.itemView.findViewById<TextView>(R.id.roomName).text = meeting.roomName
        holder.itemView.findViewById<TextView>(R.id.button4).setOnClickListener {
            (context as MainActivity).showMeetingJoinAlert(meeting)
        }
    }

    override fun getItemCount() = meetings.size
}