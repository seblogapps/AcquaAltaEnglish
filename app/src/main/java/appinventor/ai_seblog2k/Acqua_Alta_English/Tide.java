package appinventor.ai_seblog2k.Acqua_Alta_English;

/**
 * Created by stognacci on 10/08/2016.
 */

public class Tide {
    private String mForecastDate;
    private String mExtremalDate;
    private String mExtremalType;
    private String mExtremalValue;

    public Tide(String forecastDate, String extremalDate, String extremalType, String extremalValue) {
        mForecastDate = forecastDate;
        mExtremalDate = extremalDate;
        mExtremalType = extremalType;
        mExtremalValue = extremalValue;
    }

    public String getForecastDate() {
        return mForecastDate;
    }

    public void setForecastDate(String forecastDate) {
        mForecastDate = forecastDate;
    }

    public String getExtremalDate() {
        return mExtremalDate;
    }

    public void setExtremalDate(String extremalDate) {
        mExtremalDate = extremalDate;
    }

    public String getExtremalType() {
        return mExtremalType;
    }

    public void setExtremalType(String extremalType) {
        mExtremalType = extremalType;
    }

    public String getExtremalValue() {
        return mExtremalValue;
    }

    public void setExtremalValue(String extremalValue) {
        mExtremalValue = extremalValue;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Tide{");
        sb.append("mForecastDate='").append(mForecastDate).append('\'');
        sb.append(", mExtremalDate='").append(mExtremalDate).append('\'');
        sb.append(", mExtremalType='").append(mExtremalType).append('\'');
        sb.append(", mExtremalValue='").append(mExtremalValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
