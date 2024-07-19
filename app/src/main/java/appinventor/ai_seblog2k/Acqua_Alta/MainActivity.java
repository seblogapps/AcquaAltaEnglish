package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentRequestParameters;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.UserMessagingPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    protected static final String APPS_DATA = "HighTideAppData";
    protected static final String TIDEJSONDATA_TAG = "SAVED_DATA";
    private List<Tide> mTideList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TideRecyclerViewAdapter mTideRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;

    final ProcessTideData processTideData = new ProcessTideData();

    private ProcessTideSavedData processTideSavedData;

    private TextView tideRecapDescription;
    private ImageView tideRecapIconLeft;
    private ImageView tideRecapIconRight;
    private RelativeLayout tideTitleHeaderLayout;
    private RelativeLayout tideDescriptionLayout;
    private RelativeLayout tideTableHeaderLayout;

    private TextView tideForecastDate;

    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;
    private ConsentInformation consentInformation;
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    String fakeWebData =
            "[{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-19 18:25:00\",\"TIPO_ESTREMALE\":\"min\",\"VALORE\":\"-100\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-20 00:10:00\",\"TIPO_ESTREMALE\":\"max\",\"VALORE\":\"143\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-20 06:35:00\",\"TIPO_ESTREMALE\":\"min\",\"VALORE\":\"-30\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-20 13:05:00\",\"TIPO_ESTREMALE\":\"max\",\"VALORE\":\"75\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-20 19:00:00\",\"TIPO_ESTREMALE\":\"min\",\"VALORE\":\"0\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-21 00:35:00\",\"TIPO_ESTREMALE\":\"max\",\"VALORE\":\"65\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-21 07:05:00\",\"TIPO_ESTREMALE\":\"min\",\"VALORE\":\"-20\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-21 13:35:00\",\"TIPO_ESTREMALE\":\"max\",\"VALORE\":\"75\"},{\"DATA_PREVISIONE\":\"2016-08-19 13:30:00\",\"DATA_ESTREMALE\":\"2016-08-21 19:40:00\",\"TIPO_ESTREMALE\":\"min\",\"VALORE\":\"0\"}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_toolbarname);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // AdMob banner
        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
        testDevices.add("11EE834DE9176B621F70C33C75B7E126");

        RequestConfiguration requestConfiguration
                = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        MobileAds.initialize(this);
//        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("11EE834DE9176B621F70C33C75B7E126")
                .build();
        mAdView.loadAd(adRequest);

        // FireBase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);

        // Ad-Mob Consent form
        // Create a ConsentRequestParameters object.
        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            this,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                // Consent has been gathered.
                                if (consentInformation.canRequestAds()) {
                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });
        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk();
        }

        // Find all views in the main activity
        tideRecapDescription = (TextView) findViewById(R.id.tideRecapDescription);
        tideForecastDate = (TextView) findViewById(R.id.tideForecastDate);
        tideRecapIconLeft = (ImageView) findViewById(R.id.tideRecapIconLeft);
        tideRecapIconRight = (ImageView) findViewById(R.id.tideRecapIconRight);
        tideTitleHeaderLayout = (RelativeLayout) findViewById(R.id.tideTitleHeader);
        tideDescriptionLayout = (RelativeLayout) findViewById(R.id.tideDescriptionLayout);
        tideTableHeaderLayout = (RelativeLayout) findViewById(R.id.tideTableHeaders);

        // Set up the RecyclerView object
        mRecyclerView = (RecyclerView) findViewById(R.id.tideRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        // Load and process the Tide Data
        updateTideData();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTideData();
            }
        });

        Context context = getApplicationContext();
        swipeContainer.setColorSchemeColors((ContextCompat.getColor(context, android.R.color.holo_blue_bright)),
                (ContextCompat.getColor(context, android.R.color.holo_green_light)),
                (ContextCompat.getColor(context, android.R.color.holo_orange_light)),
                (ContextCompat.getColor(context, android.R.color.holo_red_light)));

        // Used only during debug development
