package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.incc.softwareproject.socialngatutor.PostViewActivity;
import com.incc.softwareproject.socialngatutor.ProfileActivity;
import com.incc.softwareproject.socialngatutor.R;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

public class NoteRecyclerItemViewHolder extends AnimateViewHolder implements View.OnClickListener {
    private Context context;

    private TextView tv_fullname;
    private TextView tv_file_description;
    private TextView tv_note_description;
    private TextView tv_datetime;

    private String file_url;
    private String userId;

    public NoteRecyclerItemViewHolder(final View parent, TextView fullname,
                                      TextView file_description, TextView note_description, TextView datetime) {
        super(parent);
        this.tv_fullname = fullname;
        this.tv_file_description = file_description;
        this.tv_note_description = note_description;
        this.tv_datetime = datetime;

        context =  parent.getContext();
    }

    public static NoteRecyclerItemViewHolder newInstance(View parent) {
        TextView fullname = (TextView) parent.findViewById(R.id.note_full_name);
        TextView file_description = (TextView) parent.findViewById(R.id.note_file_description);
        TextView note_description = (TextView) parent.findViewById(R.id.note_description);
        TextView datetime = (TextView) parent.findViewById(R.id.note_time);

        return new NoteRecyclerItemViewHolder(parent,fullname,file_description,note_description,datetime);
    }

    @Override
    public void onClick(View v) {

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

    public void setFullname(String fullname) {
        this.tv_fullname.setText(fullname);
    }

    public void setFUserId(String FUserId) {
        this.userId = FUserId;
    }

    public void setNDescription(String NDescription) {
        this.tv_note_description.setText(NDescription);
    }

    public void setFDescription(String FDescription) {
        this.tv_file_description.setText(FDescription);
    }

    public void setDatetime(String datetime) {
        this.tv_datetime.setText(datetime);
    }

    public void setFileUrl(String fileUrl) {
        this.file_url = fileUrl;
    }
}
