package com.coralie.projectmovie.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coralie.projectmovie.R;
import com.coralie.projectmovie.models.Video;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context myContext;
    private List<Video> videoList;

    public VideoAdapter(Context myContext, List<Video> videoList){
        this.myContext = myContext;
        this.videoList = videoList;

    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.video_view, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final VideoAdapter.ViewHolder viewHolder, int i){
        viewHolder.title.setText(videoList.get(i).getName());

    }

    @Override
    public int getItemCount(){

        return videoList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView thumbnail;

        public ViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Video clickedDataItem = videoList.get(pos);
                        String videoId = videoList.get(pos).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoId);
                        myContext.startActivity(intent);

                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
