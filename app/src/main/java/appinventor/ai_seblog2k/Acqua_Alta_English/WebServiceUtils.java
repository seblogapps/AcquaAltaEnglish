package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by stognacci on 16/08/2016.
 */

public class WebServiceUtils {

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
