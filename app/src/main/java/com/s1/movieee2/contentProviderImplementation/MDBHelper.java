package com.s1.movieee2.contentProviderImplementation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by s1mar_000 on 23-02-2016.
 */
public class MDBHelper extends SQLiteOpenHelper {

   static final int DB_VERSION = 3;
    public MDBHelper(Context context) {
        super(context, MContract.DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table
        db.execSQL(CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(UPGRADE_MOVIE_TABLE);
        onCreate(db);

    }

    /**
     * TABLE strings
     */
    //Data Types

    final String TEXT_TYPE= " TEXT";
    final String INTEGER_TYPE=" INTEGER";
    final String COMMA = " , ";

    //TABLE generation strings

    final String CREATE_MOVIE_TABLE= "CREATE TABLE "+MContract.MTable.TABLE_NAME+ " ( "
            + MContract.MTable.MOVIE_TABLE_COLUMN_ID+INTEGER_TYPE+" PRIMARY KEY"+" AUTOINCREMENT"+COMMA
            + MContract.MTable.MOVIE_TABLE_COLUMN_RATING+INTEGER_TYPE+COMMA
            + MContract.MTable.MOVIE_TABLE_COLUMN_TITLE+TEXT_TYPE+COMMA
            + MContract.MTable.MOVIE_TABLE_COLUMN_SYN+TEXT_TYPE+COMMA
            + MContract.MTable.MOVIE_TABLE_COLUMN_IMAGE_PATH+TEXT_TYPE+COMMA
            + MContract.MTable.MOVIE_TABLE_COLUMN_TRAILER_LINK+TEXT_TYPE+" );";

    final String UPGRADE_MOVIE_TABLE = "DROP TABLE IF EXISTS "+ MContract.MTable.TABLE_NAME;
}