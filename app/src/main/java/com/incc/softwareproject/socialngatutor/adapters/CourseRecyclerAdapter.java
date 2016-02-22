package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CollegeRecyclerItemViewHolder;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CourseRecyclerItemViewHolder;

import java.util.List;

/**
 * Created by carlo on 22/02/2016.
 */
public class CourseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    List<String> courseId;
    List<String> courseNo;
    List<String> name;
    List<String> code;
    public CourseRecyclerAdapter(List<String> courseId,List<String> code,List<String> name,List<String> courseNo){
        this.courseId = courseId;
        this.code = code;
        this.name = name;
        this.courseNo = courseNo;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        return CourseRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        CourseRecyclerItemViewHolder holder = (CourseRecyclerItemViewHolder) vholder;

        String s_code = code.get(position);
        String s_name = name.get(position);
        String s_courseNo = courseNo.get(position);
        String s_courseId = courseId.get(position);

        holder.setCode(s_code);
        holder.setName(s_name);
        holder.setCourseNo(s_courseNo);
        holder.setCourseId(s_courseId);
    }

    @Override
    public int getItemCount() {
        return code == null ? 0 : code.size();
    }
}
