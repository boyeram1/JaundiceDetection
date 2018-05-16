package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

public class SVMForwardAsyncTask extends AsyncTask<int[], Integer, Double> {
    private int imageWidth, imageHeight, selectedBlockSize;

    public SVMForwardAsyncTask(int imageWidth, int imageHeight,
                               int selectedBlockSize) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.selectedBlockSize = selectedBlockSize;
    }

    protected void onPostExecute(Double result) {
        end = System.currentTimeMillis();
        setResult(result);
        long totalTime = end - start;
        Log.d(MobileSunset.TAG, String.format("Total time: %.3fs, for %d features",
                totalTime / 1000.0, MobileSunset.BLOCK_SIZE));
        new LogRuntimesAsyncTask().execute(new int [] {mSelectedNumBlocks, (int) totalTime, imageWidth, imageHeight});
        //mCamera.takePicture(null, null, mPicture);
    }

    @Override
    protected Double doInBackground(int[]... params) {
        return mSVM.forward(extractFeatures(params[0]));
    }

    private double[] extractFeatures(int[] img) {
        double[] features = new double[294];
//			for (int r = 0; r < selectedBlockSize; r++) {
//				for (int c = 0; c < selectedBlockSize; c++) {
//					calculateBlockMeans(img, imageWidth, imageHeight,
//							selectedBlockSize, r, c, features);
//					calculateBlockStandardDeviation(img, imageWidth, imageHeight,
//							selectedBlockSize, r, c, features);
//				}
//			}
        return normalizeFeatures(features);
    }

    private double[] normalizeFeatures(double[] features) {
        double[] normalized = new double[294];

        for (int numSector = 0; numSector < 49; numSector++) {
            for (int numFeature = 0; numFeature < NUMBER_OF_FEATURES; numFeature++) {
                normalized[numFeature] = features[numSector*numFeature + numFeature] - SVMData.NORMALIZATION_MINS[numFeature]
                        / SVMData.NORMALIZATION_MAXES[numFeature];
            }
        }

        return normalized;
    }

    private void calculateBlockMeans(int[] img, int imageWidth,
                                     int imageHeight, int selectedNumBlocks, int r, int c,
                                     double[][] features) {
        double rSum = 0, gSum = 0, bSum = 0;
        int pixelCount = (imageHeight / selectedNumBlocks)
                * (imageWidth / selectedNumBlocks);
        int depthBlocks = imageHeight / selectedNumBlocks;
        int widthBlocks = imageWidth / selectedNumBlocks;
        int offset = (r * (imageWidth) * (imageHeight / selectedNumBlocks)) // OK, past row blocks
                + (c * (imageWidth / selectedNumBlocks)); // OK, to given column block
        int featureOffset = r * selectedNumBlocks + c;
        for (int i = 0; i < depthBlocks; i++) {
            int rowOffset = offset
                    + i * imageWidth; // OK, skips down i single rows
            for (int j = 0; j < widthBlocks; j++) {
                int pixel = img[rowOffset + j]; // OK, skips over j pixels
                rSum += Color.red(pixel);
                gSum += Color.green(pixel);
                bSum += Color.blue(pixel);
            }
        }
        features[featureOffset][R_MEAN] = rSum / pixelCount;
        features[featureOffset][G_MEAN] = gSum / pixelCount;
        features[featureOffset][B_MEAN] = bSum / pixelCount;
    }

    private void calculateBlockStandardDeviation(int[] img, int imageWidth,
                                                 int imageHeight, int selectedNumBlocks, int r, int c,
                                                 double[][] features) {
        double rSum = 0, gSum = 0, bSum = 0;
        int pixelCount = (imageHeight / selectedNumBlocks)
                * (imageWidth / selectedNumBlocks);
        int depthBlocks = imageHeight / selectedNumBlocks;
        int widthBlocks = imageWidth / selectedNumBlocks;
        int offset = (r * (imageWidth) * (imageHeight / selectedNumBlocks)) // OK, past row blocks
                + (c * (imageWidth / selectedNumBlocks)); // OK, to given column block
        int featureOffset = r * selectedNumBlocks + c;
        for (int i = 0; i < depthBlocks; i++) {
            int rowOffset = offset
                    + i * imageWidth; // OK, skips down i single rows
            for (int j = 0; j < widthBlocks; j++) {
                int pixel = img[rowOffset + j]; // OK, skips over j pixels
                rSum += ((Color.red(pixel) - features[featureOffset][R_MEAN]) * (Color.red(pixel) - features[featureOffset][R_MEAN]));
                gSum += ((Color.green(pixel) - features[featureOffset][G_MEAN]) * (Color
                        .green(pixel) - features[featureOffset][G_MEAN]));
                bSum += ((Color.blue(pixel) - features[featureOffset][B_MEAN]) * (Color
                        .blue(pixel) - features[featureOffset][B_MEAN]));
            }
        }

        features[featureOffset][R_STANDARD_DEVIATION] = Math
                .sqrt(rSum / (pixelCount - 1));
        features[featureOffset][G_STANDARD_DEVIATION] = Math
                .sqrt(gSum / (pixelCount - 1));
        features[featureOffset][B_STANDARD_DEVIATION] = Math
                .sqrt(bSum / (pixelCount - 1));
    }

}
