package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.R;

public class ActivityRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_description;
    private final TextView tv_datetime;
    private SimpleDraweeView userPP;   //  PROFILE PICTURE

    private Context context;

    private String activity;
    private String userId;
    private String postId;
    private String FUsername;
    public ActivityRecyclerItemViewHolder(final View parent, TextView fullname,
                                          TextView description, TextView datetime, SimpleDraweeView userPP) {
        super(parent);
        this.tv_fullname = fullname;
        this.tv_description = description;
        this.tv_datetime = datetime;
        this.userPP = userPP;
        context =  parent.getContext();
        tv_description.setOnClickListener(this);
    }

    public static ActivityRecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.n_fullname);
        TextView description = (TextView) parent.findViewById(R.id.n_description);
        TextView datetime = (TextView) parent.findViewById(R.id.n_time);

       SimpleDraweeView userPP = (SimpleDraweeView) parent.findViewById(R.id.n_ppuser);  //  NOT INCLUDED YET!!!
        return new ActivityRecyclerItemViewHolder(parent,fullname,description,datetime,userPP);
    }

    @Override
    public void onClick(View v) {
        if((tv_description.getText().toString()).equals("commented on your post")){
            Intent i = new Intent(context,PostViewActivity.class);
            i.putExtra("PostId",postId);
            context.startActivity(i);
        }
    }


    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setFFullname(String FF) {
        this.tv_fullname.setText(FF);
    }

    public void setFUserId(String FUserId) {
        this.userId = FUserId;
    }

    public void setFUsername(String FUsername) {
        this.FUsername = FUsername;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setDescription(String description) {
        tv_description.setText(description);
    }

    public void setDatetime(String datetime) {
        tv_datetime.setText(datetime);
    }

    public void setUserPP(String pp_url){
        Uri uri = Uri.parse(pp_url);
        userPP.setImageURI(uri);
    }
}
