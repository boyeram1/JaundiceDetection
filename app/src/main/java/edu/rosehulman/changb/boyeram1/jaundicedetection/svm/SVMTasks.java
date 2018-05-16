package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;

public class SVMTasks extends AppCompatActivity {
	public static final String TAG = "JaundiceDetection";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final String P = "Performance";
	public static final int BLOCK_SIZE = 7;
	private static final int NUMBER_OF_FEATURES = 6;

	private Context mContext;
    private boolean mCaptureFlag = false;


    private Bitmap mImageBitmap;
	// location

	// SVM
	private SVM mSVM;
	private GetSVMAsyncTask mGetSVMAsyncTask;
	private int mSelectedNumBlocks = BLOCK_SIZE;
	private boolean mIsSVMReady = false;
	private long start = 0, end = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create an instance of Camera
		mContext = this;

		this.mGetSVMAsyncTask = new GetSVMAsyncTask();
		this.mGetSVMAsyncTask.execute();

		doAfterPictureTaken();
	}

	public void doAfterPictureTaken() {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 8;
//        // down sample
//
//        mImageBitmap = BitmapFactory.decodeByteArray(data, 0,
//                data.length, options);
//        mThumbnail.setImageBitmap(mImageBitmap);
//        // Un-comment to store the picture
//        if (mCaptureFlag){
//            new SavePictureAsynctask().execute(data);
//            mCaptureFlag = false;
//        }
//        int allPixels[] = new int[mImageBitmap.getWidth()
//                * mImageBitmap.getHeight()];
//        mImageBitmap.getPixels(allPixels, 0, mImageBitmap.getWidth(), 0, 0,
//                mImageBitmap.getWidth(), mImageBitmap.getHeight());
//        if (mIsSVMReady) {
//            start = System.currentTimeMillis();
//            new SVMForwardAsyncTask(mImageBitmap.getWidth(),
//                    mImageBitmap.getHeight(), mSelectedNumBlocks)
//                    .execute(allPixels);
//        }
    }
	
	private class LogRuntimesAsyncTask extends AsyncTask<int[], Void, Void>{

		@Override
		protected Void doInBackground(int[]... params) {
			File logsStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					getString(R.string.app_name));

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

	private class SavePictureAsynctask extends
			AsyncTask<byte[], Integer, Boolean> {

		@Override
		protected Boolean doInBackground(byte[]... params) {
			byte[] data = params[0];
			// TODO Auto-generated method stub
			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				Log.d(TAG,
						"Error creating media file, check storage permissions");
				return false;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ pictureFile.getAbsolutePath())));
//				Log.d(TAG, pictureFile.getName() + " Saved");
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			return true;
		}

		private File getOutputMediaFile(int type) {
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.

			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					getString(R.string.app_name));

			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				if (!mediaStorageDir.mkdirs()) {
					Log.d(TAG, "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.US).format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "Pic_" + timeStamp + "_" + mSelectedNumBlocks
						+ ".jpg");
			} else {
				Log.d(TAG, "Not image?");
				return null;
			}

			return mediaFile;
		}

		protected void onPostExecute(Boolean result) {
			if (result) {
//				Log.d(TAG, "saved picture successfully");
				Toast.makeText(mContext, "Saved", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "Save Failed", Toast.LENGTH_SHORT)
						.show();
				Log.d(TAG, "saving picture failed");
			}
		}
	}
}
