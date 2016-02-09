package com.incc.softwareproject.socialngatutor.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;

/**
 * Created by carlo on 21/12/2015.
 */
public class fragTab2 extends Fragment {

    public static fragTab2 createInstance() {
        fragTab2 frag_tab2 = new fragTab2();
        Bundle bundle = new Bundle();
        frag_tab2.setArguments(bundle);
        return frag_tab2;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_2, container, false);
    }





}
