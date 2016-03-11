package com.s1.movieee2.contentProviderImplementation;

/**
 * Created by s1mar_000 on 23-02-2016.
 */
public class MContract {

    /**
     * Content Authority and Path
     */

    public static final String AUTHORITY = "com.s1.moviex.provider";
    public static final String PATH = "m";
    public static final String API_KEY=""; //Add your API KEY

        /*  content://authority/path/id  */
    /**
     * DataBase name and other specifics
     */
    public static final String DATABASE_NAME="movie.db";

    /**
     * Table(s)
     */

    public abstract class MTable{

        //table name
        public static final String TABLE_NAME="movie_table";
        //columns names
        public static final String MOVIE_TABLE_COLUMN_ID="_id";
        public static final String MOVIE_TABLE_COLUMN_TITLE="title";
        public static final String MOVIE_TABLE_COLUMN_RATING="rating";
        public static final String MOVIE_TABLE_COLUMN_IMAGE_PATH="image_path";
        public static final String MOVIE_TABLE_COLUMN_TRAILER_LINK="trailer";
        public static final String MOVIE_TABLE_COLUMN_SYN="synopsis";
    }

}