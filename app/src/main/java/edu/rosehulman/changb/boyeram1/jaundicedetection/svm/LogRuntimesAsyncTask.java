package edu.rosehulman.changb.boyeram1.jaundicedetection.svm;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;

public class LogRuntimesAsyncTask extends AsyncTask<int[], Void, Void> {

    @Override
    protected Void doInBackground(int[]... params) {
        File logsStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),  getString(R.string.app_name));

        // Create the storage directory if it does not exist
        if (!logsStorageDir.exists()) {
            if (!logsStorageDir.mkdirs()) {
                Log.d("LogRunAsyncTask", "failed to create log directory");
            }
        }
        File logFile;

        logFile = new File(logsStorageDir.getPath() + File.separator
                + "MobileSunsetLog.txt");

        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(logFile, true);

            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(String.format("%d, %.3f, %d, %d\n", params[0][0], params[0][1]/1000.0, params[0][2], params[0][3]));
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}