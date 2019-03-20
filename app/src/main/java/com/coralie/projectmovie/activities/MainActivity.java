package com.coralie.projectmovie.activities;

// URL :
    // Glide docs :
// https://bumptech.github.io/glide/doc/getting-started.html

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.widget.EditText;

import com.coralie.projectmovie.R;
import com.coralie.projectmovie.adapters.MovieAdapter;
import com.coralie.projectmovie.api.MovieService;
import com.coralie.projectmovie.models.Movie;
import com.coralie.projectmovie.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String API_KEY = "2a8952e8371fa67a96f6093ccdbe138a";

    private RecyclerView recyclerView;
    private RecyclerView gridView;
    private EditText editText;
    private MovieService service;

    private MovieAdapter adapter;
    private List<Movie> movieList;

    public static final String LOG_TAG = MovieAdapter.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieService.class);

        initialize();
        launchSearch();


    }

    private void initialize(){
        System.out.println("Fetching movies...");

        recyclerView = findViewById(R.id.recyclerView);
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);

        //if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //{

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void launchSearch() {

        Call<MovieResponse> call = service.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>(){
                @Override
                public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {

                    if (response.isSuccessful()) {
                        Log.d(TAG, "onResponse");

                        List<Movie> movies = response.body().getResults();
                        recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);

                    }

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure", t);
                }
            });
        }



    }
