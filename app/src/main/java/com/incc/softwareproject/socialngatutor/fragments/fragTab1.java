package com.incc.softwareproject.socialngatutor.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.incc.softwareproject.socialngatutor.R;
import com.incc.softwareproject.socialngatutor.Server.Post;
import com.incc.softwareproject.socialngatutor.Server.Server;
import com.incc.softwareproject.socialngatutor.adapters.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;


/**
 * Created by carlo on 21/12/2015.
 */
public class fragTab1 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    String SCHOOL_ID;
    //  TO PASS IN RECYCLER ADAPTER
    List<String> userId = new ArrayList<>();
    List<String> username = new ArrayList<>();
    List<String> fullname = new ArrayList<>();
    List<String> post = new ArrayList<>();
    List<String> postId = new ArrayList<>();
    List<String> datetime = new ArrayList<>();
    List<String> pp_url = new ArrayList<>();
    List<Boolean> owned = new ArrayList<>();
    List<Boolean> isUpvoted = new ArrayList<>();
    List<Boolean> isShared = new ArrayList<>();
    List<String> upvotes = new ArrayList<>();
    List<String> comments = new ArrayList<>();
    List<String> shares = new ArrayList<>();
    List<String> fileUrl = new ArrayList<>();
    List<String> fileName = new ArrayList<>();
    //share
    private List<String> share_postId = new ArrayList<>();
    private List<String> share_pic_url = new ArrayList<>();
    private List<String> share_fullname = new ArrayList<>();
    private List<String> share_username = new ArrayList<>();
    private List<String> share_post_description = new ArrayList<>();
    private List<String> share_file_url = new ArrayList<>();
    private List<String> share_file_name = new ArrayList<>();
    private List<String> share_userType = new ArrayList<>();

    SharedPreferences sData;
    RecyclerView recyclerView;
    SwipeRefreshLayout srl;
    public static fragTab1 newInstance() {
        fragTab1 frag_tab1 = new fragTab1();
        Bundle bundle = new Bundle();
        frag_tab1.setArguments(bundle);
        return frag_tab1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //recyclerView = (RecyclerView) inflater.inflate(R.layout.tab_1, container, false);
        View view = inflater.inflate(R.layout.tab_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.t1_recyclerView);
        clear();
        new initPost().execute();
        sData = this.getActivity().getSharedPreferences("ShareData", Context.MODE_PRIVATE);
        SCHOOL_ID = sData.getString("SchoolId", "wala");
        srl = (SwipeRefreshLayout) view.findViewById(R.id.t1_swipe_refresh_layout);
        srl.setOnRefreshListener(this);
        //srl.setColorSchemeColors(R.color.color_accent_pink,R.color.color_primary_blue,R.color.color_primary_red);
        srl.setRefreshing(true);
        //Toast.makeText(getActivity(),SCHOOL_ID, Toast.LENGTH_SHORT).show();

        return view;
    }

    public void clear(){
        username.clear();
        fullname.clear();
        postId.clear();
        userId.clear();
        post.clear();
        datetime.clear();
        pp_url.clear();
        owned.clear();
        isUpvoted.clear();
        isShared.clear();
        upvotes.clear();
        comments.clear();
        shares.clear();
        fileName.clear();
        fileUrl.clear();
        share_pic_url.clear();
        share_fullname.clear();
        share_username.clear();
        share_post_description.clear();
        share_file_url.clear();
        share_file_name.clear();
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(fullname,username,post,postId,
                                                            userId,datetime,pp_url,owned,isUpvoted,
                                                            isShared,upvotes, comments,shares,fileUrl,fileName,
                                                            share_postId, share_userType , share_pic_url,
                                                            share_fullname,  share_username, share_post_description,
                                                            share_file_url, share_file_name);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(recyclerAdapter));
    }

    protected void convertJSON(String result){
        //Log.e("tab1 res:",result);
        try {
            clear();
            JSONObject reader = new JSONObject(result);
            JSONArray data = reader.getJSONArray("Post");
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonobject = data.getJSONObject(i);
               //Log.d("des",jsonobject.getString("description"));  //TESTING PURPOSE ONLY
                username.add(jsonobject.getString("username"));
                post.add(jsonobject.getString("description"));
                fullname.add(jsonobject.getString("full_name"));
                postId.add(jsonobject.getString("postId"));
                userId.add(jsonobject.getString("schoolId"));
                datetime.add(jsonobject.getString("datetime"));
                pp_url.add(jsonobject.getString("pic_url"));
                owned.add(jsonobject.getBoolean("isOwned"));
                isUpvoted.add(jsonobject.getBoolean("isUpvoted"));
                isShared.add(jsonobject.getBoolean("isShared"));
                upvotes.add(jsonobject.getString("upvotes"));
                shares.add(jsonobject.getString("shares"));
                comments.add(jsonobject.getString("comments"));
                if("nofile".equals(jsonobject.getString("file_url"))){
                    fileUrl.add("");
                    fileName.add("");
                }
                else{
                    fileUrl.add(jsonobject.getString("file_url"));
                    fileName.add(jsonobject.getString("file_description"));
                }
                // share
                if(jsonobject.isNull("share_postId")){
                    share_postId.add("");
                    share_pic_url.add("");
                    share_fullname.add("");
                    share_username.add("");
                    share_post_description.add("");
                    share_file_url.add("");
                    share_file_name .add("");
                    share_userType.add("");
                }
                else{
                    share_postId.add(jsonobject.getString("share_postId"));
                    share_pic_url.add(jsonobject.getString("share_pic_url"));
                    share_fullname.add(jsonobject.getString("share_full_name"));
                    share_username.add(jsonobject.getString("share_username"));
                    share_userType.add(jsonobject.getString("share_userType"));
                    share_post_description.add(jsonobject.getString("share_description"));
                    if(jsonobject.isNull("share_file_url")){
                        share_file_url.add("");
                        share_file_name .add("");
                    }
                    else {
                        share_file_url.add(jsonobject.getString("share_file_url"));
                        share_file_name.add(jsonobject.getString("share_file_description"));
                    }
                }
            }
            srl.setRefreshing(false);
            setupRecyclerView(recyclerView);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        clear();
        new initPost().execute();

    }

    private class initPost extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //Log.d("id",SCHOOL_ID);
            Post svConn = new Post();
            return svConn.getFeeds(SCHOOL_ID);
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.equals("")){
               // Log.d("ress",result);
                convertJSON(result);
            }
            else{
                Log.d("TAB1","error");
            }
        }
    }
}