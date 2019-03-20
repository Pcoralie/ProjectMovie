package com.coralie.projectmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coralie.projectmovie.activities.DetailActivity;
import com.coralie.projectmovie.R;
import com.coralie.projectmovie.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{


    public Context myContext;
    private final List<Movie> movieList;

    public MovieAdapter(Context myContext , List<Movie> movieList) {
        this.myContext = myContext;
        this.movieList = movieList;

    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.title.setText(movieList.get(position).getOriginalTitle());
        String vote = Double.toString(movieList.get(position).getVoteAverage());
        viewHolder.userrating.setText(vote);



        Glide.with(myContext)
                .load(movieList.get(position).getPosterPath())
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList != null ? movieList.size() : 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView userrating;
        private ImageView thumbnail;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.title = view.findViewById(R.id.title);
            this.userrating = view.findViewById(R.id.userrating);
            this.thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(myContext, DetailActivity.class);
                        intent.putExtra("original_title", movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path", movieList.get(pos).getPosterPath());
                        intent.putExtra("overview", movieList.get(pos).getOverview());
                        intent.putExtra("vote_average", Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date", movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You Clicked" + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();


                    }
                }

            });

        }

    }

}
