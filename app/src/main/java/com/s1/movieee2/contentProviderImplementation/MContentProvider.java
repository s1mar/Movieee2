package com.s1.movieee2.contentProviderImplementation;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by s1mar_000 on 23-02-2016.
 */
public class MContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private MDBHelper DBhelper;
    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    @Override
    public boolean onCreate() {
        //only initialising the DBhelper and deferring the database creation for later.
        DBhelper = new MDBHelper(getContext());
        matcher.addURI(MContract.AUTHORITY,MContract.PATH,1);
        matcher.addURI(MContract.AUTHORITY,MContract.PATH+"/#",2);
        matcher.addURI(MContract.AUTHORITY,MContract.PATH+"/*",3);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCur;
        db = DBhelper.getWritableDatabase();
        try {
            switch (matcher.match(uri)) {

                case 1:
                    retCur = db.query(MContract.MTable.TABLE_NAME, projection, null, null, null, null, null);
                    break;
                case 2:
                    retCur = db.query(MContract.MTable.TABLE_NAME, projection, "_id LIKE ?", new String[]{uri.getLastPathSegment().toString()}, null, null, null);
                    break;
                case 3:
                    retCur = db.query(MContract.MTable.TABLE_NAME, projection,MContract.MTable.MOVIE_TABLE_COLUMN_TITLE+" LIKE ?", new String[]{uri.getLastPathSegment().toString()}, null, null, null);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid URI");

            }
        }
        catch (SQLException ex){
            return null;

        }

        return retCur;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (matcher.match(uri)){
            case 1:
                return  "vnd.android.cursor.dir/vnd.com.s1.moviex.provider.movie_table";
            case 2:
                return  "vnd.android.cursor.item/vnd.com.s1.moviex.provider.movie_table";
            default:
                throw new IllegalArgumentException("Unsupported URI");
        }

    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        db = DBhelper.getWritableDatabase();
        long _id=db.insert(MContract.MTable.TABLE_NAME,null,values);
        if(_id > 0){

            return ContentUris.withAppendedId(uri,_id);

        }
        throw new SQLException("Failed to add record "+ uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = DBhelper.getWritableDatabase();
        int rows =0 ;

        switch (matcher.match(uri)){

            case 1:
             rows = db.delete(MContract.MTable.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri");

        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = DBhelper.getWritableDatabase();
        int rows =0 ;

        switch (matcher.match(uri)){

            case 1:
                rows = db.update(MContract.MTable.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid Uri");

        }

        return rows;
    }
}
