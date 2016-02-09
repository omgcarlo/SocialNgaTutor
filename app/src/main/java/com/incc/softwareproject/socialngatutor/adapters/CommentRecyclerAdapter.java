package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CommentRecyclerItemViewHolder;

import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> fullname;
    private List<String> username;
    private List<String> userId;
    private List<Boolean> isApproved;
    private List<String> comment;
    private List<String> userType;

    public CommentRecyclerAdapter(List<String> itemList, List<String> itemList2,
                                  List<String> itemList3, List<Boolean> item4,
                                  List<String> item5, List<String> item6){

        fullname = itemList;
        username = itemList2;
        userId = itemList3;
        isApproved = item4;
        comment = item5;
        userType = item6;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.output_comment, parent, false);
        return CommentRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommentRecyclerItemViewHolder holder = (CommentRecyclerItemViewHolder) viewHolder;

        String s_fullname = fullname.get(position);
        String s_username = username.get(position);
        String s_userId = userId.get(position);
        boolean s_isApproved = isApproved.get(position);
        String s_comment = comment.get(position);
        String s_usertype = userType.get(position);

        holder.setFullname(s_fullname);
        holder.setUserName(s_username);
        holder.setUserId(s_userId);
        holder.setIsApproved(s_isApproved);
        holder.setComment(s_comment);
        holder.setUserType(s_usertype);
    }

    @Override
    public int getItemCount() {
        return fullname == null ? 0 : fullname.size();
    }

}
