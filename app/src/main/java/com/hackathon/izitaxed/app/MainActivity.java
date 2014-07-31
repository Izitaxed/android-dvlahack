package com.hackathon.izitaxed.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.media.Image;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    static public FrameLayout preview;
    static public UseCamera cam;
    static public Camera mCamera;
    static public TextView update;
    static public Integer sendOption =1;
    static public String macAddress;
    static public Context context;
    static public ImageView thumb;
    static public List<String> pics;
    static public List<Bitmap> picsBit;
    static public TextView vrm_box;
    public Bitmap currBmp;
    public Integer submitResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayViewWelcome();
        pics = new ArrayList<String>();
        picsBit = new ArrayList<Bitmap>();
    }

    protected void displayViewWelcome() {
        setContentView(R.layout.welcome);

        final RelativeLayout continueButton = (RelativeLayout) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayViewCheck();
            }
        });
    }

    protected  void displayViewCheck() {
    setContentView(R.layout.check);
        final TextView checkTitle = (TextView) findViewById(R.id.checkTitle);
        final TextView checkBox1 = (TextView) findViewById(R.id.checkBox1);
        final RelativeLayout checkButton = (RelativeLayout) findViewById(R.id.checkButton);
        final RelativeLayout vehDetail = (RelativeLayout) findViewById(R.id.vehDetail);
        final TextView taxed = (TextView) findViewById(R.id.taxed);
        final TextView unTaxed = (TextView) findViewById(R.id.unTaxed);
        final TextView notFound = (TextView) findViewById(R.id.notFound);
        final TextView checkThanks = (TextView) findViewById(R.id.checkThanks);
        final TextView checkNowSubmit = (TextView) findViewById(R.id.checkNowSubmit);
        final RelativeLayout continueButton = (RelativeLayout) findViewById(R.id.continueButton);
        final TextView vrm_box = (TextView) findViewById(R.id.vrm_box);
        final TextView make = (TextView) findViewById(R.id.make);
        final TextView model = (TextView) findViewById(R.id.model);
        final TextView color = (TextView) findViewById(R.id.color);

        checkTitle.setVisibility(View.VISIBLE);
        checkBox1.setVisibility(View.VISIBLE);
        checkButton.setVisibility(View.VISIBLE);
        vehDetail.setVisibility(View.GONE);
        taxed.setVisibility(View.GONE);
        unTaxed.setVisibility(View.GONE);
        notFound.setVisibility(View.GONE);
        checkThanks.setVisibility(View.GONE);
        checkNowSubmit.setVisibility(View.GONE);
        continueButton.setVisibility(View.GONE);
        vrm_box.setEnabled(true);

        final RelativeLayout backButton = (RelativeLayout) findViewById(R.id.backButton);

         checkButton.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 submitCheck();
             }
         });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayViewCheck();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView vrmHold = (TextView) findViewById(R.id.vrm_box);
                displayViewCamera(vrmHold.getText().toString());
            }
        });

    }

    protected void submitCheck() {
        final TextView checkTitle = (TextView) findViewById(R.id.checkTitle);
        final TextView checkBox1 = (TextView) findViewById(R.id.checkBox1);
        final RelativeLayout checkButton = (RelativeLayout) findViewById(R.id.checkButton);
        final RelativeLayout vehDetail = (RelativeLayout) findViewById(R.id.vehDetail);
        final TextView taxed = (TextView) findViewById(R.id.taxed);
        final TextView unTaxed = (TextView) findViewById(R.id.unTaxed);
        final TextView notFound = (TextView) findViewById(R.id.notFound);
        final TextView checkThanks = (TextView) findViewById(R.id.checkThanks);
        final TextView checkNowSubmit = (TextView) findViewById(R.id.checkNowSubmit);
        final RelativeLayout continueButton = (RelativeLayout) findViewById(R.id.continueButton);
        final TextView make = (TextView) findViewById(R.id.make);
        final TextView model = (TextView) findViewById(R.id.model);
        final TextView color = (TextView) findViewById(R.id.color);


        final TextView vrm_box = (TextView) findViewById(R.id.vrm_box);

        String vrm ="";
        if(vrm_box.getText().toString()==null) {
            return;
        } else {
            vrm = vrm_box.getText().toString();
        }
        new QueryRequest().execute(
                checkTitle,
                checkBox1,
                checkButton,
                vehDetail,
                taxed,
                unTaxed,
                notFound,
                checkThanks,
                checkNowSubmit,
                continueButton,
                vrm_box,
                make,
                model,
                color,
                vrm
        );

    }

    protected void displayViewCamera(String vrm) {
        setContentView(R.layout.camera);
        RelativeLayout submitView = (RelativeLayout) findViewById(R.id.submitted);
        submitView.setVisibility(View.GONE);

        final RelativeLayout cameraButton =(RelativeLayout) findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getPhoto();
            }
        });

        final RelativeLayout submitButton =(RelativeLayout) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vrm_box = (TextView) findViewById(R.id.vrm_box);

                String json = submitObj(
                    vrm_box.getText().toString(),
                    picsBit,
                    Build.MANUFACTURER + " " + Build.MODEL
                ).toString();

                RelativeLayout submitView = (RelativeLayout) findViewById(R.id.submitted);
                new QuerySubmit().execute(json, submitView);

            }
        });

        try {
            preview = (FrameLayout) findViewById(R.id.previewFrame);
        } catch (Exception e) {
            preview = null;
        }
        context = this;
        if(preview != null) {
            cam = null;
            startupCam(context, preview);
        }

        TextView vrm_box = (TextView) findViewById(R.id.vrm_box);
        vrm_box.setText(vrm);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        try {
            preview = (FrameLayout) findViewById(R.id.previewFrame);
        } catch (Exception e) {
           preview = null;
        }
        context = this;
        if(preview != null) {
            cam = null;
            startupCam(context, preview);
        }
    }


    static public void startupCam(Context context,FrameLayout preview) {
        cam = new UseCamera();
        cam.run(context,preview);
    }

    public void getPhoto() {
        if(mCamera != null) {
            mCamera.takePicture(null, null, mPicture);
        }
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File pictureFile = getOutputMediaFile();
        if (data == null) {
            return;
        }
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            if(pictureFile != null) {
                System.out.println(pics.size());
                pics.add(pictureFile.getAbsolutePath());
            }
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }
        try {
            loadPhotos();
            mCamera.startPreview();
        } catch (Exception e) {

        }
    }
};

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    private void loadPhotos(){
        try {
            if (pics == null) {
                return;
            }
            Integer i2 = 0;
            for (Integer i = pics.size() - 1; i >= 0; i--) {
                i2++;
                File imgFile = new File(pics.get(i));
                if (imgFile.exists()) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);

                    if (i2 <= 3) {
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(myBitmap, myBitmap.getWidth() / 3, myBitmap.getHeight() / 3, true);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                        myBitmap = null;
                        scaledBitmap = null;

                        System.out.println("i2 " + i2);
                        if (i2 == 1) {
                            ImageView imgHold1 = (ImageView) findViewById(R.id.photoHold1);
                            imgHold1.setImageBitmap(rotatedBitmap);
                            try{picsBit.remove(0);}catch (Exception e){};
                            picsBit.add(0,rotatedBitmap);
                        }
                        if (i2 == 2) {
                            ImageView imgHold2 = (ImageView) findViewById(R.id.photoHold2);
                            imgHold2.setImageBitmap(rotatedBitmap);
                            try{picsBit.remove(1);}catch (Exception e){};
                            picsBit.add(1,rotatedBitmap);
                        }
                        if (i2 == 3) {
                            ImageView imgHold3 = (ImageView) findViewById(R.id.photoHold3);
                            imgHold3.setImageBitmap(rotatedBitmap);
                            try{picsBit.remove(2);}catch (Exception e){};
                            picsBit.add(2,rotatedBitmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String encodeToBase64(Bitmap image) {
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

    public JSONObject submitObj(String vrm,List<Bitmap> picsBit, String phoneDetail) {


        /*
        {"VRM":"DAVECAR",
                "DateTime":"2012-12-12",
                "MACAddress":"02:30",
                "Photo": ["MDEwMTAxMDE=","MDEwMTAxMDE="],
            "GpsLocation":{"type" : "point", "coordinates" :["40","50"]},
            "BrowserInfo":"Tester"
        }
        */
        JSONObject jSub = new JSONObject();
        try {
            jSub.put("VRM", vrm);
            jSub.put("DateTime",System.currentTimeMillis());
            jSub.put("MACAddress",getMacAddress());
            jSub.put("BrowserInfo",phoneDetail);

            JSONArray picObj = new JSONArray();
            for (Bitmap pn : picsBit) {
                picObj.put(encodeToBase64(pn));
            }
            jSub.put("Photo",picObj);

            JSONObject coordObj = new JSONObject();
            coordObj.put("type","point");

            GPSTracker mGPS = new GPSTracker(this);

            try {
                Location loc = mGPS.getLocation();
                JSONArray coordinatesObj = new JSONArray();
                coordinatesObj.put(""+loc.getLongitude());
                coordinatesObj.put(""+loc.getLatitude());
                coordObj.put("coordinates", coordinatesObj);
                jSub.put("GpsLocation",coordObj);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                JSONArray coordinatesObj = new JSONArray();
                coordinatesObj.put("0");
                coordinatesObj.put("0");
                coordObj.put("coordinates", coordinatesObj);
                jSub.put("GpsLocation",coordObj);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return jSub;
    }

    private String getMacAddress() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

}




