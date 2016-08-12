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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = MainActivity.class.getSimpleName();
    private List<Tide> mTideList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TideRecyclerViewAdapter mTideRecyclerViewAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the RecyclerView object
        mRecyclerView = (RecyclerView) findViewById(R.id.tideRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Load and process the Tide Data
        final ProcessTideData processTideData = new ProcessTideData();
        processTideData.execute();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mTideRecyclerViewAdapter != null) {
                    mTideRecyclerViewAdapter.clear();
                }
                processTideData.execute();
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                mTideRecyclerViewAdapter = new TideRecyclerViewAdapter(MainActivity.this, getTides());
                mRecyclerView.setAdapter(mTideRecyclerViewAdapter);
                swipeContainer.setRefreshing(false);
            }
        }
    }
}
