package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by stognacci on 10/08/2016.
 */

enum DownloadStatus {
    IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK
}

public class GetRawData {
    private static final String TAG = GetRawData.class.getSimpleName();
    private String mRawURL;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetRawData(String rawURL) {
        this.mRawURL = rawURL;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset() {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawURL = null;
        this.mData = null;
    }

    public String getData() {
        return mData;
    }

    public DownloadStatus getDownloadStatus() {
        return mDownloadStatus;
    }

    public void setRawURL(String rawURL) {
        mRawURL = rawURL;
    }

    public void execute() {
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawURL);

    }

    public class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String webData) {
            mData = webData;
            Log.d(TAG, "onPostExecute: Data returned was: " + mData);
            if (mData == null) {
                if (mRawURL == null) {
                    mDownloadStatus = DownloadStatus.NOT_INITIALIZED;
                } else {
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                // data successfully downloaded
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bReader = null;

            if (params == null) {
                return null;
            }

            URL url;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                bReader = new BufferedReader(new InputStreamReader(inputStream));
                String readLine;
                while ((readLine = bReader.readLine()) != null) {
                    stringBuffer.append(readLine + "\n");
                }
                return stringBuffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: MalformedURLException", e);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: IOException", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bReader != null) {
                    try {
                        bReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "doInBackground: Error closing stream", e);
                    }
                }
            }
        }
    }
}
