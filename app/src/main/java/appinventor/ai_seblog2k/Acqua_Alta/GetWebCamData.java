package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stognacci on 12/08/2016.
 */

public class GetWebCamData {

    private static final String TAG = GetWebCamData.class.getSimpleName();

    private List<WebCam> mWebCams;
    private Context mContext;
    private final String [] webCamDescription;
    private final String[] webCamUrls;

    public GetWebCamData(Context context) {
        this.mContext = context;
        webCamDescription = mContext.getResources().getStringArray(R.array.WebCam_Descriptions);
        webCamUrls = mContext.getResources().getStringArray(R.array.WebCam_URLs);
        mWebCams = new ArrayList<>();
        for (int i = 0; i < webCamUrls.length; i++) {
            WebCam webCamObject = new WebCam(webCamUrls[i], webCamDescription[i]);
            this.mWebCams.add(webCamObject);
        }
        for (WebCam webCam : mWebCams) {
            Log.d(TAG, "getWebCams: processResult: " + webCam.toString());
        }
    }

    public List<WebCam> getWebCams() {
        return mWebCams;
    }

}
