package com.example.filoble.hi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by filoble on 16/10/17.
 */
public class NNRetrieve {
    private Bitmap mImageToSend;
    private String url = "http://192.168.43.241:3000/api/uploadface/";
    private NNPrediction nnPrediction = null;
    private Context context = FullscreenActivity.getContext();
    private ImageView mImageView = (ImageView) ((Activity)context).findViewById(R.id.imageObtained);
    private TextView mTextView = (TextView) ((Activity)context).findViewById(R.id.nnResult);
    private ProgressBar mProgressBar = (ProgressBar) ((Activity)context).findViewById(R.id.loading_spinner);

    public NNRetrieve(Bitmap imageToSend) {
        mImageToSend = imageToSend;
    }

    private byte[] getImageAsByteArray(){
        Log.d("NNRetrieve: ", "encoding image to string");
        byte[] enc = null;
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mImageToSend.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            enc = stream.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
        }
        return enc;
    }

    public void sendImage(){

        Log.d("NNRetrieve: ", "Sending Image");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                nnPrediction = parseJSONString(new String(response.data));
                showPredictions();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, DataPart> getByteData(){
                Map<String, DataPart> params = new HashMap<>();
                long imageName = System.currentTimeMillis();
                params.put("pic", new DataPart(imageName + ".jpg", getImageAsByteArray()));
                return params;
            }
        };
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(2147483640, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FullscreenActivity.getContext()).add(volleyMultipartRequest);
    }


    private NNPrediction parseJSONString(String resp){
        Log.d("NNRetrieve: ", "parsing JSON response");
        Log.i("parse JSON String", resp);
        String[] labels = null;
        double[] acc = null;
        try {
            JSONObject jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("predictions");
            int len = jsonArray.length();
            labels = new String[len];
            acc = new double[len];
            for(int i = 0; i < len; ++i) {
                JSONObject jsonObjecti = jsonArray.getJSONObject(i);
                labels[i] = jsonObjecti.getString("label");
                acc[i] = jsonObjecti.getDouble("accuracy");
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return new NNPrediction(labels, acc);
    }

    private void showPredictions(){
        mImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        Log.d("onActivityResult: ", "Reading results ...");
        if(nnPrediction == null) {
            mTextView.setText("No Result Obtained");
        }
        else {
            mTextView.setText(nnPrediction.toString());
            Log.d("final predictions are: ", nnPrediction.toString());
        }
    }
}
