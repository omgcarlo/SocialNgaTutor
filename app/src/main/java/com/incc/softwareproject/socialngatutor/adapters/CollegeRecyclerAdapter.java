package com.incc.softwareproject.socialngatutor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.adapters.viewholder.CollegeRecyclerItemViewHolder;

import java.util.List;

/**
 * Created by carlo on 22/02/2016.
 */
public class CollegeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    List<String> code;
    List<String> name;
    List<String> dean;
    public CollegeRecyclerAdapter(List<String> code,List<String> name,List<String> dean){
        this.code = code;
        this.name = name;
        this.dean = dean;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.college_item, parent, false);
        return CollegeRecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        CollegeRecyclerItemViewHolder holder = (CollegeRecyclerItemViewHolder) vholder;

        String s_code = code.get(position);
        String s_name = name.get(position);
        String s_dean = dean.get(position);

        holder.setCode(s_code);
        holder.setName(s_name);
        holder.setDean(s_dean);
    }

    @Override
    public int getItemCount() {
        return code == null ? 0 : code.size();
    }
}
