package com.coralie.projectmovie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.coralie.projectmovie.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class ToWatchDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "toWatch.db";

    private static final int DATABASE_VERSION = 1;

    public static final String LOGTAG = "TOWATCH";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;

    public ToWatchDb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TOWATCH = "CREATE TABLE " + ToWatchContract.ToWatchEntry.TABLE_NAME + " (" +
                ToWatchContract.ToWatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ToWatchContract.ToWatchEntry.COLUMN_MOVIEID + " INTEGER, " +
                ToWatchContract.ToWatchEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ToWatchContract.ToWatchEntry.COLUMN_USERRATING + " REAL NOT NULL, " +
                ToWatchContract.ToWatchEntry.COLUMN_POSTER_PATH + " TEXT , " +
                ToWatchContract.ToWatchEntry.COLUMN_PLOT_SYNOPSIS + " TEXT " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TOWATCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ToWatchContract.ToWatchEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public void addToWatch(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToWatchContract.ToWatchEntry.COLUMN_MOVIEID, movie.getId());
        values.put(ToWatchContract.ToWatchEntry.COLUMN_TITLE, movie.getOriginalTitle());
        values.put(ToWatchContract.ToWatchEntry.COLUMN_USERRATING, movie.getVoteAverage());
        values.put(ToWatchContract.ToWatchEntry.COLUMN_POSTER_PATH, movie.getPoster());
        values.put(ToWatchContract.ToWatchEntry.COLUMN_PLOT_SYNOPSIS, movie.getOverview());

        db.insert(ToWatchContract.ToWatchEntry.TABLE_NAME, null, values);
        db.close();
    }

    //public void deleteToWatch(String title /*int id*/ ){
        public void deleteToWatch(int id){

            SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ToWatchContract.ToWatchEntry.TABLE_NAME, ToWatchContract.ToWatchEntry.COLUMN_MOVIEID+ "=" + id, null);
        //db.delete(ToWatchContract.ToWatchEntry.TABLE_NAME, ToWatchContract.ToWatchEntry.COLUMN_TITLE + "="+ title , null);
    }

    public List<Movie> getAllToWatch(){
        String[] columns = {
                ToWatchContract.ToWatchEntry._ID,
                ToWatchContract.ToWatchEntry.COLUMN_MOVIEID,
                ToWatchContract.ToWatchEntry.COLUMN_TITLE,
                ToWatchContract.ToWatchEntry.COLUMN_USERRATING,
                ToWatchContract.ToWatchEntry.COLUMN_POSTER_PATH,
                ToWatchContract.ToWatchEntry.COLUMN_PLOT_SYNOPSIS

        };
        String sortOrder =
                ToWatchContract.ToWatchEntry._ID + " ASC";
        List<Movie> toWatchList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ToWatchContract.ToWatchEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ToWatchContract.ToWatchEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ToWatchContract.ToWatchEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(ToWatchContract.ToWatchEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(ToWatchContract.ToWatchEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(ToWatchContract.ToWatchEntry.COLUMN_PLOT_SYNOPSIS)));

                toWatchList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return toWatchList;
    }

}
