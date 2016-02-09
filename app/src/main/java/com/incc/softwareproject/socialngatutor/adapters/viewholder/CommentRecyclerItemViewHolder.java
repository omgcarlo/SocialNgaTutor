package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.services.FollowService;

import org.w3c.dom.Text;

public class CommentRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;

    private  TextView tv_isFollowing;
    private TextView nameSaUser; // Full name of the user
    private TextView tv_comment;
    private TextView userId; // Id sa user
    private ImageView userPP;   //  PROFILE PICTURE

    //private String sessionId;// IMONG ID
    private Context context;

    private boolean isApproved;
    private String userType;

    public CommentRecyclerItemViewHolder(final View parent, TextView tv_username,
                                         TextView fullname, TextView userId, TextView isFollowing, TextView comment,ImageButton userPP) {
        super(parent);
        this.tv_username = tv_username;;
        this.tv_fullname = fullname;
        this.userId = userId;
        this.tv_isFollowing = isFollowing;
        this.tv_comment = comment;
        this.userPP = userPP;
        //  SETTING LISTENER TO FULLNAME AND USERNAME
        //userPP.setOnClickListener(this);
        //nameSaUser.setOnClickListener(this);
       // parent.setOnClickListener(this);
        context =  parent.getContext();
    }

    public static CommentRecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.c_fullname);
        TextView username = (TextView) parent.findViewById(R.id.c_username);
        TextView userId = (TextView) parent.findViewById(R.id.c_userId);
        TextView isFollowing = (TextView) parent.findViewById(R.id.c_isFollowing);
        TextView comment = (TextView) parent.findViewById(R.id.c_comment);
        ImageButton userPP = (ImageButton) parent.findViewById(R.id.c_userPP);
        return new CommentRecyclerItemViewHolder(parent,username,fullname,userId,isFollowing,comment,userPP);
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
            tv_isFollowing.setText("true");
        }  else{
            tv_isFollowing.setText("false");
        }
    }
    public void setComment(String comment) {
        tv_comment.setText(comment);
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.c_username || v.getId() == R.id.c_fullname || v.getId() == R.id.c_ppicture) {
                // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("Username", tv_username.getText().toString());
            i.putExtra("FullName", tv_fullname.getText().toString());
            i.putExtra("UserId", userId.getText().toString());
            //i.putExtra("isFollowed", tv_isFollowing.getText().toString());
            context.startActivity(i);
        }
        else{
            //APPROVED COMMENT HERE
        }
    }



}
