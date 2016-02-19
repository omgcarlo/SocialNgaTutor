package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.CommentActivity;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.ShareActivity;
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.services.UpvoteService;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;
    private final TextView tv_post; //  or description
    private final TextView tv_datetime;
    private final ImageButton comment;
    private final ImageButton share;
    private final ImageButton upvote;
    private final ImageButton upvote2;
    private SimpleDraweeView ppicture;
    private Context context;
    private String userId;
    private String postId;
    private ImageButton options;

    private String schoolId;
    private SharedPreferences spreferences;

    private Boolean owned;
    private boolean isUpvoted;
    private boolean isShared;

    private final TextView upvotes_count;
    private final TextView comments;
    private final TextView shares;
    public RecyclerItemViewHolder(final View parent, TextView tv_username,
                                  TextView tv_post, TextView fullname,
                                  ImageButton comment, ImageButton upvote,ImageButton upvote2,
                                  ImageButton share,TextView tv_datetime,
                                  SimpleDraweeView pp, ImageButton options,TextView upvotes,
                                  TextView comments,TextView shares) {

        super(parent);
        //  TV = TEXTVIEW
        this.tv_username = tv_username;
        this.tv_post = tv_post;
        this.tv_fullname = fullname;
        this.tv_datetime = tv_datetime;
        this.upvotes_count = upvotes;
        this.comments = comments;
        this.shares = shares;

        // BUTTONS/IMAGEBUTTONS
        this.share = share;
        this.comment = comment;
        this.upvote = upvote;
        this.upvote2 = upvote2;

        // PROFILE PICTURE
        this.ppicture = pp;

        //  WHEN THE USER TOUCHES THE USERNAME OR FULLNAME
        this.tv_fullname.setOnClickListener(this);
        this.tv_username.setOnClickListener(this);

        //  WHEN THE USER TOUCHES THE POST
        this.tv_post.setOnClickListener(this);

        //  SET LISTENER SA COMMENT UG UPVOTE
        this.comment.setOnClickListener(this);
        this.share.setOnClickListener(this);
        this.upvote.setOnClickListener(this);
        this.upvote2.setOnClickListener(this);
        //  INIT AND SET LISTENER FOR OPTIONS
        this.options = options;
        
        options.setOnClickListener(this);
        parent.setOnClickListener(this);
        context =  parent.getContext();
        spreferences = context.getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        schoolId = spreferences.getString("SchoolId", "Wala");



    }

    public static RecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.card_fullname);
        TextView username = (TextView) parent.findViewById(R.id.card_username);
        TextView post = (TextView) parent.findViewById(R.id.card_post_details);
        TextView datetime = (TextView) parent.findViewById(R.id.card_datetime);


        TextView upvotes = (TextView) parent.findViewById(R.id.card_upvotes_count);
        TextView comments = (TextView) parent.findViewById(R.id.card_comments);
        TextView shares = (TextView) parent.findViewById(R.id.card_shares);


        ImageButton comment = (ImageButton) parent.findViewById(R.id.pt_commentBtn);    //PT = PosT
        ImageButton upvote = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn);
        ImageButton upvote2 = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn2);
        ImageButton share = (ImageButton) parent.findViewById(R.id.pt_shareBtn);

        ImageButton options = (ImageButton) parent.findViewById(R.id.card_options);

        SimpleDraweeView profilePicture = (SimpleDraweeView) parent.findViewById(R.id.card_ppicture);

        return new RecyclerItemViewHolder(parent,username,post,fullname,comment,upvote,upvote2,share,
                                            datetime,profilePicture,options,upvotes,comments,shares);

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
    public void setProfilePicture(String pp_url){
        Uri imageUri = Uri.parse(pp_url);
        ppicture.setImageURI(imageUri);
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == tv_fullname.getId() || v.getId() == tv_username.getId()){
            // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("UserId", userId);
            context.startActivity(i);
        }
        else if(v.getId() == R.id.pt_commentBtn){
            Intent i = new Intent(context, CommentActivity.class);
            i.putExtra("PostId", postId);
            i.putExtra("FullName", tv_fullname.getText().toString());
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.animator.animate3, R.animator.animate2);
        }

        else if(v.getId() == R.id.pt_shareBtn){
            Intent i = new Intent(context, ShareActivity.class);
            i.putExtra("PostId",postId);
            i.putExtra("OwnerId",schoolId);
            context.startActivity(i);
        }
        else if(v.getId() == upvote.getId()){
            upvote.setVisibility(View.INVISIBLE);
            upvote2.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context,UpvoteService.class);
            intent.putExtra("userId",schoolId);
            intent.putExtra("postId", postId);
            context.startService(intent);
        }
        else if(v.getId() == upvote2.getId()){
            upvote2.setVisibility(View.INVISIBLE);
            upvote.setVisibility(View.VISIBLE);

            Intent intent = new Intent(context,UpvoteService.class);
            intent.putExtra("userId",schoolId);
            intent.putExtra("postId", postId);
            context.startService(intent);
        }
        else if(v.getId() == options.getId()){
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context,options);
            //Inflating the Popup using xml file
            if(owned) {
                popup.getMenuInflater()
                        .inflate(R.menu.menu_options, popup.getMenu());
            }   else{
                popup.getMenuInflater()
                        .inflate(R.menu.menu_options2, popup.getMenu());
            }

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(
                            context,
                            "You Clicked : " + item.getTitle(),
                            Toast.LENGTH_SHORT
                    ).show();
                    return true;
                }
            });

            popup.show(); //showing popup menu
        }
        else {
            //GOTO VIEW POST
            Intent i = new Intent(context, PostViewActivity.class);
            i.putExtra("PostId", postId);
            context.startActivity(i);


        }
    }
    public void setIsUpvoted(boolean upvoted){
         if(upvoted){
             upvote2.setVisibility(View.VISIBLE);
             upvote.setVisibility(View.INVISIBLE);
         }
        else{
             upvote.setVisibility(View.VISIBLE);
             upvote2.setVisibility(View.INVISIBLE);
         }

    }
    public void setIsShared(boolean shared){
        this.isShared = shared;
    }
    //  ==  COUNTS  ===
    public void setUpvotes(String upvotes){
        this.upvotes_count.setText(upvotes);
    }
    public void setShares(String shares){
        this.shares.setText(shares);
    }
    public void setComments(String comments){
        this.comments.setText(comments);
    }
    //===========================

    public void setOwned(boolean owned) {
        this.owned = owned;
    }
}
