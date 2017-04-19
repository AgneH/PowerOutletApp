package com.example.agneh.poweroutletapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;


/**
 * Created by agneh on 2017-03-28.
 */

public class DisplayOutletFR extends BottomSheetDialogFragment {

    private View thisView;
    public DisplayOutletFR() {
        //required empty public constructor
    }

    public static DisplayOutletFR newInstance(String title) {
        DisplayOutletFR frag = new DisplayOutletFR();
        Bundle args = new Bundle();
        // this is a way to send data to Dialog window
        args.putString("outlet", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        //noinspection RestrictedApi
        super.setupDialog(dialog, style);
        thisView = View.inflate(getContext(), R.layout.fragment_display_outlet, null);
        dialog.setContentView(thisView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) thisView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        String outlet = getArguments().getString("outlet");
        getOutlet(outlet);

        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) behavior).setPeekHeight(150);
        }

    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    public void getOutlet(String s){
        new GetOutlet().execute(s);
    }


    public void uiGetOutlet(Outlet outlet){
        Log.d("out",outlet.toString());
        for(Comment comment : outlet.comments){
            Log.d("out",comment.toString());
        }
        TextView txtTitle = (TextView) thisView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) thisView.findViewById(R.id.txtDescription);
        TextView txtUpvotes = (TextView) thisView.findViewById(R.id.txtUpvotes);
        TextView txtDownvotes = (TextView) thisView.findViewById(R.id.txtDownvotes);
        ListView lstComments = (ListView) thisView.findViewById(R.id.lstComments);
        CommentAdapter adapter = new CommentAdapter(outlet.getComments(),getContext());
        lstComments.setAdapter(adapter);
        txtTitle.setText(outlet.getTitle());
        txtUpvotes.setText(Integer.toString(outlet.getUpvotes()));
        txtDownvotes.setText(Integer.toString(outlet.getDownvotes()));
        txtDescription.setText(outlet.getDescription());

        String picture = "http://lekrot.no/poapi/upload/"+outlet.getPicturename();
        new DownloadImageTask((ImageView) thisView.findViewById(R.id.imaOutlet))
                .execute(picture);

    }

    private class GetOutlet extends AsyncTask<String,String,Outlet> {
        @Override
        protected Outlet doInBackground(String...params) {
            String outletid = params[0];
            String url = "http://lekrot.no/poapi/getoutlet.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("outletid",outletid);
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + jsonStr);
            try{
                JSONObject jOutlet = new JSONObject(jsonStr);
                Outlet outlet = Functions.parseOutlet(jOutlet);
                return outlet;
            }catch (Exception ex){
                return null;
            }
        }
        @Override
        protected void onPostExecute(Outlet outlet) {
            super.onPostExecute(outlet);
            uiGetOutlet(outlet);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imgCamera;

        public DownloadImageTask(ImageView bmImage) {
            this.imgCamera = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imgCamera.setImageBitmap(result);
        }
    }

}
