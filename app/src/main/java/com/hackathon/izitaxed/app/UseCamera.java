package com.hackathon.izitaxed.app;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by breezed on 06/05/2014.
 */
public class UseCamera extends Activity {
    private CameraPreview mCameraPreview;
    public  Context passedContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void run(Context context,FrameLayout preview) {
        passedContext = context;
        MainActivity.mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(context, MainActivity.mCamera);
        preview.addView(mCameraPreview);

    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode("continuous-picture");
            camera.setParameters(params);
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

}