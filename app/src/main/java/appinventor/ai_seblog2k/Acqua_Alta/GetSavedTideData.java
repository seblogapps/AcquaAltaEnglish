package appinventor.ai_seblog2k.Acqua_Alta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stognacci on 10/08/2016.
 */

public class GetSavedTideData extends GetTideJsonData {

    private static final String TAG = GetSavedTideData.class.getSimpleName();
    private List<Tide> mTides;

    public GetSavedTideData() {
         mTides = new ArrayList<>();
    }

    public List<Tide> getTides() {
        return mTides;
    }

    // Return the last forecast date from JSON data, it's stored with any record, so simply take it from record 0
    public String getForecastDateTime() {
        return mTides.get(0).getForecastDate();
    }

    // Return the index position of the maximum tide level in the current forecast
    public int getExtremalMaxValue() {
        Tide tideObject = Collections.max(mTides);
        return Integer.parseInt(tideObject.getExtremalValue());
    }

    public int getExtremalMaxValueIndex() {
        return mTides.indexOf(Collections.max(mTides));
    }

    public String getExtremalMaxValueDateTime() {
        return mTides.get(getExtremalMaxValueIndex()).getExtremalDate();
    }

    public void processResult(String sharedPreferencesJsonData) {

        final String FORECAST_DATE = "DATA_PREVISIONE";
        final String EXTREMAL_DATE = "DATA_ESTREMALE";
        final String EXTREMAL_TYPE = "TIPO_ESTREMALE";
        final String EXTREMAL_VALUE = "VALORE";

        try {
            JSONArray itemsArray = new JSONArray(sharedPreferencesJsonData);
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
