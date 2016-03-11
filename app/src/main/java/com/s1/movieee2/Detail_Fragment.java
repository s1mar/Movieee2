package com.s1.movieee2;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.s1.movieee2.contentProviderImplementation.MContract;
import com.s1.movieee2.contentProviderImplementation.MDBHelper;
import com.s1.movieee2.support.Movie;
import com.s1.movieee2.support.reviewtrailer.review;
import com.s1.movieee2.support.reviewtrailer.reviewAdapter;
import com.s1.movieee2.support.reviewtrailer.reviewAsync;
import com.s1.movieee2.support.reviewtrailer.trailerAdapter;
import com.s1.movieee2.support.reviewtrailer.trailerAsync;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by s1mar_000 on 03-03-2016.
 */
public class Detail_Fragment extends Fragment {

   @Bind(R.id.detail_image_id)
    ImageView imageHolder;
   @Bind(R.id.detail_text_title)
    TextView titleText;
   @Bind(R.id.detail_text_voting)TextView voteText;
   @Bind(R.id.detail_text_desc)TextView descText;
    @Bind(R.id.detail_trailers) ListView trailerLView;
    String imagePath;
    String imgLoc;

    reviewAdapter reviewAd;
    trailerAdapter trailerAd;
    String id;
    Movie movie;
    boolean isTablet;


     public void setIsTablet(Movie movie,Boolean isTablet){

        this.isTablet=isTablet;
        this.movie=movie;


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Configuration configuration = getResources().getConfiguration();
        if(configuration.smallestScreenWidthDp>=600)
        {
            isTablet=true;

        }
        else {
            isTablet=false;
        }

        reviewAd = new reviewAdapter(inflater.getContext(), R.layout.review, R.id.review_author, R.id.review_content, new review[0]);
        trailerAd = new trailerAdapter(inflater.getContext(), R.layout.review, R.id.review_author);

        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, root);

        try{
        if (isTablet == true) {

            if (movie != null) {
                imagePath = movie.getImgPath();

                Picasso.with(inflater.getContext()).load(imagePath).resize(400, 600).error(R.drawable.no_image).into(imageHolder);
                titleText.setText(movie.getName());
                descText.setText(movie.getOverview());
                voteText.setText(movie.getVote());
                id = movie.getId();
                if (titleText.getText().length() > 35) {
                    titleText.setMaxWidth(600);

                } else {
                    Log.e("FRAG_DETAILS", "INTENT NULL");
                }
            }
        }else {
                    Intent intent = getActivity().getIntent();
                    if (intent != null) {
                        imagePath = intent.getStringExtra("path");
                        if(imagePath.contains("storage")) {
                            imagePath= "file://"+imagePath;
                        }

                        Picasso.with(inflater.getContext()).load(imagePath).resize(400, 600).error(R.drawable.no_image).into(imageHolder);
                        titleText.setText(intent.getStringExtra("title"));
                        descText.setText(intent.getStringExtra("desc"));
                        voteText.setText(intent.getStringExtra("vote"));
                        id = intent.getStringExtra("id");

                        if (titleText.getText().length() > 35) {
                            titleText.setMaxWidth(600);
                        }

                    }
                    else {
                Log.e("FRAG_DETAILS", "INTENT NULL");
            }

        }
    }
        catch (Exception ex){

            Log.e("FRAG_DETAILS",ex.getMessage(),ex);
        }


return root;

    }

    @Override
    public void onStart() {

        super.onStart();
        new reviewAsync(id,reviewAd).execute("");
        new trailerAsync(id,trailerAd).execute("");
        ListView listView = (ListView)getView().findViewById(R.id.detail_review);
        listView.setAdapter(reviewAd);
        trailerLView.setAdapter(trailerAd);


        trailerLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String x = "https://www.youtube.com/watch?v=" + trailerAd.getSource(position);
                intent.setData(Uri.parse(x));
                startActivity(intent);


            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);
        String uriS = "content://" + MContract.AUTHORITY + "/" + MContract.PATH;
        Uri uri = Uri.parse(uriS);
        uri.buildUpon().appendPath(titleText.getText().toString()).build();
        MDBHelper helper = new MDBHelper(getActivity());
        SQLiteDatabase db;
        try {
             db = helper.getWritableDatabase();
        }
        catch (SQLException ex){
            Log.e("errDB",ex.getMessage());
            return;
        }

        Cursor x = db.query(MContract.MTable.TABLE_NAME,new String[]{"title"},"title LIKE ?",new String[]{titleText.getText().toString().trim()},null,null,null);

        MenuItem item = menu.findItem(R.id.is_fav);

        try {

            if ( x.moveToFirst()) {


                String title =titleText.getText().toString().trim();
                String dbS =   x.getString(0).trim();
                if (title.equals(dbS)) {

                    item.setChecked(true);

                }
                else {
                    x.close();
                    db.close();

                   //close cursor and do nothing
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("CursorERR",ex.getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.is_fav)
        {
            if (item.isChecked())
            {
                item.setChecked(false);
                //uncheck it and remove the entry from the database
                try {
                    String uriX = "content://" + MContract.AUTHORITY + "/" + MContract.PATH;
                    Uri ur = Uri.parse(uriX);
                    String where= MContract.MTable.MOVIE_TABLE_COLUMN_TITLE+" = "+"?";
                    String[] args={titleText.getText().toString()};
                    getActivity().getContentResolver().delete(ur,where,args);
                }
                catch (SQLException ex)
                {
                    Log.e("ERRURIDEL",ex.getMessage(),ex);
                }


            }
            else {
                item.setChecked(true);
                //check it and add the entry to the database

                try {
                    Picasso.with(getActivity()).load(imagePath).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            try {
                                String root = Environment.getExternalStorageDirectory().toString();
                                File myDir = new File(root + "/Movieee2");

                                if (!myDir.exists()) {
                                    myDir.mkdirs();
                                }

                                String name = titleText.getText() + ".jpg";
                                myDir = new File(myDir, name);
                                FileOutputStream out = new FileOutputStream(myDir);
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Err", ex.getMessage());

                } finally {

                    try {
                        String uriY = "content://" + MContract.AUTHORITY + "/" + MContract.PATH;
                        Uri u = Uri.parse(uriY);
                        ContentValues vals = new ContentValues();
                        String root = Environment.getExternalStorageDirectory().toString();
                        String name = titleText.getText() + ".jpg";
                        String path = root+"/Movieee2/"+name;
                        vals.put(MContract.MTable.MOVIE_TABLE_COLUMN_TITLE, titleText.getText().toString());
                        vals.put(MContract.MTable.MOVIE_TABLE_COLUMN_RATING, voteText.getText().toString());
                        vals.put(MContract.MTable.MOVIE_TABLE_COLUMN_SYN, descText.getText().toString());
                        vals.put(MContract.MTable.MOVIE_TABLE_COLUMN_IMAGE_PATH, path);
                        getActivity().getContentResolver().insert(u, vals);

                }catch(SQLException ex){
                    Log.e("ERRURI", ex.getMessage(), ex);
                }
            }


            }



        }

        return super.onOptionsItemSelected(item);
    }
}




