package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.services.UpvoteService;

public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView tv_fullname;
    private final TextView tv_username;
    private final TextView tv_post; //  or description
    private final TextView tv_datetime;
    private final ImageButton comment;
    private final ImageButton upvote;
    private SimpleDraweeView ppicture;
    private Context context;
    private String userId;
    private String postId;
    private ImageButton options;
    private Boolean owned;
    private String schoolId;
    private SharedPreferences spreferences;
    public RecyclerItemViewHolder(final View parent, TextView tv_username,
                                  TextView tv_post, TextView fullname,
                                  ImageButton comment, ImageButton upvote, TextView tv_datetime,
                                  SimpleDraweeView pp, ImageButton options) {
        super(parent);
        //  TV = TEXTVIEW
        this.tv_username = tv_username;
        this.tv_post = tv_post;
        this.tv_fullname = fullname;
        this.tv_datetime = tv_datetime;

        // BUTTONS/IMAGEBUTTONS
        this.comment = comment;
        this.upvote = upvote;

        // PROFILE PICTURE
        this.ppicture = pp;

        //  WHEN THE USER TOUCHES THE USERNAME OR FULLNAME
        this.tv_fullname.setOnClickListener(this);
        this.tv_username.setOnClickListener(this);

        //  WHEN THE USER TOUCHES THE POST
        this.tv_post.setOnClickListener(this);

        //  SET LISTENER SA COMMENT UG UPVOTE
        this.comment.setOnClickListener(this);
        this.upvote.setOnClickListener(this);

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

        ImageButton comment = (ImageButton) parent.findViewById(R.id.pt_commentBtn);    //PT = PosT
        ImageButton upvote = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn);
        //ImageButton share = (ImageButton) parent.findViewById(R.id.pt_shareBtn);

        ImageButton options = (ImageButton) parent.findViewById(R.id.card_options);

        SimpleDraweeView profilePicture = (SimpleDraweeView) parent.findViewById(R.id.card_ppicture);
        return new RecyclerItemViewHolder(parent,username,post,fullname,comment,upvote,datetime,profilePicture,options);
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
            i.putExtra("UserId",userId);
            context.startActivity(i);
        }
        else if(v.getId() == comment.getId()){
            // OPEN COMMENT ACTIVITY
            Intent i = new Intent(context, CommentActivity.class);
            i.putExtra("PostId", postId);
            i.putExtra("FullName",tv_fullname.getText().toString());
            context.startActivity(i);
        }
       /* else if(v.getId() == R.id.pt_shareBtn){

        }*/
        else if(v.getId() == upvote.getId()){
            upvote.setBackgroundColor(Color.parseColor("#BBDEFB"));
            upvote.getDrawable().setAlpha(60);

            Intent intent = new Intent(context,UpvoteService.class);
            intent.putExtra("ownerId",schoolId);
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


    public void setOwned(boolean owned) {
        this.owned = owned;
    }
}
