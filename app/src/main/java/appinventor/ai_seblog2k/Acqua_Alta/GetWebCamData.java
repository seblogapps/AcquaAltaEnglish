package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stognacci on 12/08/2016.
 */

public class GetWebCamData {

//    private static final String[] webCamUrls = {"http://93.62.201.235/maree/WEBCAM/smarcoweb1.jpg",
//            "http://93.62.201.235/maree/WEBCAM/smarcoweb2.jpg",
//            "http://93.62.201.235/maree/WEBCAM/smarcoweb3.jpg",
//            "http://93.62.201.235/maree/WEBCAM/rialtoweb1.jpg",
//            "http://93.62.201.235/maree/WEBCAM/rialtoweb2.jpg",
//            "http://93.62.201.235/maree/WEBCAM/muranoweb1.jpg",
//            "http://93.62.201.235/maree/WEBCAM/muranoweb2.jpg",
//            "http://93.62.201.235/maree/WEBCAM/muranoweb3.jpg",
//            "http://93.62.201.235/maree/WEBCAM/saluteweb1.jpg",
//            "http://93.62.201.235/maree/WEBCAM/saluteweb2.jpg"};

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
