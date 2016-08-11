package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stognacci on 10/08/2016.
 */

public class GetTideJsonData extends GetRawData {

    private static final String TAG = GetTideJsonData.class.getSimpleName();
    private static final String VENICE_TIDE_DATA_URI = "http://dati.venezia.it/sites/default/files/dataset/opendata/previsione.json";
    private List<Tide> mTides;
    private Uri mDestinationUri;

    public GetTideJsonData() {
        super(null);
        mDestinationUri = Uri.parse(VENICE_TIDE_DATA_URI).buildUpon().build();
        mTides = new ArrayList<>();
    }

    public List<Tide> getTides() {
        return mTides;
    }

    public void execute() {
        super.setRawURL(mDestinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.d(TAG, "execute: Built URI: " + mDestinationUri.toString());
        downloadJsonData.execute(mDestinationUri.toString());
    }


    public class DownloadJsonData extends DownloadRawData {
        @Override
        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] par = { mDestinationUri.toString() };
            return super.doInBackground(par);
        }
    }

    private void processResult() {
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(TAG, "processResult: Error downloading raw data");
            return;
        }
        final String FORECAST_DATE = "DATA_PREVISIONE";
        final String EXTREMAL_DATE = "DATA_ESTREMALE";
        final String EXTREMAL_TYPE = "TIPO_ESTREMALE";
        final String EXTREMAL_VALUE = "VALORE";

        try {
            //JSONObject jsonData = new JSONObject(getData());
            //JSONArray itemsArray = jsonData.getJSONArray("");
            JSONArray itemsArray = new JSONArray(getData());
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject jsonTideObject = itemsArray.getJSONObject(i);
                String forecastDate = jsonTideObject.getString(FORECAST_DATE);
                String extremalDate = jsonTideObject.getString(EXTREMAL_DATE);
                String extremalType = jsonTideObject.getString(EXTREMAL_TYPE);
                String extremalValue = jsonTideObject.getString(EXTREMAL_VALUE);

                Tide tideObject = new Tide(forecastDate, extremalDate, extremalType, extremalValue);

                this.mTides.add(tideObject);
            }

            for (Tide tide : mTides) {
                Log.d(TAG, "processResult: " + tide.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "processResult: Error processing Json data", e);
        }
    }

}
