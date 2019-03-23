package com.coralie.projectmovie.api;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.GET;


import com.coralie.projectmovie.models.MovieResponse;

public interface MovieService {
        String API_KEY = "2a8952e8371fa67a96f6093ccdbe138a";



        //MOVIE SEARCH
        @GET("search/movie")
        Call<MovieResponse> getSearch(@Query("api_key") String apiKey, @Query("query") String query);

        @GET("movie/popular")
        Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

        @GET("movie/top_rated")
        Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);



}
/*
curl --request GET \
  --url 'https://api.themoviedb.org/4/list/1' \
  --header 'Authorization: Bearer {access_token}' \
  --header 'Content-Type: application/json;charset=utf-8'
 */

/*
curl --request GET \
  --url 'https://api.themoviedb.org/4/list/1?api_key={api_key}' \
  --header 'Content-Type: application/json;charset=utf-8'
 */