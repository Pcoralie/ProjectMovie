package com.coralie.projectmovie.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.coralie.projectmovie.R;
import com.coralie.projectmovie.adapters.MovieAdapter;
import com.coralie.projectmovie.api.MovieService;
import com.coralie.projectmovie.data.ToWatchDb;
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


    public static final String LOG_TAG = MovieAdapter.class.getName();


    private EditText editText;
    private RecyclerView recyclerView;
    private MovieService service;
    private ConstraintLayout constraintLayout;
    private MovieAdapter adapter;
    private List<Movie> movieList;

    private AppCompatActivity activity = MainActivity.this;


    private ToWatchDb toWatchDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieService.class);

        initialize();

    }



    private void initialize() {
        System.out.println("Initialize...");

        checkMenu();

        recyclerView = findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        adapter = new MovieAdapter(this, movieList);
        toWatchDb = new ToWatchDb(activity);



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        constraintLayout = findViewById(R.id.main_content);


    }

    private void launchSearch(String query) {
        Call<MovieResponse> call = service.getSearch(API_KEY, query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse launchSearch");
                    List<Movie> movieList = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movieList));
                }
                else {
                    Log.e(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure launchSearch", t);
            }
        });
    }



    private void launchPopularMovies() {
        Call<MovieResponse> call = service.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse launchPopularMovie");

                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure launchPopularMovie", t);
            }
        });
    }


    private void launchTopRatedMovies() {
        Call<MovieResponse> call = service.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {

                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse launchTopRatedMovies");

                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure launchTopRatedMovies", t);
            }
        });
    }

    private void launchListToWatch() {
        try {
            List<Movie> movies = toWatchDb.getAllToWatch();
            recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
        } catch (Exception e){
            Log.d(TAG, "list to watch empty");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void checkMenu() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.menu_key),
                this.getString(R.string.most_popular)
        );
        if (sortOrder.equals(this.getString(R.string.most_popular))) {
            Log.d(LOG_TAG, "The most popular movies ");
            editText.setVisibility(View.INVISIBLE);


            launchPopularMovies();
        } else if (sortOrder.equals(this.getString(R.string.highest_rated))) {
            Log.d(LOG_TAG, "The movies with the highest rate");
            editText.setVisibility(View.INVISIBLE);


            launchTopRatedMovies();

        }else if (sortOrder.equals(this.getString(R.string.search))){
            //(sortOrder.equals(this.getString(R.string.search)))
            Log.d(LOG_TAG, "Searching");

            editText.setVisibility(View.VISIBLE);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() >= 0) {
                        launchSearch(s.toString());
                    } else {
                        launchSearch("");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }else {
            Log.d(LOG_TAG, "The list of movie to watch ");
            editText.setVisibility(View.INVISIBLE);


            launchListToWatch();

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (movieList.isEmpty()){
            checkMenu();
        }else{

            checkMenu();
        }
    }
}







