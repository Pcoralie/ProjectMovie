package com.coralie.projectmovie.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int id_video;
    @SerializedName("results")
    private List<Video> results;

    public int getIdVideo(){
        return id_video;
    }

    public void seIdVideo(int id_video){
        this.id_video = id_video;
    }

    public List<Video> getResults(){
        return results;
    }
}
