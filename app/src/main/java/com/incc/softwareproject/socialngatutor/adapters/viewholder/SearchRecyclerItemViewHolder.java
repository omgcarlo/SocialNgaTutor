package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.services.FollowService;
import com.incc.softwareproject.socialngatutor.services.PostService;

public class SearchRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;
    private  TextView tv_isFollowing;
    private SimpleDraweeView userPP;   //  PROFILE PICTURE
    private TextView nameSaUser; // Full name of the user

    private TextView userId; // Id sa user
    //private String sessionId;// IMONG ID
    private Context context;

    private Button follow_btn;
    private Button following_btn;

    private boolean isFollowed;


    public SearchRecyclerItemViewHolder(final View parent, TextView tv_username,
                                        TextView fullname, TextView userId,TextView isFollowing,
                                        Button follow_btn,Button  following_btn,SimpleDraweeView userpp) {
        super(parent);
        //INIT FIELDS
        this.tv_username = tv_username;
        this.tv_fullname = fullname;
        this.userId = userId;
        this.tv_isFollowing = isFollowing;
        this.follow_btn = follow_btn;
        this.following_btn = following_btn;

        userPP = userpp;
        nameSaUser = (TextView) parent.findViewById(R.id.sp_card_fullname);

        //  SETTING LISTENER TO FULLNAME AND USERNAME
        userPP.setOnClickListener(this);
        nameSaUser.setOnClickListener(this);
        //  Following/follow btn
        follow_btn.setOnClickListener(this);
        following_btn.setOnClickListener(this);

        parent.setOnClickListener(this);

        context =  parent.getContext();
    }

    public static SearchRecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.sp_card_fullname);
        TextView username = (TextView) parent.findViewById(R.id.sp_card_username);
        TextView userId = (TextView) parent.findViewById(R.id.sp_userId);
        TextView isFollowing = (TextView) parent.findViewById(R.id.sp_followers);
        Button follow_btn = (Button) parent.findViewById(R.id.sp_followBtn);
        Button following_btn = (Button) parent.findViewById(R.id.sp_followingBtn);
        SimpleDraweeView userPP = (SimpleDraweeView) parent.findViewById(R.id.sp_card_ppicture);
        return new SearchRecyclerItemViewHolder(parent,username,fullname,userId,isFollowing,follow_btn,following_btn,userPP);
    }

    public void setFullname(CharSequence text) {
        tv_fullname.setText(text);
    }
    public void setUserName(CharSequence text) {
        tv_username.setText("@" + text);
    }
    public void setUserId(CharSequence text) {
        userId.setText(text);
    }
    public void setIsFollowed(boolean isFollowed) {
        //tv_isFollowing.setText(isFollowed + "");
        if(isFollowed){
            follow_btn.setVisibility(View.GONE);
            following_btn.setVisibility(View.VISIBLE);
            tv_isFollowing.setText("true");
        }  else{
            follow_btn.setVisibility(View.VISIBLE);
            following_btn.setVisibility(View.GONE);
            tv_isFollowing.setText("false");
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sp_card_username || v.getId() == R.id.sp_card_fullname || v.getId() == R.id.sp_card_ppicture) {
                // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("UserId", userId.getText().toString());
            context.startActivity(i);
        }
        else{
            //follow process
            Intent intent = new Intent(context, FollowService.class);
            if(v.getId() == R.id.sp_followBtn) {
                Log.d("Button","follow");
                intent.putExtra("follow", "follow");
                //ANIMATE FOLLOWING BUTTON
                follow_btn.setVisibility(View.GONE);
                following_btn.setVisibility(View.VISIBLE);
                tv_isFollowing.setText("true");

            }else{
                intent.putExtra("follow", "unfollow");
                //ANIMATE FOLLOW BUTTON
                follow_btn.setVisibility(View.VISIBLE);
                following_btn.setVisibility(View.GONE);
                tv_isFollowing.setText("false");
            }
            //pass iyang id
            intent.putExtra("iyangId", userId.getText().toString());
            context.startService(intent);
        }
    }


    public void setPicUrl(String picUrl) {
       userPP.setImageURI(Uri.parse(picUrl));
    }
}
