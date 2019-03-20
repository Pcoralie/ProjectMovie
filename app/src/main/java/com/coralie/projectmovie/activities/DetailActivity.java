package com.coralie.projectmovie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coralie.projectmovie.R;
import com.coralie.projectmovie.adapters.VideoAdapter;
import com.coralie.projectmovie.api.MovieService;
import com.coralie.projectmovie.models.Movie;
import com.coralie.projectmovie.models.Video;
import com.coralie.projectmovie.models.VideoResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private static final String API_KEY = "2a8952e8371fa67a96f6093ccdbe138a";

    private MovieService service;


    TextView nameOfMovie;
    TextView plotSynopsis;
    TextView userRating;
    TextView releaseDate;
    ImageView imageView;

    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<Video> videoList;

    Movie movie;
    String thumbnail;
    String movieName;
    String synopsis;
    String rating;
    String dateOfRelease;
    int movie_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView =  findViewById(R.id.thumbnail_image_header);
        nameOfMovie =  findViewById(R.id.title);
        plotSynopsis =  findViewById(R.id.plotsynopsis);
        userRating =  findViewById(R.id.userrating);
        releaseDate =  findViewById(R.id.releasedate);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {

            thumbnail = movie.getPosterPath();
            movieName = movie.getOriginalTitle();
            synopsis = movie.getOverview();
            rating = Double.toString(movie.getVoteAverage());
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();


            Glide.with(this)
                    .load(thumbnail)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        }

        initializeView();
    }


        private void initCollapsingToolbar(){
            final CollapsingToolbarLayout collapsingToolbarLayout =
                    (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle("");
            AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
            appBarLayout.setExpanded(true);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                boolean isShow = false;
                int scrollRange = -1;

                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.getTotalScrollRange();
                    }
                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                        isShow = true;
                    } else if (isShow) {
                        collapsingToolbarLayout.setTitle(" ");
                        isShow = false;
                    }
                }


            });

    }

    private void initializeView(){
        videoList = new ArrayList<>();
        adapter = new VideoAdapter(this, videoList);

        recyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(myLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        launchMovieVideo();
    }

    private void launchMovieVideo(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieService.class);

            Call<VideoResponse> call = service.getMovieVideo(movie_id, API_KEY);
            call.enqueue(new Callback<VideoResponse>() {
                @Override
                public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                    List<Video> video = response.body().getResults();
                    recyclerView.setAdapter(new VideoAdapter(getApplicationContext(), video));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<VideoResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());

                }
            });


    }

}