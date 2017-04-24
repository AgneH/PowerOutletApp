package com.example.agneh.poweroutletapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by agneh on 2017-03-28.
 */

public class AddOutletFR extends Fragment {
    TextView lblCoordinates;
    EditText txtDescription;
    EditText txtTitle;
    View thisView;
    Button btnCamera,btnUpload;
    ImageView imgCamera;
    double lat = 0;
    double lon = 0;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    public AddOutletFR() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_add_outlet, container, false);
        lblCoordinates = (TextView) thisView.findViewById(R.id.lblCoordnates);
        lblCoordinates.setText("("+Double.toString(lat)+","+Double.toString(lon)+")");
        btnCamera = (Button) thisView.findViewById(R.id.btnCamera);
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


//    private void openCameraIntent() {
//        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (pictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
//            startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = AddOutletFR.this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
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
        // Get the dimensions of the View
        int targetW = imgCamera.getWidth();
        int targetH = imgCamera.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        if(photoW>photoH) {
            bitmap = rotateImageToPortrait(bitmap, 90);
        }
        imgCamera.setImageBitmap(bitmap);
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


    public void setLocation(double latitude, double longitude){
        lat=latitude;
        lon=longitude;
    }

}
