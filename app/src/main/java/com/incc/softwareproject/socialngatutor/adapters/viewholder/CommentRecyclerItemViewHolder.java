package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.ReportActivity;
import com.incc.softwareproject.socialngatutor.services.ApproveService;
import com.incc.softwareproject.socialngatutor.services.DeleteService;

public class CommentRecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;
    private  TextView tv_isFollowing;
    private TextView tv_comment;
    private TextView userId;              // Id sa user
    private SimpleDraweeView userPP;     //  PROFILE PICTURE
    private ImageButton options;        //  option button
    private TextView datetime;
    private Context context;
    private TextView indicator;
    private ImageButton approved;

    private String schoolId;
    SharedPreferences spreferences;
    private String commentId;
    private boolean isApproved;
    private boolean owned;

    public CommentRecyclerItemViewHolder(final View parent, TextView tv_username,
                                         TextView fullname, TextView userId, TextView isFollowing,
                                         TextView comment, SimpleDraweeView userPP, ImageButton options,
                                         TextView datetime, TextView indicator, ImageButton approved) {
        super(parent);
        this.tv_username = tv_username;;
        this.tv_fullname = fullname;
        this.userId = userId;
        this.tv_isFollowing = isFollowing;
        this.tv_comment = comment;
        this.userPP = userPP;
        this.options = options;
        this.datetime = datetime;
        this.indicator = indicator;
        this.approved = approved;
        //  SETTING LISTENER TO FULLNAME AND USERNAME
         userPP.setOnClickListener(this);
         tv_fullname.setOnClickListener(this);
         options.setOnClickListener(this);

         parent.setOnClickListener(this);

        context =  parent.getContext();
        spreferences = context.getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");
    }

    public static CommentRecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.c_fullname);
        TextView username = (TextView) parent.findViewById(R.id.c_username);
        TextView userId = (TextView) parent.findViewById(R.id.c_userId);
        TextView isFollowing = (TextView) parent.findViewById(R.id.c_isFollowing);
        TextView comment = (TextView) parent.findViewById(R.id.c_comment);
        TextView datetime = (TextView) parent.findViewById(R.id.c_time);
        TextView indicator = (TextView) parent.findViewById(R.id.c_indicator);
        ImageButton options = (ImageButton) parent.findViewById(R.id.c_options);
        ImageButton approved = (ImageButton) parent.findViewById(R.id.c_approve);
        SimpleDraweeView userPP = (SimpleDraweeView) parent.findViewById(R.id.c_ppicture);

        return new CommentRecyclerItemViewHolder(parent,username,fullname,userId,isFollowing,
                comment,userPP,options,datetime,indicator,approved);
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
        //Log.e("Usertype", userType);
        switch (userType) {
            case "S":
                indicator.setVisibility(View.GONE);
                break;
            case "A":
                indicator.setText("A");
                break;
            default:
                indicator.setText("T");
                indicator.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
        if(isApproved)
            approved.setVisibility(View.VISIBLE);
        else
            approved.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.c_username || v.getId() == R.id.c_fullname || v.getId() == R.id.c_ppicture) {
            // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("UserId", userId.getText().toString());
            //i.putExtra("isFollowed", tv_isFollowing.getText().toString());
            context.startActivity(i);
        }
        else if(v.getId() == options.getId()){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context,options);
            //Inflating the Popup using xml file
            if(schoolId.equals(userId.getText().toString())) {
                popup.getMenuInflater()
                        .inflate(R.menu.comment_menu_options, popup.getMenu());
            }
            else if(isApproved && owned){
                popup.getMenuInflater()
                        .inflate(R.menu.comment_menu_options3, popup.getMenu());
            }
            else if(!isApproved && owned){
                popup.getMenuInflater()
                        .inflate(R.menu.comment_menu_options2, popup.getMenu());
            }
            else{
                popup.getMenuInflater()
                        .inflate(R.menu.comment_menu_option, popup.getMenu());
            }

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Report")){
                        Intent i = new Intent(context, ReportActivity.class);
                        i.putExtra("CommentId",commentId);
                        i.putExtra("ReporterId",schoolId);
                        i.putExtra("ReferenceTable","comment");
                        context.startActivity(i);
                    }
                    else if(item.getTitle().equals("Approve")){
                        Intent i = new Intent(context, ApproveService.class);
                        i.putExtra("Action","approve");
                        i.putExtra("CommentId",commentId);
                        context.startService(i);
                    }
                    else if(item.getTitle().equals("Disapprove")){
                        Intent i = new Intent(context, ApproveService.class);
                        i.putExtra("Action","disapprove");
                        i.putExtra("CommentId",commentId);
                        context.startService(i);
                    }
                    else{
                        Intent i = new Intent(context, DeleteService.class);
                        i.putExtra("Action","comment");
                        i.putExtra("CommentId",commentId);
                        context.startService(i);
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        }
    }


    public void setPicUrl(String picUrl) {
        Uri uri = Uri.parse(picUrl);
        userPP.setImageURI(uri);
    }

    public void setDateTime(String dateTime) {
        datetime.setText(dateTime);
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }
}
