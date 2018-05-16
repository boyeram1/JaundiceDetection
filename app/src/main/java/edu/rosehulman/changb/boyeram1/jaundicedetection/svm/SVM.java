package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

/**
 * A trained support vector machine for Gaussian RBF kernel functions.
 * 
 * I hacked MATLAB to get it to produce the real-valued output.
 * 2013a/toolbox/stats/stats/ svmclassify.m /private/svmdecision.m
 * 
 * 
 * @author Matt Boutell. Created Jan 7, 2013.
 */
public class SVM {

	enum DataSource {
		USE_MATLAB_BUILTIN_FOR_TOY_PROBLEM, USE_SCHWAIGHOFER_FOR_TOY_PROBLEM, USE_MATLAB_BUILTIN_FOR_JAUNDICE, USE_SCHWAIGHOFER_FOR_JAUNDICE
	};

	private static DataSource source = DataSource.USE_MATLAB_BUILTIN_FOR_JAUNDICE;
	private Context mContext;
	private int nSupportVectors;
	private int dimension;
	private double[][] mSupportVectors;
	private double[] mAlphas;
	private double bias;
	private double sigma = 3; // TODO: read from file

	/**
	 * Create a new empty, untrained SVM.
	 */
	public SVM(Context context, int blockSize) {
		this.mContext = context;
		readParameters();
	}

	/**
	 * Computes the output for a given input assuming a gaussian RBF kernel.
	 * Scholkopf uses for rbf kernel, K =
	 * exp(-sum((X1i-X2i)^2)/(NET.kernelpar(1)*NET.nin)) =
	 * exp(-sqds/(sigma*dim))
	 * 
	 * MATLAB built-in uses: -sqds / (2*sigma^2) [see rbf_kernel.m], and so do
	 * other other sources.
	 * 
	 * @param outputFeatures
	 */
	public double forward(double[] outputFeatures) {
		// Add bias then loop over SV with corresponding alphas.
		double y1 = this.bias;
		for (int svIdx = 0; svIdx < this.nSupportVectors; svIdx++) {
			double dist2 = squaredistance(outputFeatures,
					mSupportVectors[svIdx]);
			switch (source) {
			case USE_MATLAB_BUILTIN_FOR_TOY_PROBLEM:
			case USE_MATLAB_BUILTIN_FOR_JAUNDICE:
				y1 += mAlphas[svIdx]
						* Math.exp(-dist2 * 1/Math.sqrt (2));
				break;
			case USE_SCHWAIGHOFER_FOR_TOY_PROBLEM:
			case USE_SCHWAIGHOFER_FOR_JAUNDICE:
				y1 += mAlphas[svIdx]
						* Math.exp(-dist2 / (this.sigma * this.sigma));
			}
//			Log.d(SVMTasks.TAG, String.format("forwarding %d", svIdx));
		}
		return y1;
	}

	private double squaredistance(double[] x, double[] y) {
//		Log.d(SVMTasks.TAG, String.format("x=%d, y=%d dim = %d", x.length, y.length, this.dimension));
		double distance = 0.0;
		assert (x.length == y.length);
		assert (x.length == this.dimension);
		for (int i = 0; i < x.length; i++) {
			distance += (x[i] - y[i]) * (x[i] - y[i]);
		}
		return distance;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String line = String.format(Locale.US,
				"SVM d=%d, %d support vectors, bias = %f:\n", this.dimension,
				this.nSupportVectors, this.bias);
		buffer.append(line);
		for (int svIdx = 0; svIdx < this.nSupportVectors; svIdx++) {
			buffer.append(String.format("alpha = %20.16f, SV = ",
					mAlphas[svIdx]));
			for (int dIdx = 0; dIdx < this.dimension; dIdx++) {
				buffer.append(String.format("%20.16f ",
						mSupportVectors[svIdx][dIdx]));
			}
			buffer.append('\n');
		}
		return buffer.toString();
	}

	private void readParameters() {
//		long start = System.currentTimeMillis();
		String trainedSVMFile = "trained_jaundice_android";
		Log.d(SVMTasks.TAG, trainedSVMFile);
		int trainedSVMFileID = mContext.getResources().getIdentifier(trainedSVMFile, "raw", mContext.getPackageName());
		Log.d(SVMTasks.TAG, trainedSVMFileID + "");
		InputStream inputStream = mContext.getResources().openRawResource(trainedSVMFileID);
		Scanner fileScanner = new Scanner(inputStream);
		this.sigma = SVMData.sigmas[0];
		Log.d(SVMTasks.TAG, String.format("sigma : %.8f", this.sigma));
		this.nSupportVectors = SVMData.nSupportVectors[0];
				Log.d(SVMTasks.TAG,	String.format("numSupo : %d", this.nSupportVectors));
		this.dimension = SVMData.dimensions[0];
		Log.d(SVMTasks.TAG, String.format("dim : %d", this.dimension));
		this.bias = SVMData.biases[0];
		Log.d(SVMTasks.TAG, String.format("bias : %.8f", this.bias));

		mSupportVectors = new double[nSupportVectors][dimension];
		mAlphas = new double[nSupportVectors];

		for (int svIdx = 0; svIdx < this.nSupportVectors; svIdx++) {
			mSupportVectors[svIdx] = new double[this.dimension];
			String [] vectors = fileScanner.nextLine().split(" ");
			for (int dIdx = 0; dIdx < this.dimension; dIdx++) {
//				Log.d(SVMTasks.TAG, String.format("didx = %d, vectors = %d", dIdx, vectors.length));
				mSupportVectors[svIdx][dIdx] = Double.parseDouble(vectors[dIdx]);
			}
//			Log.d(SVMTasks.TAG, String.format("reading %d", svIdx));
		}
		String [] alphas = fileScanner.nextLine().split(" ");
		for (int svIdx = 0; svIdx < this.nSupportVectors; svIdx++) {
			mAlphas[svIdx] = Double.parseDouble(alphas[svIdx]);
		}
		fileScanner.close();
//		long end = System.currentTimeMillis();
//		long total = end - start;
//		Log.d(SVMTasks.TAG, String.format("totaltime reading %d", total));
//		Log.d(SVMTasks.TAG, String.format("msupport=%d mdim=%d ", nSupportVectors, this.dimension));

	}
}
