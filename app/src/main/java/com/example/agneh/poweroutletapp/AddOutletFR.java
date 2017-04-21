package com.example.agneh.poweroutletapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddOutletFR extends Fragment {
    TextView txtDescription;
    TextView txtTitle;
    View thisView;
    Button btnCamera,btnUpload;
    ImageView imgCamera;
    double lat = 30;
    double lon = 30;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public AddOutletFR() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_add_outlet, container, false);
        btnCamera = (Button) thisView.findViewById(R.id.btnCamera);
        btnUpload = (Button) thisView.findViewById(R.id.btnUpload);
        imgCamera = (ImageView) thisView.findViewById(R.id.imgCamera);
        txtDescription = (TextView) thisView.findViewById(R.id.txtDescription);
        txtTitle = (TextView) thisView.findViewById(R.id.txtTitle);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("btnUpload","clicked");
                String title = txtTitle.getText().toString();
                if(!title.trim().equals("")) {
                    Log.d("title=",title);
                    insertOutlet(lat, lon, txtTitle.getText().toString(), txtDescription.getText().toString(), encodeImage());
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "Please insert a title", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return thisView;
    }



    public void insertOutlet(double lat, double lon,String title, String description,String picture){
        Log.d("insertoutlet","started");
        new InsertOutlet().execute(Double.toString(lat),Double.toString(lon),title,description,picture);
        Log.d("insertoutlet","ended");
    }


    public void uiInsertOutlet(String outletid){
        Log.d("uiinsertoutlet","started");
        if(outletid!=null)
            Log.d("out",outletid);
        else Log.d("out","something went wrong");
        //TODO:Write code to set UI elements

    }



    private class InsertOutlet extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String...params) {
            Log.d("async","started");
            String lat = params[0];
            String lon = params[1];
            String title = params[2];
            String description = params[3];
            String picture = params[4];
            String url = "http://lekrot.no/poapi/insertoutlet.php";
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            HashMap<String,String> map = new HashMap<>();
            map.put("lat",lat);
            map.put("lon",lon);
            map.put("title",title);
            if(description!=null && !description.equals("")) map.put("description",description);
            if(picture!=null && !picture.equals("")) map.put("picture",picture);
            String result = webreq.makeWebServiceCall(url, WebRequest.POSTRequest,map);
            Log.d("Response: ", "> " + result);
            return Functions.tryParseInt(result) ? result : null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            uiInsertOutlet(result);
            Log.d("async","ended");
        }
    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = rotateImageToPortrait(imageBitmap, 90);
            imageBitmap = flipImageHorizontally(imageBitmap);
            imgCamera.setImageBitmap(imageBitmap);
        }
    }

    public String encodeImage(){
        try {
            imgCamera.buildDrawingCache();
            Bitmap bm = imgCamera.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }catch(Exception e){
            return null;
        }

    }

    public Bitmap rotateImageToPortrait(Bitmap bitmap, float degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap flipImageHorizontally(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth()/2, bitmap.getHeight()/2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
