package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.CommentActivity;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;

import org.w3c.dom.Text;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;
    private final TextView tv_post; //  or description
    private final TextView tv_datetime;
    private final ImageButton comment;
    private final ImageButton upvote;
    private Context context;
    private String userId;
    private String postId;

    public RecyclerItemViewHolder(final View parent, TextView tv_username,
                                  TextView tv_post, TextView fullname,
                                  ImageButton comment,ImageButton upvote,TextView tv_datetime) {
        super(parent);
        //  TV = TEXTVIEW
        this.tv_username = tv_username;
        this.tv_post = tv_post;
        this.tv_fullname = fullname;
        this.tv_datetime = tv_datetime;

        // BUTTONS/IMAGEBUTTONS
        this.comment = comment;
        this.upvote = upvote;

        //  WHEN THE USER TOUCHES THE USERNAME OR FULLNAME
        this.tv_fullname.setOnClickListener(this);
        this.tv_username.setOnClickListener(this);

        //  WHEN THE USER TOUCHES THE POST
        this.tv_post.setOnClickListener(this);

        //  SET LISTENER SA COMMENT UG UPVOTE
        this.comment.setOnClickListener(this);
        this.upvote.setOnClickListener(this);

        parent.setOnClickListener(this);
        context =  parent.getContext();
    }

    public static RecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.card_fullname);
        TextView username = (TextView) parent.findViewById(R.id.card_username);
        TextView post = (TextView) parent.findViewById(R.id.card_post_details);
        TextView datetime = (TextView) parent.findViewById(R.id.card_datetime);

        ImageButton comment = (ImageButton) parent.findViewById(R.id.pt_commentBtn);    //PT = PosT
        ImageButton upvote = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn);

        //ImageButton share = (ImageButton) parent.findViewById(R.id.pt_shareBtn);
        return new RecyclerItemViewHolder(parent,username,post,fullname,comment,upvote,datetime);
    }

    public void setFullname(CharSequence text) {
        tv_fullname.setText(text);
    }
    public void setUserName(CharSequence text) {
        tv_username.setText("@" + text);
    }
    public void setPostDetails(CharSequence text) {
        tv_post.setText(text);
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public void setDateTime(String dateTime) {
        this.tv_datetime.setText(dateTime);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == tv_fullname.getId() || v.getId() == tv_username.getId()){
            // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("Username", tv_username.getText().toString());
            i.putExtra("FullName", tv_fullname.getText().toString());
            i.putExtra("UserId",userId);
            i.putExtra("isFollowed","true");    //  ASSUMING NGA TANAN POST KAY GIKAN SA GIPANG FOLLOW NIYA
            context.startActivity(i);
        }
        else if(v.getId() == comment.getId()){
            // OPEN COMMENT ACTIVITY
            Intent i = new Intent(context, CommentActivity.class);
            i.putExtra("PostId", postId);
            context.startActivity(i);
        }
       /* else if(v.getId() == R.id.pt_shareBtn){

        }*/
        else if(v.getId() == upvote.getId()){
            upvote.setBackgroundColor(Color.parseColor("#BBDEFB"));
            upvote.getDrawable().setAlpha(60);
        }
        else {
            //GOTO VIEW POST
            Intent i = new Intent(context, PostViewActivity.class);
            i.putExtra("PostId", postId);
            context.startActivity(i);

        }
    }



}
