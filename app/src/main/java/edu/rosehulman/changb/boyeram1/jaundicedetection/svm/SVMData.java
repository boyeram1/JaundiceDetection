package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

public class SVMData {

	public static final double[] sigmas = { 1 };
	public static final int[] nSupportVectors = { 84 };
	public static final int[] dimensions = { 294 };
        public static final double[] biases = { -0.1452 };
	public static final double[] NORMALIZATION_MINS = { 0, 0, 0, 0, 0, 0 };
    public static final double[] NORMALIZATION_MAXES = { 0, 0, 0, 0, 0, 0 };
;

	// normalize range 0 to Lmean 783.9325
    // normalize range 0 to Lstddev 328.4326
    // normalize range -144.4875 to Smean 322.1157
    // normalize range 0 to Sstddev 70.9895
    // normalize range -85.5067 to Tmean 235.5792
    // normalize range 0 to Tstddev 60.0199
	// Lspace r + g + b
    // Sspace r - b
    // Tspace r - 2 * g + b
}
