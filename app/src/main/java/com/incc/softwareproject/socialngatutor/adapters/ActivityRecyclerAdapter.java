package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.ActivityRecyclerItemViewHolder;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CommentRecyclerItemViewHolder;

import java.util.List;

public class ActivityRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> activity;
    private List<String> from_fullname;
    private List<String> from_username;
    private List<String> postId;
    private List<String> from_userId;
    private List<String> description;
    private List<String> datetime;
    private List<String> pic_url;
    private List<String> activityId;
    public ActivityRecyclerAdapter(List<String> activity, List<String> from_fullname,
                                   List<String> from_username, List<String> postId,
                                   List<String> from_userId, List<String> description,
                                   List<String> datetime,List<String> pic_url,List<String> activityId) {
        this.activity = activity;
        this.from_fullname = from_fullname;
        this.from_userId = from_userId;
        this.from_username = from_username;
        this.postId = postId;
        this.description = description;
        this.datetime = datetime;
        this.pic_url = pic_url;
        this.activityId = activityId;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_notif, parent, false);
        return ActivityRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ActivityRecyclerItemViewHolder holder = (ActivityRecyclerItemViewHolder) viewHolder;

        String s_activity = activity.get(position);
        String s_from_fullname = from_fullname.get(position);
        String s_from_userId = from_userId.get(position);
        String s_from_username = from_username.get(position);
        String s_postId = postId.get(position);
        String s_description = description.get(position);
        String s_datetime = datetime.get(position);
        String s_pic_url = pic_url.get(position);
        String s_activityId = activityId.get(position);

        holder.setActivity(s_activity);
        holder.setFFullname(s_from_fullname);
        holder.setFUserId(s_from_userId);
        holder.setFUsername(s_from_username);
        holder.setPostId(s_postId);
        holder.setDescription(s_description);
        holder.setDatetime(s_datetime);
        holder.setUserPP(s_pic_url);
        holder.setActivityId(s_activityId);

    }

    @Override
    public int getItemCount() {
        return activity == null ? 0 : activity.size();
    }

}
