package appinventor.ai_seblog2k.Acqua_Alta;

/**
 * Created by stognacci on 12/08/2016.
 */

public class WebCam {
    private String mUrl;
    private String mDescription;

    public WebCam(String url, String description) {
        this.mUrl = url;
        this.mDescription = description;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("WebCam{");
        sb.append("mUrl='").append(mUrl).append('\'');
        sb.append(", mDescription='").append(mDescription).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
