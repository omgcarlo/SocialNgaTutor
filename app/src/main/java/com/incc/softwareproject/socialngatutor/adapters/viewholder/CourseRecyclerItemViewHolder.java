package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.DiscoverActivity;
import com.incc.softwareproject.socialngatutor.R;

import org.w3c.dom.Text;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by carlo on 22/02/2016.
 */
public class CourseRecyclerItemViewHolder extends AnimateViewHolder implements View.OnClickListener {

    private TextView tv_name;
    private TextView tv_code;
    private TextView tv_courseNo;
    private ImageButton next;
    private String courseId;
    private Context context;
    public CourseRecyclerItemViewHolder(View parent, TextView code, TextView name, TextView courseNo, ImageButton next) {
        super(parent);
        this.tv_code = code;
        this.tv_name = name;
        this.tv_courseNo = courseNo;
        this.next = next;
        next.setOnClickListener(this);
        parent.setOnClickListener(this);
        context = parent.getContext();
    }

    public static CourseRecyclerItemViewHolder newInstance(View parent) {
        TextView code = (TextView) parent.findViewById(R.id.item_course_code);
        TextView name = (TextView) parent.findViewById(R.id.item_course_name);
        TextView courseNo = (TextView) parent.findViewById(R.id.item_course_no);

        ImageButton next = (ImageButton) parent.findViewById(R.id.item_course_next_btn);
        return new CourseRecyclerItemViewHolder(parent,code,name,courseNo,next);
    }
    @Override
    public void animateAddImpl(ViewPropertyAnimatorListener listener) {

    }

    @Override
    public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {

    }
    public void setCourseNo(String courseNo) {
        this.tv_courseNo.setText(courseNo);
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    public void setCode(String code) {
        this.tv_code.setText(code);
    }

    public void setName(String name) {
        this.tv_name.setText(name);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, DiscoverActivity.class);
        i.putExtra("Code",tv_courseNo.getText().toString());
        context.startActivity(i);
    }
}