//        GetRawData mGetRawData = new GetRawData("http://dati.venezia.it/sites/default/files/dataset/opendata/previsione.json");
//        mGetRawData.execute();
//
//        GetTideJsonData mGetTideJsonData = new GetTideJsonData();
//        mGetTideJsonData.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTideData();
            }
        });
    }

    private void initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                    runOnUiThread(
                            () -> {
                                // TODO: Request an ad.
                                // InterstitialAd.load(...);
                            });
                })
                .start();

    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tideGraph) {
            // FireBase Analytics SELECT_CONTENT logging
            Bundle bundle = new Bundle();
            String firebase_id = "tideGraph";
            String firebase_name = "Selected Tide Graph Activity";
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebase_id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, firebase_name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TideGraph");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            Intent intent = new Intent(this, TideGraphActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_tideMonthGraph) {
            // FireBase Analytics SELECT_CONTENT logging
            Bundle bundle = new Bundle();
            String firebase_id = "tideMonthGraph";
            String firebase_name = "Selected Tide Month Graph Activity";
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebase_id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, firebase_name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "TideMonthGraph");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            Intent intent = new Intent(this, TideMonthGraphActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_webCams) {
            // FireBase Analytics SELECT_CONTENT logging
            Bundle bundle = new Bundle();
            String firebase_id = "webCams";
            String firebase_name = "Selected WebCam Activity";
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebase_id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, firebase_name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "WebCams");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            Intent intent = new Intent(this, WebCamActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_info) {
            // FireBase Analytics SELECT_CONTENT logging
            Bundle bundle = new Bundle();
            String firebase_id = "info";
            String firebase_name = "Selected Info Dialog";
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, firebase_id);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, firebase_name);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Info");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            InfoClassDialogFragment mInfoClassDialogFragment = new InfoClassDialogFragment();
            mInfoClassDialogFragment.show(getSupportFragmentManager(), "Dialog");
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTideData() {
        // No internet connection
        if (!Utils.hasInternetConnection(getApplicationContext())) {
            // If no internet connection, check if we have previously saved tide data in sharedpreferences
            SharedPreferences mSharedPreferences = getSharedPreferences(APPS_DATA, MODE_PRIVATE);
            String jsonSavedData = mSharedPreferences.getString(TIDEJSONDATA_TAG, null);
            Log.d(TAG, "updateTideData: fromSharedPreferences " + jsonSavedData);
            if (jsonSavedData != null) { // Some previous tide data stored on shared preferences
                Snackbar snackbar = Snackbar.make(findViewById(R.id.content_main), R.string.snackbar_NoInternetDatafromCache, Snackbar.LENGTH_LONG);
                snackbar.setAction("Action", null);
                snackbar.show();
                processTideSavedData = new ProcessTideSavedData(jsonSavedData);
                processTideSavedData.execute();

            } else {    // No previous tide data stored on shared preferences
                Snackbar snackbar = Snackbar.make(findViewById(R.id.content_main), R.string.snackbar_InternetConnectionNeeded, Snackbar.LENGTH_LONG);
                snackbar.setAction("Action", null);
                snackbar.show();
            }
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        } else { // Internet connection available, download fresh data
            if (mTideRecyclerViewAdapter != null) {
                mTideRecyclerViewAdapter.clear();
            }
            showTideTable(false);
            processTideData.execute();
        }
    }

    private void showTideTable(boolean show) {
        if (!show) {
            tideTitleHeaderLayout.setVisibility(View.GONE);
            tideTableHeaderLayout.setVisibility(View.GONE);
            tideDescriptionLayout.setVisibility(View.GONE);
        } else {
            tideTitleHeaderLayout.setVisibility(View.VISIBLE);
            tideTableHeaderLayout.setVisibility(View.VISIBLE);
            tideDescriptionLayout.setVisibility(View.VISIBLE);
        }
    }

    // Class to process the JSON data received from the network
    public class ProcessTideData extends GetTideJsonData {

        public ProcessTideData() {
            super();
        }

        @Override
        public void execute() {
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        private class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                // Only if no errors on downloading data (it can happen if network is up but data is not downloaded)
                if (webData != null) {
                    //Save fresh webData to Shared Preferences
                    SharedPreferences mSharedPreferences = getSharedPreferences(APPS_DATA, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(TIDEJSONDATA_TAG, webData);
                    editor.apply();
                    // Load tide values into mTideList ArrayList
                    mTideList = getTides();
                    // Read from the tide forecast table the maximum value of the extremal tide level
                    int extremalMaxValue = getExtremalMaxValue();
                    Log.d(TAG, "onPostExecute: maxTideValue " + extremalMaxValue);
                    // From the max value of tide level, extract the string with the description of the tide level
                    // Read from the tide forecast the date and time for the next max tide event
                    String extremalMaxValueDateTime = getExtremalMaxValueDateTime();
                    Log.d(TAG, "onPostExecute: getExtremalMaxValueDateTime " + extremalMaxValueDateTime);
                    String extremalMaxValueDescription = getExtremalMaxValueDescription(extremalMaxValue, extremalMaxValueDateTime);
                    tideRecapDescription.setText(extremalMaxValueDescription);
                    Log.d(TAG, "onPostExecute: extremalValueDescription " + extremalMaxValueDescription);
                    // Depending on the max extremal value, set the tide recap icon, both left and right
                    int tideRecapIconImageResource = getTideRecapIcon(extremalMaxValue);
                    tideRecapIconLeft.setImageResource(tideRecapIconImageResource);
                    tideRecapIconRight.setImageResource(tideRecapIconImageResource);
                    // Depending on the max extremal value, set the background for the description recap line
                    tideDescriptionLayout.setBackgroundResource((getTideRecapBackground(extremalMaxValue)));
                    // Read from the tide forecast the last update date of the forecast and load it to textView
                    String tideForecastDateTime = getForecastDateTime();
                    tideForecastDate.setText(new StringBuilder().append(getString(R.string.tideForecast_lastUpdateText))
                            .append(Utils.formatJSONDate(tideForecastDateTime))
                            .append(getString(R.string.tideForecast_lastUpdateText_at))
                            .append(Utils.formatJSONTime(tideForecastDateTime))
                            .toString());
                    // Set the full table (including recap of the tide levels) visible
                    showTideTable(true);
                    // Load tide table data and set to the adapter
                    mTideRecyclerViewAdapter = new TideRecyclerViewAdapter(MainActivity.this, mTideList);
                    mRecyclerView.setAdapter(mTideRecyclerViewAdapter);
                    swipeContainer.setRefreshing(false);
                } else {
                    final Snackbar snackbar = Snackbar.make(findViewById(R.id.content_main), R.string.snackbar_ErrorDownloadingData, Snackbar.LENGTH_LONG);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        }
    }

    // Class to process the tide data that is stored on Shared Preferences (in case of no internet connection)
    public class ProcessTideSavedData extends ProcessTideData {

        private String savedData;

        public ProcessTideSavedData(String savedData) {
            super();
            this.savedData = savedData;
        }

        @Override
        public void execute() {
            GetSavedTideData mGetSavedTideData = new GetSavedTideData();
            mGetSavedTideData.processResult(savedData);
            mTideList = mGetSavedTideData.getTides();
            // Read from the tide forecast table the maximum value of the extremal tide level
            int extremalMaxValue = mGetSavedTideData.getExtremalMaxValue();
            Log.d(TAG, "onPostExecute: saved maxTideValue " + extremalMaxValue);
            // From the max value of tide level, extract the string with the description of the tide level
            // Read from the tide forecast the date and time for the next max tide event
            String extremalMaxValueDateTime = mGetSavedTideData.getExtremalMaxValueDateTime();
            Log.d(TAG, "onPostExecute: saved getExtremalMaxValueDateTime " + extremalMaxValueDateTime);
            String extremalMaxValueDescription = getExtremalMaxValueDescription(extremalMaxValue, extremalMaxValueDateTime);
            tideRecapDescription.setText(extremalMaxValueDescription);
            Log.d(TAG, "onPostExecute: saved extremalValueDescription " + extremalMaxValueDescription);
            // Depending on the max extremal value, set the tide recap icon, both left and right
            int tideRecapIconImageResource = getTideRecapIcon(extremalMaxValue);
            tideRecapIconLeft.setImageResource(tideRecapIconImageResource);
            tideRecapIconRight.setImageResource(tideRecapIconImageResource);
            // Depending on the max extremal value, set the background for the description recap line
            tideDescriptionLayout.setBackgroundResource((getTideRecapBackground(extremalMaxValue)));
            // Read from the tide forecast the last update date of the forecast and load it to textView
            String tideForecastDateTime = mGetSavedTideData.getForecastDateTime();
            tideForecastDate.setText(new StringBuilder().append(getString(R.string.tideForecast_lastUpdateText))
                    .append(Utils.formatJSONDate(tideForecastDateTime))
                    .append(getString(R.string.tideForecast_lastUpdateText_at))
                    .append(Utils.formatJSONTime(tideForecastDateTime))
                    .toString());
            // Set the full table (including recap of the tide levels) visible
            showTideTable(true);
            // Load tide table data and set to the adapter
            mTideRecyclerViewAdapter = new TideRecyclerViewAdapter(MainActivity.this, mTideList);
            mRecyclerView.setAdapter(mTideRecyclerViewAdapter);
        }
    }

    protected String getExtremalMaxValueDescription(int extremalValue, String extremalDateTime) {
        String extremalDate = Utils.formatJSONDate(extremalDateTime);
        String extremalTime = Utils.formatJSONTime(extremalDateTime);
        final StringBuilder sb = new StringBuilder("" + extremalValue);
        sb.append(getString(R.string.extremalValueDesc_Units));
        sb.append(getString(R.string.extremalValueDesc_onDate));
        sb.append(extremalDate);
        sb.append(getString(R.string.extremalValueDesc_atTime));
        sb.append(extremalTime);
        sb.append("\n");
        if (extremalValue >= 140) {
            sb.append(getString(R.string.extremalValueDesc_ExtraHigh));
        } else if (extremalValue >= 100) {
            sb.append(getString(R.string.extremalValueDesc_VeryHigh));
        } else if (extremalValue >= 80) {
            sb.append(getString(R.string.extremalValueDesc_High));
        } else if (extremalValue >= -50) {
            sb.append(getString(R.string.extremalValueDesc_Normal));
        } else if (extremalValue >= -90) {
            sb.append(getString(R.string.extremalValueDesc_Low));
        } else if (extremalValue < -90) {
            sb.append(getString(R.string.extremalValueDesc_ExtraLow));
        } else
            sb.append(getString(R.string.extremalValueDesc_Unknown));
        return sb.toString();
    }

    protected int getTideRecapBackground(int extremalValue) {
        if (extremalValue >= 140) {
            return R.drawable.gradientextrahigh;
        } else if (extremalValue >= 100) {
            return R.drawable.gradientveryhigh;
        } else if (extremalValue >= 80) {
            return R.drawable.gradienthigh;
        } else if (extremalValue >= -50) {
            return R.drawable.gradientnormal;
        } else if (extremalValue >= -90) {
            return R.drawable.gradientlow;
        } else if (extremalValue < -90) {
            return R.drawable.gradientextralow;
        } else
            return R.drawable.gradientnormal;
    }

    protected int getTideRecapIcon(int extremalValue) {
        if (extremalValue >= 140) {
            return R.drawable.extremalextrahigh;
        } else if (extremalValue >= 100) {
            return R.drawable.extremalveryhigh;
        } else if (extremalValue >= 80) {
            return R.drawable.extremalhigh;
        } else if (extremalValue >= -50) {
            return R.drawable.extremalnormal;
        } else if (extremalValue >= -90) {
            return R.drawable.extremallow;
        } else if (extremalValue < -90) {
            return R.drawable.extremalextralow;
        } else
            return R.drawable.extremalunknown;
    }
}

