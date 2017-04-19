package com.example.agneh.poweroutletapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Torkel on 05-Apr-17.
 */

public class Functions {

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Outlet parseOutlet(JSONObject jOutlet) {
        try {
            String outletid = jOutlet.getString("outletid");
            double lat = Double.parseDouble(jOutlet.getString("lat"));
            double lon = Double.parseDouble(jOutlet.getString("lon"));
            String title = jOutlet.getString("title");
            String description = jOutlet.getString("description");
            String picturename = jOutlet.getString("picture");
            Log.d("picture",picturename);
            Outlet outlet = new Outlet(outletid,title,lat,lon,description,picturename);
            // Getting comments
            JSONArray jComments = jOutlet.getJSONArray("comments");

            // looping through All comments
            for (int i = 0; i < jComments.length(); i++) {
                JSONObject c = jComments.getJSONObject(i);
                String commentid = c.getString("commentid");
                String coutletid = c.getString("outletid");
                String date = c.getString("date");
                String comment = c.getString("comment");
                int upvote = Integer.parseInt(c.getString("upvote"));
                Comment tmpcomment = new Comment(commentid,coutletid,date,comment,upvote);
                outlet.addComment(tmpcomment);
            }
            outlet.calculateUpvotes();
            return outlet;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
