package com.coralie.projectmovie.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.coralie.projectmovie.R;
import com.coralie.projectmovie.adapters.MovieAdapter;
import com.coralie.projectmovie.api.GlideApp;
import com.coralie.projectmovie.api.MovieService;
import com.coralie.projectmovie.data.ToWatchDb;
import com.coralie.projectmovie.models.Movie;
import com.coralie.projectmovie.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private static final String API_KEY = "2a8952e8371fa67a96f6093ccdbe138a";

    private static final String ORIGINAL_TITLE ="ORIGINAL_TITLE";

    private TextView nameOfMovie;
    private TextView plotSynopsis;
    private TextView userRating;
    private TextView releaseDate;
    private ImageView imageView;
    private MovieService service;

    private RecyclerView recyclerView;

    private MovieAdapter adapter;
    private List<Movie> movieList;

    private String thumbnail;
    private String movieName;
    private String synopsis;
    private String rating;
    private String dateOfRelease;
    private int movie_id;

    private ToWatchDb toWatchDb;
    private Movie toWatch;



    private final AppCompatActivity activity = DetailActivity.this;


    public static void start(Context context , String originalTitle) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ORIGINAL_TITLE, originalTitle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        System.out.println("onCreate DetailActivity");

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieService.class);




        imageView = findViewById(R.id.thumbnail_image_header);
        nameOfMovie = findViewById(R.id.title);
        plotSynopsis = findViewById(R.id.plotsynopsis);
        userRating = findViewById(R.id.userrating);
        releaseDate = findViewById(R.id.releasedate);

        Intent intent = getIntent();
        if (intent.hasExtra("original_title")) {

            movieName = getIntent().getExtras().getString("original_title");
            thumbnail = getIntent().getExtras().getString("poster_path");
            synopsis = getIntent().getExtras().getString("overview");
            rating = getIntent().getExtras().getString("vote_average");
            dateOfRelease = getIntent().getExtras().getString("release_date");
            movie_id = getIntent().getExtras().getInt("movie_id");


            plotSynopsis.setText(synopsis);
            nameOfMovie.setText(movieName);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);

            GlideApp.with(this)
                    .load(thumbnail)
                    .into(imageView);


        } else {
            System.out.println("No API data");

        }


         Button button = findViewById(R.id.to_watch_button);


        button.setOnClickListener(
                new View.OnClickListener() {
                    //@Override
                    public void onClick(View v) {

                            saveToWatch();
                    }
                }
        );

         Button watched = findViewById(R.id.watched_button);

        watched.setOnClickListener(
                new View.OnClickListener() {
                    //@Override
                    public void onClick(View v ){

                        int movie_id = getIntent().getExtras().getInt("id");
                        toWatchDb = new ToWatchDb(DetailActivity.this);

                        List<Movie> movies = toWatchDb.getAllToWatch();

                        for ( Movie i : movies)
                        {
                            if (i.getId() == movie_id){
                                toWatchDb.deleteToWatch(movie_id);

                            }
                        }



                    }
                }
        );


    }

        public void saveToWatch(){
            toWatchDb = new ToWatchDb(activity);
            toWatch = new Movie();

            double voteAverage = Double.parseDouble(rating);
            toWatch.setVoteAverage(voteAverage);

            toWatch.setId(movie_id);
            System.out.println(movie_id);
            toWatch.setOriginalTitle(movieName);
            toWatch.setPosterPath(thumbnail);
            toWatch.setOverview(synopsis);
            toWatchDb.addToWatch(toWatch);
        }














}