package com.example.agneh.poweroutletapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.R.attr.path;
import static android.app.Activity.RESULT_OK;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddOutletFR extends Fragment {
    TextView lblCoordinates;
    EditText txtDescription;
    EditText txtTitle;
    View thisView;
    Button btnUpload;
    ImageButton btnCamera;
    ImageView imgCamera;
    public boolean imagechanged = false;
    double lat = 0;
    double lon = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    int rotation;
    public AddOutletFR() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_add_outlet, container, false);
        changeOrientation();
        lblCoordinates = (TextView) thisView.findViewById(R.id.lblCoordnates);
        lblCoordinates.setText("("+Double.toString(lat)+","+Double.toString(lon)+")");
        btnCamera = (ImageButton) thisView.findViewById(R.id.btnCamera);
        btnUpload = (Button) thisView.findViewById(R.id.btnUpload);
        imgCamera = (ImageView) thisView.findViewById(R.id.imgCamera);
        txtDescription = (EditText) thisView.findViewById(R.id.txtDescription);
        txtTitle = (EditText) thisView.findViewById(R.id.txtTitle);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("btnUpload","clicked");
                String title = txtTitle.getText().toString();
                if(!title.trim().equals("")) {
                    Log.d("title=",title);
                    String pic = imagechanged?encodeImage():"";
                    insertOutlet(lat, lon, txtTitle.getText().toString(), txtDescription.getText().toString(), pic);
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "Please insert a title", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return thisView;
    }


    //use this to insert an outlet into the db
    //Refer to uiInsertOutlet if you want to use the result
    public void insertOutlet(double lat, double lon,String title, String description,String picture){
        Log.d("insertoutlet","started");
        new InsertOutlet().execute(Double.toString(lat),Double.toString(lon),title,description,picture);
        Log.d("insertoutlet","ended");
    }

    //this runs after insertoutlet. Use this if you need the outletid for anything
    public void uiInsertOutlet(String outletid){
        Log.d("uiinsertoutlet","started");
        if(outletid!=null)
            Toast.makeText(getContext(),"Outlet added",Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),"Something went wrong. Could not add outlet.",Toast.LENGTH_LONG).show();
        //TODO:Write code to set UI elements

        //Pop backstack twice to go back to the homefragment
        getFragmentManager().popBackStack();
        getFragmentManager().popBackStackImmediate();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_0){
                rotation = 0;
            } else if(((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_180){
                rotation = 1;
            } else if(((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation() == Surface.ROTATION_270){
                rotation = 2;
            } else{
                rotation = 3;
            }
            setPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = AddOutletFR.this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".png",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(AddOutletFR.this.getContext().getPackageManager()) != null) {
            // Creates a file for the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(AddOutletFR.this.getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setPic() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

//         Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize=3;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        if(rotation == 0) {
            bitmap = rotateImageToPortrait(bitmap, 90);
        } else if(rotation == 1) {
            bitmap = rotateImageToPortrait(bitmap, 270);
        } else if(rotation == 2) {
            bitmap = rotateImageToPortrait(bitmap, 180);
        }
        imgCamera.setBackgroundColor(Color.TRANSPARENT);
        imgCamera.setImageBitmap(bitmap);
        imgCamera.setScaleType(ImageView.ScaleType.FIT_START);
        imagechanged=true;
    }

    public String encodeImage(){
        try {
            imgCamera.buildDrawingCache();
            Bitmap bm = imgCamera.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
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


    public void setLocation(double latitude, double longitude){
        lat=latitude;
        lon=longitude;
    }

    public void changeOrientation(){
        if(getArguments() != null){
            switch (getArguments().getInt("orientation", 0)){
                case Surface.ROTATION_0:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
                case Surface.ROTATION_90:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case Surface.ROTATION_180:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                    break;
                case Surface.ROTATION_270:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                    break;
                default:
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    break;
            }
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

}
