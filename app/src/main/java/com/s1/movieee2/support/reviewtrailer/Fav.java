package com.s1.movieee2.support.reviewtrailer;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.s1.movieee2.contentProviderImplementation.MContract;

/**
 * Created by s1mar_000 on 05-03-2016.
 */
public class Fav extends AsyncTask<Void,Void,Cursor> {


    Context context;
    SimpleCursorAdapter adapter;


    public Fav(Context context,SimpleCursorAdapter adapter){
        this.context = context;
        this.adapter = adapter;

    }

    @Override
    protected Cursor doInBackground(Void... params) {
        String uriS = "content://" + MContract.AUTHORITY + "/" + MContract.PATH;
        Uri uri = Uri.parse(uriS);
        try {
           return context.getContentResolver().query(uri,new String[]{},null,null,null);
        }
        catch (SQLException ex){
            Log.e("Fav",ex.getMessage(),ex);
        }
        return null;

    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        if(cursor!=null){

            adapter.changeCursor(cursor);
            adapter.notifyDataSetChanged();

        }

    }
}
