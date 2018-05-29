package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;

public class SVMTasks {
	public static final String TAG = "JaundiceDetection";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String P = "Performance";
	public static final int BLOCK_SIZE = 7;
	private static final int NUMBER_OF_FEATURES = 6;

	private Context mContext;
    private SVMConsumer mSVMConsumer;

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

	public void sendBitmapToSVM(Bitmap imageBitmap) {
        Log.d(TAG, "Starting SVM Analysis");
        int allPixels[] = new int[imageBitmap.getWidth()
                * imageBitmap.getHeight()];
        imageBitmap.getPixels(allPixels, 0, imageBitmap.getWidth(), 0, 0,
                imageBitmap.getWidth(), imageBitmap.getHeight());
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
					Log.d(TAG, "failed to create log directory");
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
		}

		protected void onPostExecute(Double result) {
			end = System.currentTimeMillis();
			long totalTime = end - start;
			Log.d(TAG, String.format("Total time: %.3fs, for %d features",
					totalTime / 1000.0, mSelectedNumBlocks));
			new LogRuntimesAsyncTask().execute(new int [] {mSelectedNumBlocks, (int) totalTime, imageWidth, imageHeight});
			mSVMConsumer.onImageProcessed(result);
		}

		@Override
		protected Double doInBackground(int[]... params) {
			return mSVM.forward(extractFeatures(params[0]));
		}

		private double[] extractFeatures(int[] img) {
			double[] features = new double[294];
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
	}

	private class GetSVMAsyncTask extends AsyncTask<Void, Void, SVM> {
		protected void onProgressUpdate(Void... params) {
			mIsSVMReady = false;
		}

		protected void onPostExecute(SVM result) {
			mIsSVMReady = true;
			mSVM = result;
			Log.d(TAG, "done Loading SVM");
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
        void onImageProcessed(Double result);
    }
}
