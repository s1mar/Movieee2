package com.s1.movieee2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.s1.movieee2.support.Movie;
import com.s1.movieee2.support.reviewtrailer.Fav;

/**
 * Created by s1mar_000 on 11-03-2016.
 */
public class frag_fav extends Fragment {


    interface comm {

        void startB(Movie movie);

    }

    comm mCallBack;

    boolean isPhone;
    SimpleCursorAdapter adapter;
    View GL;


    void device(boolean val) {

        isPhone = val;

    }


    void frag_fav() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.test2, null, new String[]{"image_path"}, new int[]{R.id.iView2}, 0);
        new Fav(getActivity(), adapter).execute();

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            isPhone = false;
        } else {
            isPhone = true;
        }

        if (isPhone) {

            return inflater.inflate(R.layout.main_frag_phone, container, false);
        } else {

            return inflater.inflate(R.layout.main_frag_tablet, container, false);

        }


    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (comm) activity;
        } catch (ClassCastException ex) {
            Log.e("Class cast", activity.toString() + "isnt hooked up to the interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null; // avoid leaking
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isPhone) {
            View viewP = getView();
            GridView listUI = (GridView) viewP.findViewById(R.id.list_view_frag_phone);
            GL = listUI;

            listUI.setAdapter(adapter);


        } else {
            View viewTAB = getView();
            ListView listUI = (ListView) viewTAB.findViewById(R.id.list_view_frag_tablet);
            GL = listUI;

            listUI.setAdapter(adapter);

        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (isPhone) {
            GridView c = (GridView) GL;
            c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor c = adapter.getCursor();
                    c.moveToPosition(position);
                    Intent intent = new Intent(getActivity(), pDetail_Activity.class);
                    intent.putExtra("title", c.getString(2));
                    intent.putExtra("vote", c.getString(1));
                    intent.putExtra("path", c.getString(4));

                    intent.putExtra("desc", c.getString(3));
                    intent.putExtra("id", c.getString(0));
                    c.close();
                    startActivity(intent);
                }


            });

        } else {
            ListView lv = (ListView) GL;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Cursor c = adapter.getCursor();
                    c.moveToPosition(position);
                    Movie movie = new Movie();
                    movie.setId(c.getString(0));
                    movie.setOverview(c.getString(3));
                    movie.setVote(c.getString(1));
                    movie.setImgPath("file://"+ c.getString(4));
                    movie.setName(c.getString(2));
                    mCallBack.startB(movie);
                }
            });

        }
    }
}
