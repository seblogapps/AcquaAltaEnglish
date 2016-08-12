package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stognacci on 12/08/2016.
 */

public class GetWebCamData {

    private static final String[] webCamUrls = {    "http://93.62.201.235/maree/WEBCAM/smarcoweb1.jpg" ,
                                                    "http://93.62.201.235/maree/WEBCAM/smarcoweb2.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/smarcoweb3.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/rialtoweb1.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/rialtoweb2.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/muranoweb1.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/muranoweb2.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/muranoweb3.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/saluteweb1.jpg",
                                                    "http://93.62.201.235/maree/WEBCAM/saluteweb2.jpg"};
    private static final String[] webCamDescription = { "S.Marco \nTorre dell'Orologio 1" ,
                                                        "S.Marco \nTorre dell'Orologio 2" ,
                                                        "S.Marco \nTorre dell'Orologio 3" ,
                                                        "Rialto \nPalazzo Cavalli 1" ,
                                                        "Rialto \nPalazzo Cavalli 2" ,
                                                        "Isola di Murano \nFaro 1" ,
                                                        "Isola di Murano \nFaro 2" ,
                                                        "Isola di Murano \nFaro 3" ,
                                                        "Bacino S.Marco \nPunta Salute 1" ,
                                                        "Bacino S.Marco \nPunta Salute 2"};

    private static final String TAG = GetWebCamData.class.getSimpleName();
    private List<WebCam> mWebCams;

    public GetWebCamData() {
        mWebCams = new ArrayList<>();
    }

    public List<WebCam> getWebCams() {
        for (int i = 0; i < webCamUrls.length; i++) {
            WebCam webCamObject = new WebCam(webCamUrls[i], webCamDescription[i]);
            this.mWebCams.add(webCamObject);
        }
        for (WebCam webCam : mWebCams) {
            Log.d(TAG, "getWebCams: processResult: " + webCam.toString());
        }
        return mWebCams;
    }

}
