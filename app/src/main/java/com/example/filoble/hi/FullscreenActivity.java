package com.example.filoble.hi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "myApp";
    private RelativeLayout relativeLayoutStartScreen;
    private RelativeLayout relativeLayoutResultScreen;
    private ImageView mImageView;
    private TextView mTextView;
    private ProgressBar mProgressBar;
    private static Context instance;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        instance = this;
        relativeLayoutStartScreen = (RelativeLayout) findViewById(R.id.startScreen);
        relativeLayoutResultScreen = (RelativeLayout) findViewById(R.id.imageForResult);
        Button button = (Button) findViewById(R.id.imageButton);
        mImageView = (ImageView) findViewById(R.id.imageObtained);
        mTextView = (TextView) findViewById(R.id.nnResult);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);
        relativeLayoutStartScreen.setVisibility(View.VISIBLE);
        relativeLayoutResultScreen.setVisibility(View.GONE);
        mTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Log.d(TAG ,"image received");
            relativeLayoutStartScreen.setVisibility(View.GONE);
            mTextView.setVisibility(View.GONE);
            relativeLayoutResultScreen.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.VISIBLE);
            Log.d("onActivityResult: ", "Sending to network ...");
            mImageView.setImageBitmap(imageBitmap);
            mProgressBar.setVisibility(View.VISIBLE);
            Log.d("WebConnect: ", "doing Background work...");
            NNRetrieve nnRetrieve = new NNRetrieve(imageBitmap);
            nnRetrieve.sendImage();
        }
    }

    public static Context getContext(){
        return instance;
    }
}

