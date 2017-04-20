package com.example.agneh.poweroutletapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Torkel on 3/30/2017.
 */

public class Outlet {
    String outletid,title,description,picturename;
    int pictureid;
    Double lat,lon;
    int upvotes;
    int downvotes;
    ArrayList<Comment> comments = new ArrayList<>();

    public Outlet(String outletid,String title,double lat, double lon,String description,String picturename){
        this.picturename = picturename;
        this.outletid = outletid;
        this.title=title;
        this.lat=lat;
        this.lon=lon;
        this.description=description;
    }

    public void calculateUpvotes(){
        this.upvotes=0;
        this.downvotes=0;
        for(Comment comment : comments){
            if(comment.getUpvote()==1)upvotes++;
            else downvotes++;
        }
    }

    public ArrayList<Comment> getComments(){
        return comments;
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

    public String getPicturename() {
        return picturename;
    }

    public int getPictureid() { return pictureid; }

    public int getUpvotes() { return upvotes; }

    public int getDownvotes() { return downvotes; }

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
                ", picture='" + picturename + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", upvotes=" + upvotes+
                ", downvotes=" + downvotes+
                '}';
    }
}