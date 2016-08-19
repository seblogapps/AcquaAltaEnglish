package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Tide> mTideList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TideRecyclerViewAdapter mTideRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;

    final ProcessTideData processTideData = new ProcessTideData();

    private TextView tideRecapDescription;
    private ImageView tideRecapIconLeft;
    private ImageView tideRecapIconRight;
    private LinearLayout tideTableRecapLayout;
    private LinearLayout tideDescriptionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find all views in the main activity
        tideRecapDescription = (TextView) findViewById(R.id.tideRecapDescription);
        tideRecapIconLeft = (ImageView) findViewById(R.id.tideRecapIconLeft);
        tideRecapIconRight = (ImageView) findViewById(R.id.tideRecapIconRight);
        tideTableRecapLayout = (LinearLayout) findViewById(R.id.tideTableRecapHeader);
        tideDescriptionLayout = (LinearLayout) findViewById(R.id.tideDescriptionLayout);

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

        GetRawData mGetRawData = new GetRawData("http://dati.venezia.it/sites/default/files/dataset/opendata/previsione.json");
        mGetRawData.execute();

        GetTideJsonData mGetTideJsonData = new GetTideJsonData();
        mGetTideJsonData.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTideData();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_tideGraph) {
            Intent intent = new Intent(this, TideGraphActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_webCams) {
            Intent intent = new Intent(this, WebCamActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateTideData() {
        if (!Utils.hasInternetConnection(getApplicationContext())) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.content_main), "Internet connection needed", Snackbar.LENGTH_LONG);
            snackbar.setAction("Action", null);
            snackbar.show();
            if (swipeContainer != null)
                swipeContainer.setRefreshing(false);
        } else {
            if (mTideRecyclerViewAdapter != null) {
                mTideRecyclerViewAdapter.clear();
            }
            tideTableRecapLayout.setVisibility(View.GONE);
            processTideData.execute();
        }
    }

    public class ProcessTideData extends GetTideJsonData {

        public ProcessTideData() {
            super();
        }

        @Override
        public void execute() {
            //super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        private class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String webData) {
                super.onPostExecute(webData);
                mTideList = getTides();
                // Read from the tide forecast table forecast Date and Time
                String forecastUpdateDateTime = Utils.formatJSONDate(getForecastDateTime());
                Log.d(TAG, "onPostExecute: forecastDate " + forecastUpdateDateTime);
                // Read from the tide forecast table the maximum value of the extremal tide level
                int extremalMaxValue = getExtremalMaxValue();
                Log.d(TAG, "onPostExecute: maxTideValue " + extremalMaxValue);
                // From the max value of tide level, extract the string with the description of the tide level
                // Read from the tide forecast the date and time for the next max tide event
                String extremalMaxValueDateTime = Utils.formatJSONDate(getExtremalMaxValueDateTime());
                String extremalMaxValueDescription = getExtremalMaxValueDescription(extremalMaxValue, extremalMaxValueDateTime);
                Log.d(TAG, "onPostExecute: extremalValueDescription " + extremalMaxValueDescription);
                tideRecapDescription.setText(extremalMaxValueDescription);
                Log.d(TAG, "onPostExecute: maxTideValueIndex " + getExtremalMaxValueIndex());
                Log.d(TAG, "onPostExecute: getExtremalMaxValueDateTime " + extremalMaxValueDateTime);
                // Depending on the max extremal value, set the tide recap icon, both left and right
                int tideRecapIconImageResource = getTideRecapIcon(extremalMaxValue);
                tideRecapIconLeft.setImageResource(tideRecapIconImageResource);
                tideRecapIconRight.setImageResource(tideRecapIconImageResource);
                // Depending on the max extremal value, set the background for the description recap line
                tideDescriptionLayout.setBackgroundResource((getTideRecapBackground(extremalMaxValue)));
                // Set the full table with recap of the tide levels visible
                tideTableRecapLayout.setVisibility(View.VISIBLE);
                // Load tide table data and set to the adapter
                mTideRecyclerViewAdapter = new TideRecyclerViewAdapter(MainActivity.this, mTideList);
                mRecyclerView.setAdapter(mTideRecyclerViewAdapter);
                swipeContainer.setRefreshing(false);
            }

            private String getExtremalMaxValueDescription(int extremalValue, String extremalDateTime) {
                final StringBuilder sb = new StringBuilder("" + extremalValue);
                sb.append("cm ");
                sb.append(" il ");
                sb.append(extremalDateTime);
                sb.append("\n");
                if (extremalValue >= 140) {
                    sb.append("Alta marea eccezionale (oltre 140cm)");
                } else if (extremalValue >= 100) {
                    sb.append("Marea molto sostenuta (tra 110cm e 139cm)");
                } else if (extremalValue >= 80) {
                    sb.append("Marea sostenuta (tra 80cm e 109cm)");
                } else if (extremalValue >= -50) {
                    sb.append("Marea normale (tra -50cm e 79cm)");
                } else if (extremalValue >= -90) {
                    sb.append("Marea sotto i valori normali (tra -90cm e -51cm)");
                } else if (extremalValue < -90) {
                    sb.append("Bassa marea eccezionale (inferiore -90cm)");
                } else
                    sb.append(" dato non previsto");
                return sb.toString();
            }

            private int getTideRecapIcon(int extremalValue) {
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

            private int getTideRecapBackground(int extremalValue) {
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


        }
    }
}

