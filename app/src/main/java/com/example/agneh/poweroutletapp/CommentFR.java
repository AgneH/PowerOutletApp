package com.example.agneh.poweroutletapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by agneh on 2017-04-05.
 */

public class CommentFR extends Fragment{
    public CommentFR() {
        //empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    //use this to insert a comment to the db
    //refer to uiInsertComment if you need the result
    public void insertComment(String outletid,String comment,int upvote){
        new InsertComment().execute(outletid,comment,Integer.toString(upvote));
    }

    //runs after insertComment, use this if you need to do anything with the result
    public void uiInsertComment(String commentid){
        if(commentid!=null){
            Log.d("out",commentid);
        }else{
            Log.d("out","something went wrong");
        }
    }

    private class InsertComment extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String...params) {
            String outletid = params[0];
            String comment = params[1];
            String upvote = params[2];
            String url = "http://lekrot.no/poapi/insertcomment.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("outletid",outletid);
            map.put("comment",comment);
            map.put("upvote",upvote);
            String result = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + result);
            return Functions.tryParseInt(result) ? result : null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            uiInsertComment(result);
        }
    }
}
