package appinventor.ai_seblog2k.Acqua_Alta_English;

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


    static String formatDate(String rawDate) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat inFormatter = new SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(rawDate);
                String formattedDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(dateObj);
                Log.d(TAG, "formatDate: Formatted date (API>=24): " + formattedDateTime);
                return formattedDateTime;
            } else {
                java.text.SimpleDateFormat inFormatter = new java.text.SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(rawDate);
                java.text.SimpleDateFormat outFormatter = new java.text.SimpleDateFormat(defaultDateTimePattern);
                String formattedDateTime = outFormatter.format(dateObj);
                Log.d(TAG, "formatDate: Formatted date (API<24): " + formattedDateTime);
                return formattedDateTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "formatDate: Error formatting date from JSON");
            return rawDate;
        }
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
