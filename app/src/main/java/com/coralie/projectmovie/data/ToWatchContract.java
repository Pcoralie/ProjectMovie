package com.coralie.projectmovie.data;

import android.provider.BaseColumns;

public class ToWatchContract {
    public static final class ToWatchEntry implements BaseColumns {

        public static final String TABLE_NAME = "toWatch";
        public static final String COLUMN_MOVIEID = "movieid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_USERRATING = "userrating";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
    }
}
