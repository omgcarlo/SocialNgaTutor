package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CommentRecyclerItemViewHolder;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.EventRecyclerItemViewHolder;

import java.util.List;

public class EventRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> eventId;
    List<String> title;
    List<String> description;
    List<String> eventdate;


    public EventRecyclerAdapter(List<String> eventId,List<String> title,List<String> description,List<String> eventdate){
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.eventdate = eventdate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        return EventRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EventRecyclerItemViewHolder holder = (EventRecyclerItemViewHolder) viewHolder;

        String s_eventId = eventId.get(position);
        String s_title = title.get(position);
        String s_description = description.get(position);
        String s_eventdate = eventdate.get(position);

        holder.setEventId(s_eventId);
        holder.setEventTitle(s_title);
        holder.setEventDescription(s_description);
        holder.setEventDate(s_eventdate);
    }

    @Override
    public int getItemCount() {
        return title == null ? 0 : title.size();
    }

}
