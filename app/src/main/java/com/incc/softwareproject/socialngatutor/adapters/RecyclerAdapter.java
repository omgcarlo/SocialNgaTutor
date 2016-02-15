package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.RecyclerItemViewHolder;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> fullname;
    private List<String> username;
    private List<String> post;
    private List<String> postId;
    private List<String> userId;
    private List<String> datetime;
    private List<String> pp_url;
    private List<Boolean> owned;
    public RecyclerAdapter(List<String> itemList,List<String> itemList2,
                           List<String> itemList3,List<String> itemList4,
                           List<String> itemList5,List<String> datetime,List<String> pp_url, List<Boolean> owned){

        // Why the diffirent names? Kay maglibog oks nya
        fullname = itemList;
        username = itemList2;
        post = itemList3;
        postId = itemList4;
        userId = itemList5;
        this.datetime = datetime;
        this.pp_url = pp_url;
        this.owned = owned;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        //  s_ means variable is single :) CHAR
        String s_fullname = fullname.get(position);
        String s_username = username.get(position);
        String s_post = post.get(position);
        String s_userId = userId.get(position);
        String s_postId = postId.get(position);
        String s_datetime = datetime.get(position);
        String s_pp_url = pp_url.get(position);
        boolean s_owned = owned.get(position);

        holder.setFullname(s_fullname);
        holder.setUserName(s_username);
        holder.setPostDetails(s_post );
        holder.setUserId(s_userId);
        holder.setPostId(s_postId);
        holder.setDateTime(s_datetime);
        holder.setProfilePicture(s_pp_url);
        holder.setOwned(s_owned);
    }

    @Override
    public int getItemCount() {
        return fullname == null ? 0 : fullname.size();
    }

}
