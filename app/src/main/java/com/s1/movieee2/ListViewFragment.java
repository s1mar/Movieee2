package com.s1.movieee2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.s1.movieee2.support.Movie;
import com.s1.movieee2.support.MovieAdapter;
import com.s1.movieee2.support.mAsyncTask;
import com.s1.movieee2.support.movieQuery;

/**
 * Created by s1mar_000 on 02-03-2016.
 */
public class ListViewFragment extends Fragment {

    public ListViewFragment(){};
    MovieAdapter adapter;
    SimpleCursorAdapter cursorAdapter;

    boolean fFlag;
    View container;
    Boolean isPhone;
    View GL;
    String choice;


    Communicate mCallBack;

    public interface Communicate{

        void startB(Movie movie);
        void startFav(boolean isPhone);


    }


    void choiceSelect(String choice)
    {
        if (choice==null)
            this.choice = "default";
        else
            this.choice=choice;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (Communicate) activity;
        }
        catch (ClassCastException ex)
        {
            Log.e("Class cast",activity.toString()+"isnt hooked up to the interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack=null; // avoid leaking
    }

    protected void isPhone(boolean x)
    {

        isPhone=x;

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {
            isPhone=false;
        } else {
            isPhone=true;
        }





        adapter = new MovieAdapter(new String[0], inflater.getContext(), R.layout.test, R.id.iView);
        new mAsyncTask(adapter).execute(new movieQuery().movieQueryGenerator(choice));




        if (isPhone == true){

            return inflater.inflate(R.layout.main_frag_phone, container, false);
        }
        else {

            return inflater.inflate(R.layout.main_frag_tablet, container, false);

        }

    }
    @Override
    public void onStart() {

        super.onStart();


        if(isPhone == true ) {
            View viewP = getView();
            GridView listUI = (GridView) viewP.findViewById(R.id.list_view_frag_phone);
            GL = listUI;

            if (fFlag) {
                listUI.setAdapter(cursorAdapter);
            } else {
                listUI.setAdapter(adapter);
            }
        }
        else {
            View viewTAB = getView();
            ListView listUI = (ListView)viewTAB.findViewById(R.id.list_view_frag_tablet);
            GL=listUI;

            if (fFlag) {
                listUI.setAdapter(cursorAdapter);
            } else {
                listUI.setAdapter(adapter);
            }
            }

        }

    @Override
    public void onResume() {
        super.onResume();

        if(isPhone==true)
        {
            GridView gridView = (GridView)GL;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                     Movie movie = new Movie();
                    if(!fFlag){
                     movie=adapter.getMovie(position);
                    Intent intent = new Intent(getActivity(),pDetail_Activity.class);
                    intent.putExtra("title",movie.getName());
                    intent.putExtra("vote",movie.getVote());
                    intent.putExtra("path",movie.getImgPath());
                    intent.putExtra("desc",movie.getOverview());
                    intent.putExtra("id",movie.getId());
                    startActivity(intent);}

                }
            });

        }
        else
        {
            ListView lv =(ListView) GL;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Movie movie = new Movie();
                    movie = adapter.getMovie(position);
                    mCallBack.startB(movie);

                }
            });




                }
        }

    }



