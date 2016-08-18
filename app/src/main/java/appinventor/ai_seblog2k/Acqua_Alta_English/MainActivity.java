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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Tide> mTideList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TideRecyclerViewAdapter mTideRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;

    final ProcessTideData processTideData = new ProcessTideData();
    private String extremalMaxValueDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Log.d(TAG, "onPostExecute: mTideListSize " + mTideList.size());
                Log.d(TAG, "onPostExecute: forecastDate " + Utils.formatJSONDate(getForecastDateTime()));
                int extremalMaxValue = getExtremalMaxValue();
                Log.d(TAG, "onPostExecute: maxTideValue " + extremalMaxValue);
                Log.d(TAG, "onPostExecute: maxTideValueIndex " + getExtremalMaxValueIndex());
                extremalMaxValueDescription = getExtremalMaxValueDescription(extremalMaxValue);
                Log.d(TAG, "onPostExecute: extremalValueDescription " + extremalMaxValueDescription);
                mTideRecyclerViewAdapter = new TideRecyclerViewAdapter(MainActivity.this, mTideList);
                mRecyclerView.setAdapter(mTideRecyclerViewAdapter);
                swipeContainer.setRefreshing(false);
            }

            private String getExtremalMaxValueDescription(int extremalValue) {
                if (extremalValue >= 140) {
                    return "Alta marea eccezionale oltre 140cm";
                } else if (extremalValue >= 100) {
                    return "Marea molto sostenuta +110 ÷ +139";
                } else if (extremalValue >= 80) {
                    return "Marea sostenuta +80 ÷ +109";
                } else if (extremalValue >= 50) {
                    return "Marea normale -50 ÷ +79";
                } else if (extremalValue >= -90) {
                    return "Marea sotto i valori normali -90 ÷ -51";
                } else if (extremalValue < 90) {
                    return "Bassa marea eccezionale <-90";
                } else
                    return null;
            }
        }
    }
}

