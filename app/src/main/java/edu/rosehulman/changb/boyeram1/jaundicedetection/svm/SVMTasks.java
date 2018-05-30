package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

public class SVMTasks {
	public static final String TAG = "JD-SVMTasks";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String P = "JD-Performance";
	public static final int BLOCK_SIZE = 7;
	private static final int NUMBER_OF_FEATURES = 6;

	private Context mContext;
    private SVMConsumer mSVMConsumer;
    private TestResult mTestResult;

    // SVM
	private SVM mSVM;
	private GetSVMAsyncTask mGetSVMAsyncTask;
	private int mSelectedNumBlocks = BLOCK_SIZE;
	private boolean mIsSVMReady = false;
	private long start = 0, end = 0;

	public SVMTasks(Context context, SVMConsumer mSVMConsumer) {
	    this.mContext = context;
        this.mGetSVMAsyncTask = new GetSVMAsyncTask();
        this.mGetSVMAsyncTask.execute();
        this.mSVMConsumer = mSVMConsumer;
    }

	public void sendBitmapToSVM(Bitmap imageBitmap, TestResult testResult) {
        Log.d(TAG, "Starting SVM Analysis");
        mTestResult = testResult;
        int allPixels[] = new int[imageBitmap.getWidth()
                * imageBitmap.getHeight()];
        imageBitmap.getPixels(allPixels, 0, imageBitmap.getWidth(), 0, 0,
                imageBitmap.getWidth(), imageBitmap.getHeight());
        Log.d(TAG, "Loaded bitmap into int pixel array: " + imageBitmap.getWidth() + " x " + imageBitmap.getHeight());
        Log.d(TAG, "int pixel array size: " + allPixels.length);
        if (mIsSVMReady) {
            start = System.currentTimeMillis();
            new SVMForwardAsyncTask(imageBitmap.getWidth(),
                    imageBitmap.getHeight(), mSelectedNumBlocks)
                    .execute(allPixels);
        }
    }
	
	private class LogRuntimesAsyncTask extends AsyncTask<int[], Void, Void>{

		@Override
		protected Void doInBackground(int[]... params) {
			File logsStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					mContext.getString(R.string.app_name));

			// Create the storage directory if it does not exist
			if (!logsStorageDir.exists()) {
				if (!logsStorageDir.mkdirs()) {
					Log.d(P, "failed to create log directory");
				}
			}
			File logFile;
			
			logFile = new File(logsStorageDir.getPath() + File.separator
						+ "JaundiceAppLog.txt");
			
