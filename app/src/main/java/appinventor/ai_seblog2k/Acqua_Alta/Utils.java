package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by stognacci on 18/08/2016.
 */

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    private static final String JSONDateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static final String defaultDateTimePattern = "dd/MM/yyyy HH:mm";
    private static final String defaultDatePattern = "dd/MM/yyyy";
    private static final String defaultTimePattern = "HH:mm";


    // Take JSONDateTime string and return a string with date and time formatted with user locale
    static String formatJSONDateTime(String jsonDateTime) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat inFormatter = new SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                String formattedDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(dateObj);
                Log.d(TAG, "formatJSONDateTime: Formatted date (API>=24): " + formattedDateTime);
                return formattedDateTime;
            } else {
                java.text.SimpleDateFormat inFormatter = new java.text.SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                java.text.SimpleDateFormat outFormatter = new java.text.SimpleDateFormat(defaultDateTimePattern);
                String formattedDateTime = outFormatter.format(dateObj);
                Log.d(TAG, "formatJSONDateTime: Formatted date (API<24): " + formattedDateTime);
                return formattedDateTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "formatJSONDateTime: Error formatting date from JSON");
            return jsonDateTime;
        }
    }
    // Take JSONDateTime string and return a string with date formatted with user locale
    static String formatJSONDate(String jsonDateTime) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat inFormatter = new SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                String formattedDateTime = DateFormat.getDateInstance(DateFormat.SHORT).format(dateObj);
                Log.d(TAG, "formatJSONDate: Formatted date (API>=24): " + formattedDateTime);
                return formattedDateTime;
            } else {
                java.text.SimpleDateFormat inFormatter = new java.text.SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                java.text.SimpleDateFormat outFormatter = new java.text.SimpleDateFormat(defaultDatePattern);
                String formattedDateTime = outFormatter.format(dateObj);
                Log.d(TAG, "formatJSONDate: Formatted date (API<24): " + formattedDateTime);
                return formattedDateTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "formatJSONDate: Error formatting date from JSON");
            return jsonDateTime;
        }
    }

    // Take JSONDateTime string and return a string with time formatted with user locale
    static String formatJSONTime(String jsonDateTime) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat inFormatter = new SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                String formattedDateTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(dateObj);
                Log.d(TAG, "formatJSONTime: Formatted date (API>=24): " + formattedDateTime);
                return formattedDateTime;
            } else {
                java.text.SimpleDateFormat inFormatter = new java.text.SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(jsonDateTime);
                java.text.SimpleDateFormat outFormatter = new java.text.SimpleDateFormat(defaultTimePattern);
                String formattedDateTime = outFormatter.format(dateObj);
                Log.d(TAG, "formatJSONTime: Formatted date (API<24): " + formattedDateTime);
                return formattedDateTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "formatJSONTime: Error formatting date from JSON");
            return jsonDateTime;
        }
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
