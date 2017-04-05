package com.example.agneh.poweroutletapp;

import java.util.ArrayList;

/**
 * Created by Torkel on 3/30/2017.
 */

public class Outlet {
    String outletid,title,description,picture;
    Double lat,lon;
    int upvotes;
    ArrayList<Comment> comments = new ArrayList<>();

    public Outlet(String outletid,String title,double lat, double lon,String description){
        this.outletid = outletid;
        this.title=title;
        this.lat=lat;
        this.lon=lon;
        this.description=description;
    }

    public void calculateUpvotes(){
        upvotes=0;
        for(Comment comment : comments){
            if(comment.getUpvote()==1)upvotes++;
            else upvotes--;
        }
    }

    public void addComment(Comment comment){
        comments.add(comment);
    }

    public String getOutletid() {
        return outletid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "Outlet{" +
                "outletid='" + outletid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", upvotes=" + upvotes+
                '}';
    }
}