			FileOutputStream fOut;
			try {
				fOut = new FileOutputStream(logFile, true);
				
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				osw.write(String.format("%d, %.3f, %d, %d\n", params[0][0], params[0][1]/1000.0, params[0][2], params[0][3]));
				osw.flush();
				osw.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	private class SVMForwardAsyncTask extends AsyncTask<int[], Integer, Double> {
		private int imageWidth, imageHeight, selectedBlockSize;

		public SVMForwardAsyncTask(int imageWidth, int imageHeight,
				int selectedBlockSize) {
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
			this.selectedBlockSize = selectedBlockSize;
            Log.d(TAG, "SVM forward task created");
        }

		protected void onPostExecute(Double result) {
			end = System.currentTimeMillis();
			long totalTime = end - start;
			Log.d(TAG, String.format("Total time: %.3fs, for %d features",
					totalTime / 1000.0, NUMBER_OF_FEATURES));
			new LogRuntimesAsyncTask().execute(new int [] {NUMBER_OF_FEATURES, (int) totalTime, imageWidth, imageHeight});
			mSVMConsumer.onImageProcessed(mTestResult, result);
		}

		@Override
		protected Double doInBackground(int[]... params) {
			return mSVM.forward(extractFeatures(params[0]));
		}

		private double[] extractFeatures(int[] img) {
			double[] features = new double[BLOCK_SIZE * BLOCK_SIZE * NUMBER_OF_FEATURES]; // 294
			for (int r = 0; r < BLOCK_SIZE; r++) {
			    for (int c = 0; c < BLOCK_SIZE; c++) {
//			        Log.d(TAG, "Extracting - Row: " + r + " Col: " + c);
			        extractFeaturesByRowCol(img, imageWidth, imageHeight, r, c, features);
                }
            }
            Log.d(TAG, "Extracted " + features.length + " elements");
			return normalizeFeatures(features);
		}

        private void extractFeaturesByRowCol(int[] img, int imageWidth, int imageHeight, int r, int c, double[] features) {
            double rSum = 0, gSum = 0, bSum = 0;
            int depthBlocks = imageHeight / BLOCK_SIZE;
            int widthBlocks = imageWidth / BLOCK_SIZE;
            int pixelCount = depthBlocks * widthBlocks;
            int offset = (r * (imageWidth) * (imageHeight / BLOCK_SIZE)) // OK, past row blocks
                    + (c * (imageWidth / BLOCK_SIZE)); // OK, to given column block
            int featureOffset = r * BLOCK_SIZE + c;
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
            double R = rSum / pixelCount;
            double G = gSum / pixelCount;
            double B = bSum / pixelCount;

            features[featureOffset * NUMBER_OF_FEATURES] = R;
            features[featureOffset * NUMBER_OF_FEATURES + 1] = G;
            features[featureOffset * NUMBER_OF_FEATURES + 2] = B;
            features[featureOffset * NUMBER_OF_FEATURES + 3] = R + G + B;
            features[featureOffset * NUMBER_OF_FEATURES + 4] = R - B;
            features[featureOffset * NUMBER_OF_FEATURES + 5] = R - 2 * G + B;

//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + " R: " + features[featureOffset * NUMBER_OF_FEATURES]);
//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + 1 + " G: " + features[featureOffset * NUMBER_OF_FEATURES + 1]);
//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + 2 + " B: " + features[featureOffset * NUMBER_OF_FEATURES + 2]);
//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + 3 + " L: " + features[featureOffset * NUMBER_OF_FEATURES + 3]);
//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + 4 + " S: " + features[featureOffset * NUMBER_OF_FEATURES + 4]);
//            Log.d(TAG, featureOffset * NUMBER_OF_FEATURES + 5 + " T: " + features[featureOffset * NUMBER_OF_FEATURES + 5]);
        }

		private double[] normalizeFeatures(double[] features) {
			double[] normalized = new double[BLOCK_SIZE * BLOCK_SIZE * NUMBER_OF_FEATURES]; // 294

			for (int row = 0; row < BLOCK_SIZE * BLOCK_SIZE; row++) {
				for (int col = 0; col < NUMBER_OF_FEATURES; col++) {
				    int elementNum = row * NUMBER_OF_FEATURES + col;
					normalized[elementNum] =
                            (features[elementNum] - SVMData.NORMALIZATION_MINS[col])
							/ SVMData.NORMALIZATION_MAXES[col];
//					Log.d(TAG, "normalized element: " + elementNum + " val: "
//                            + features[elementNum] + " to " + normalized[elementNum]);
				}
			}
            Log.d(TAG, "Normalized " + normalized.length + " elements");
            return normalized;
		}

	}

	private class GetSVMAsyncTask extends AsyncTask<Void, Void, SVM> {
		protected void onProgressUpdate(Void... params) {
			mIsSVMReady = false;
		}

		protected void onPostExecute(SVM result) {
			mIsSVMReady = true;
			mSVM = result;
			Log.d(TAG, "Done Loading SVM");
		}

		@Override
		protected SVM doInBackground(Void... params) {
			// Log.d(TAG, "started async getSVMAsyncTask");
			SVM newSVM = new SVM(mContext, 7);
			if (isCancelled())
				return null;
			mSVM = newSVM;
			return mSVM;
		}
	}

    public interface SVMConsumer {
        void onImageProcessed(TestResult testResult, Double result);
    }
}
