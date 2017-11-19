package com.example.filoble.hi;

/**
 * Created by filoble on 25/10/17.
 */

public class NNPrediction {

    String[] mPredictionLabel;
    double[] mPredictionAccuracy;

    NNPrediction(){
        //Do Nothing;
    }
    NNPrediction(String[] predictionLabel, double[] predictionAccuracy){
        mPredictionAccuracy = predictionAccuracy;
        mPredictionLabel = predictionLabel;
    }
    public void setPredictionValues(String[] predictionLabel, double[] predictionAccuracy){
        mPredictionLabel = predictionLabel;
        mPredictionAccuracy = predictionAccuracy;
    }
    public String[] getPredictionLabels(){ return  mPredictionLabel; }
    public double[] getPredictionAccuracy(){ return mPredictionAccuracy; }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < mPredictionAccuracy.length; ++i){
            stringBuilder.append(mPredictionLabel[i].toString() + " -- " + String.valueOf(mPredictionAccuracy[i]) + "\n");
        }
        return stringBuilder.toString();
    }
}
