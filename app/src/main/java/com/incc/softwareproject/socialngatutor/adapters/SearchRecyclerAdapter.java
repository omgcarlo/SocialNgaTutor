package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.RecyclerItemViewHolder;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.SearchRecyclerItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> fullname;
    private List<String> username;
    private List<String> userId;
    private List<Boolean> isFollowed;
    private List<String> pic_url;
    public SearchRecyclerAdapter(List<String> itemList, List<String> itemList2,
                                 List<String> itemList3,List<Boolean> item4,List<String> pic_url){

        fullname = itemList;
        username = itemList2;
        userId = itemList3;
        isFollowed = item4;
        this.pic_url = pic_url;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
        return SearchRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SearchRecyclerItemViewHolder holder = (SearchRecyclerItemViewHolder) viewHolder;

        String s_fullname = fullname.get(position);
        String s_username = username.get(position);
        String s_userId = userId.get(position);
        boolean s_isFollowed = isFollowed.get(position);
        String s_pic_url = pic_url.get(position);

        holder.setFullname(s_fullname);
        holder.setUserName(s_username);
        holder.setUserId(s_userId);
        holder.setIsFollowed(s_isFollowed);
        holder.setPicUrl(s_pic_url);
    }

    @Override
    public int getItemCount() {
        return fullname == null ? 0 : fullname.size();
    }

}
