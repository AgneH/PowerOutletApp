package com.example.agneh.poweroutletapp;

/**
 * Created by Torkel on 3/30/2017.
 */

public class Comment {
    private String commentid, outletid, date, comment;
    private int upvote;

    public Comment(String commentid, String outletid, String date, String comment, int upvote) {
        this.commentid = commentid;
        this.outletid = outletid;
        this.date = date;
        this.comment = comment;
        this.upvote = upvote;
    }

    public String getCommentid() {
        return commentid;
    }

    public String getOutletid() {
        return outletid;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public int getUpvote() {
        return upvote;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentid='" + commentid + '\'' +
                ", outletid='" + outletid + '\'' +
                ", date='" + date + '\'' +
                ", comment='" + comment + '\'' +
                ", upvote=" + upvote +
                '}';
    }

}
