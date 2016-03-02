package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.CommentActivity;
import com.incc.softwareproject.socialngatutor.EditPostActivity;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.ReportActivity;
import com.incc.softwareproject.socialngatutor.ShareActivity;
import com.incc.softwareproject.socialngatutor.services.CommentService;
import com.incc.softwareproject.socialngatutor.services.DeleteService;
import com.incc.softwareproject.socialngatutor.services.PostService;
import com.incc.softwareproject.socialngatutor.services.UpvoteService;

import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class RecyclerItemViewHolder extends AnimateViewHolder implements View.OnClickListener {

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

    private LinearLayout fileCV;
    private String fileUrl;
    private TextView file;
    //  SHARE
    private String share_postId;
    private LinearLayout share_container;
    private SimpleDraweeView share_profilePicture;
    private TextView share_fullname;
    private TextView share_username;
    private TextView share_post_description;
    private CardView share_file_container;
    private TextView share_file_name;
    private String shareFileUrl;
    private ProgressBar share_progressbar;
    //private String sharePostId;
    private String shareUserType;

    public RecyclerItemViewHolder(final View parent, TextView tv_username,
                                  TextView tv_post, TextView fullname,
                                  ImageButton comment, ImageButton upvote, ImageButton upvote2,
                                  ImageButton share, TextView tv_datetime,
                                  SimpleDraweeView pp, ImageButton options, TextView upvotes,
                                  TextView comments, TextView shares, TextView file, LinearLayout fileCV,
                                  LinearLayout share_container, SimpleDraweeView share_profilePicture,
                                  TextView share_fullname, TextView share_username, TextView share_post_description,
                                  CardView share_file_container, TextView share_file_name,ProgressBar share_progressbar) {

        super(parent);
        //  TV = TEXTVIEW
        this.tv_username = tv_username;
        this.tv_post = tv_post;
        this.tv_fullname = fullname;
        this.tv_datetime = tv_datetime;
        this.upvotes_count = upvotes;
        this.comments = comments;
        this.shares = shares;
        this.file = file;

        // BUTTONS/IMAGEBUTTONS
        this.share = share;
        this.comment = comment;
        this.upvote = upvote;
        this.upvote2 = upvote2;

        // PROFILE PICTURE
        this.ppicture = pp;
        //  CARDVIEW
        this.fileCV = fileCV;

        //  Share Content
        this.share_container = share_container;
        this.share_profilePicture = share_profilePicture;
        this.share_fullname = share_fullname;
        this.share_username = share_username;
        this.share_post_description = share_post_description;
        this.share_file_container = share_file_container;
        this.share_file_name = share_file_name;
        this.share_progressbar = share_progressbar;

        //  WHEN THE USER TOUCHES THE USERNAME OR FULLNAME
        this.tv_fullname.setOnClickListener(this);
        this.tv_username.setOnClickListener(this);

        //  WHEN THE USER TOUCHES THE POST
        this.tv_post.setOnClickListener(this);

        //  SET LISTENER ON CLICKABLES
        this.comment.setOnClickListener(this);
        this.share.setOnClickListener(this);
        this.upvote.setOnClickListener(this);
        this.upvote2.setOnClickListener(this);
        this.fileCV.setOnClickListener(this);
        this.share_file_name.setOnClickListener(this);
        //  INIT AND SET LISTENER FOR OPTIONS
        this.options = options;

        options.setOnClickListener(this);
        parent.setOnClickListener(this);
        context = parent.getContext();
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
        TextView file = (TextView) parent.findViewById(R.id.card_file_name);
        LinearLayout fileCV = (LinearLayout) parent.findViewById(R.id.card_post_file);

        ImageButton comment = (ImageButton) parent.findViewById(R.id.pt_commentBtn);    //PT = PosT
        ImageButton upvote = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn);
        ImageButton upvote2 = (ImageButton) parent.findViewById(R.id.pt_upvoteBtn2);
        ImageButton share = (ImageButton) parent.findViewById(R.id.pt_shareBtn);

        ImageButton options = (ImageButton) parent.findViewById(R.id.card_options);

        SimpleDraweeView profilePicture = (SimpleDraweeView) parent.findViewById(R.id.card_ppicture);

        //  Share container
        LinearLayout share_container = (LinearLayout) parent.findViewById(R.id.share_card_container);
        SimpleDraweeView share_profilePicture = (SimpleDraweeView) parent.findViewById(R.id.share_card_ppicture);
        TextView share_fullname = (TextView) parent.findViewById(R.id.share_card_fullname);
        TextView share_username = (TextView) parent.findViewById(R.id.share_card_username);
        TextView share_post_description = (TextView) parent.findViewById(R.id.share_card_post_details);
        CardView share_file_container = (CardView) parent.findViewById(R.id.share_card_post_file);
        TextView share_file_name = (TextView) parent.findViewById(R.id.share_card_file_name);
        ProgressBar share_progressbar = (ProgressBar) parent.findViewById(R.id.share_progressbar);
        return new RecyclerItemViewHolder(parent, username, post, fullname, comment, upvote, upvote2, share,
                datetime, profilePicture, options, upvotes, comments, shares, file, fileCV,
                share_container, share_profilePicture, share_fullname, share_username,
                share_post_description, share_file_container, share_file_name,share_progressbar);

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

    public void setProfilePicture(String pp_url) {
        Uri imageUri = Uri.parse(pp_url);
        ppicture.setImageURI(imageUri);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == tv_fullname.getId() || v.getId() == tv_username.getId()) {
            // GOTO PROFILE WHEN NAME OR USERNAME IS CLICKED
            Intent i = new Intent(context, ProfileActivity.class);
            i.putExtra("UserId", userId);
            context.startActivity(i);
        } else if (v.getId() == R.id.pt_commentBtn) {
            Intent i = new Intent(context, CommentActivity.class);
            i.putExtra("PostId", postId);
            i.putExtra("FullName", tv_fullname.getText().toString());
            i.putExtra("isOwned",owned);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.animator.animate3, R.animator.animate2);
        } else if (v.getId() == R.id.pt_shareBtn) {
            Intent i = new Intent(context, ShareActivity.class);
            if(share_postId.equals(""))
                i.putExtra("PostId", postId);
            else
                i.putExtra("PostId", share_postId);
            i.putExtra("OwnerId", schoolId);
            context.startActivity(i);
            ((Activity) context).overridePendingTransition(R.animator.animate3, R.animator.animate2);
        } else if (v.getId() == upvote.getId()) {
            upvote.setVisibility(View.INVISIBLE);
            upvote2.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, UpvoteService.class);
            intent.putExtra("userId", schoolId);
            intent.putExtra("postId", postId);
            context.startService(intent);
        } else if (v.getId() == upvote2.getId()) {
            upvote2.setVisibility(View.INVISIBLE);
            upvote.setVisibility(View.VISIBLE);

            Intent intent = new Intent(context, UpvoteService.class);
            intent.putExtra("userId", schoolId);
            intent.putExtra("postId", postId);
            context.startService(intent);
        } else if (v.getId() == options.getId()) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context, options);
            //Inflating the Popup using xml file
            if (owned) {
                popup.getMenuInflater()
                        .inflate(R.menu.menu_options, popup.getMenu());
            } else {
                popup.getMenuInflater()
                        .inflate(R.menu.menu_options2, popup.getMenu());
            }

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getTitle().equals("Report")){
                        Intent i = new Intent(context, ReportActivity.class);
                        i.putExtra("PostId",postId);
                        i.putExtra("ReporterId",schoolId);
                        i.putExtra("ReferenceTable","post");
                        context.startActivity(i);
                    }
                    else if(item.getTitle().equals("Edit")){
                        Intent i = new Intent(context, EditPostActivity.class);
                        i.putExtra("PostId",postId);
                        context.startActivity(i);
                    }
                    else{
                        Intent i = new Intent(context, DeleteService.class);
                        i.putExtra("PostId",postId);
                        i.putExtra("Action","post");
                        context.startService(i);
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        } else if (v.getId() == fileCV.getId()) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
            context.startActivity(browserIntent);

        }
        else if(v.getId() == share_file_name.getId()){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareFileUrl));
            context.startActivity(browserIntent);
        }
        else {
            //GOTO VIEW POST
            Intent i = new Intent(context, PostViewActivity.class);
            i.putExtra("PostId", postId);
            context.startActivity(i);
        }
    }

    public void setIsUpvoted(boolean upvoted) {
        if (upvoted) {
            upvote2.setVisibility(View.VISIBLE);
            upvote.setVisibility(View.INVISIBLE);
        } else {
            upvote.setVisibility(View.VISIBLE);
            upvote2.setVisibility(View.INVISIBLE);
        }

    }

    public void setIsShared(boolean shared) {
        this.isShared = shared;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    //  ==  COUNTS  ===
    public void setUpvotes(String upvotes) {
        this.upvotes_count.setText(upvotes);
    }

    public void setShares(String shares) {
        this.shares.setText(shares);
    }

    public void setComments(String comments) {
        this.comments.setText(comments);
    }
    //===========================

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileName(String fileName) {
        if (!fileName.equals("")) {
            this.fileCV.setVisibility(View.VISIBLE);
            //this.fileCV.setBackgroundColor(Color.DKGRAY);
            this.file.setText(fileName);
        }
    }

    @Override
    public void animateAddImpl(ViewPropertyAnimatorListener listener) {
        ViewCompat.animate(itemView)
                .translationY(0)
                .alpha(1)
                .setDuration(500)
                .setListener(listener)
                .start();
    }

    @Override
    public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {

    }

    public void setSharePic(String sharePic) {
        if(sharePic.equals(""))
            Log.e("Recycler",sharePic);
            //share_container.setVisibility(View.GONE);
        else
        share_profilePicture.setImageURI(Uri.parse(sharePic));
        share_progressbar.setVisibility(View.GONE);
    }

    public void setShareUsername(String shareUsername) {
        if(shareUsername.equals(""))
            share_container.setVisibility(View.GONE);
        else
        share_username.setText(shareUsername);
    }

    public void setShareFullName(String shareFullName) {
        if(shareFullName.equals(""))
            share_container.setVisibility(View.GONE);
        else
        share_fullname.setText(shareFullName);
    }

    public void setShareDescription(String shareDescription) {
        if(shareDescription.equals(""))
            share_container.setVisibility(View.GONE);
        else
        share_post_description.setText(shareDescription);
    }

    public void setShareFileUrl(String shareFileUrl) {
        if(shareFileUrl.equals("nofile")){
            this.share_file_container.setVisibility(View.GONE);
        }
        else{
            this.share_file_container.setVisibility(View.VISIBLE);
            this.shareFileUrl = shareFileUrl;
        }

    }
    public void setShareFileName(String shareFileName) {
        share_file_name.setText(shareFileName);
    }

    public void setSharePostId(String sharePostId) {
        this.share_postId= sharePostId;
    }


    public void setShareUserType(String shareUserType) {
        this.shareUserType = shareUserType;
    }
}
