package com.incc.softwareproject.socialngatutor.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.incc.softwareproject.socialngatutor.CourseActivity;
import com.incc.softwareproject.socialngatutor.R;

import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder;

/**
 * Created by carlo on 22/02/2016.
 */
public class CollegeRecyclerItemViewHolder extends AnimateViewHolder implements View.OnClickListener{
    private TextView tv_code;
    private TextView tv_name;
    private TextView tv_dean;
    private ImageButton nextBtn;
    private Context context;
    public CollegeRecyclerItemViewHolder(View parent, TextView code, TextView name, TextView dean, ImageButton next) {
        super(parent);
        tv_code = code;
        tv_name = name;
        tv_dean = dean;
        nextBtn = next;

        tv_code.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        tv_dean.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        this.context = parent.getContext();
    }
    public static CollegeRecyclerItemViewHolder newInstance(View parent) {
        TextView code = (TextView) parent.findViewById(R.id.item_college_code);
        TextView name = (TextView) parent.findViewById(R.id.item_college_name);
        TextView dean = (TextView) parent.findViewById(R.id.item_college_dean);

        ImageButton next = (ImageButton) parent.findViewById(R.id.item_college_next_btn);
        return new CollegeRecyclerItemViewHolder(parent,code,name,dean,next);
    }

    @Override
    public void animateAddImpl(ViewPropertyAnimatorListener listener) {
        
    }

    @Override
    public void animateRemoveImpl(ViewPropertyAnimatorListener listener) {

    }

    public void setCode(String code) {
        this.tv_code.setText(code);
    }

    public void setName(String name) {
        this.tv_name.setText(name);
    }

    public void setDean(String dean) {

        this.tv_dean.setText(dean == null || dean.equals("null")? "" : dean);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context, CourseActivity.class);
        i.putExtra("Code",tv_code.getText().toString());
        context.startActivity(i);
    }


}
