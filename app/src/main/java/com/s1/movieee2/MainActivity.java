package com.s1.movieee2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.s1.movieee2.support.Movie;

public class MainActivity extends Activity implements ListViewFragment.Communicate,frag_fav.comm{

    View spinnerM;

    @Override
    public void startB(Movie movie) {


        Detail_Fragment fragment = new Detail_Fragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container_detail_tablet, fragment);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragment.setIsTablet(movie, true);
        transaction.commit();

    }

    @Override
    public void startFav(boolean isPhone) {

        frag_fav fragment = new frag_fav();
        fragment.device(isPhone);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.LGContainer, fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    public void startFav() {

        frag_fav fragment = new frag_fav();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.LGContainer,fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

    }

    void startNorm(String choice){

        ListViewFragment fragment = new ListViewFragment();
        fragment.choiceSelect(choice);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.LGContainer,fragment);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        ActionBar actionBar = this.getActionBar();
        spinnerM = getLayoutInflater().inflate(R.layout.spinner_menu,null);
        actionBar.setCustomView(spinnerM);
        actionBar.setDisplayShowCustomEnabled(true);


        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment pt;
            pt = new ListViewFragment();
            ft.replace(R.id.LGContainer,pt);
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();

        Spinner spinner = (Spinner) spinnerM.findViewById(R.id.spin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String choice = "";

                switch (position) {

                    case 1:
                        choice = "popular";
                        break;
                    case 2:
                        choice = "ratedR";
                        break;
                    case 3:
                        choice = "kidsPopular";
                        break;
                    case 4:
                        choice = "releaseOfTheYear";
                        break;
                    case 5:
                        choice = "Fav";

                        startFav();
                        return;

                    default:
                        choice = "popular";
                        break;

                }
                startNorm(choice);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }


}
