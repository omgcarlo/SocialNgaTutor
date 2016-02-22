package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.ActivityRecyclerItemViewHolder;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.NoteRecyclerItemViewHolder;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> fullname;
    private List<String> userId;
    private List<String> note_description;
    private List<String> datetime;
    private List<String> file_url;
    private List<String> file_description;

    public NoteRecyclerAdapter(List<String> fullname,List<String> userId,
                               List<String> note_description,List<String> file_description,
                               List<String> datetime, List<String> file_url ) {

        this.fullname = fullname;
        this.userId = userId;
        this.note_description = note_description;
        this.datetime = datetime;
        this.file_url = file_url;
        this.file_description = file_description;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        return NoteRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NoteRecyclerItemViewHolder holder = (NoteRecyclerItemViewHolder) viewHolder;


        String s_fullname = fullname.get(position);
        String s_userId = userId.get(position);
        String s_ndescription = note_description.get(position);
        String s_datetime = datetime.get(position);
        String s_file_url = file_url.get(position);
        String s_fdescription = file_description.get(position);


        holder.setFullname(s_fullname);
        holder.setFUserId(s_userId);
        holder.setNDescription(s_ndescription);
        holder.setFDescription(s_fdescription);
        holder.setDatetime(s_datetime);
        holder.setFileUrl(s_file_url);
    }

    @Override
    public int getItemCount() {
        return fullname == null ? 0 : fullname.size();
    }

}
