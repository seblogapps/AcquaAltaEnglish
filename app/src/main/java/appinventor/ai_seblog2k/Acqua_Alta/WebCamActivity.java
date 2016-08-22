package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import java.util.ArrayList;
import java.util.List;

public class WebCamActivity extends AppCompatActivity {

    private static final String TAG = WebCamActivity.class.getSimpleName();
    private List<WebCam> mWebCamList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WebCamRecyclerViewAdapter mWebCamRecyclerViewAdapter;

    private GetWebCamData mGetWebCamData;

    public static View.OnClickListener webCamOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        webCamOnClickListener = new MyOnClickListener();

        updateWebCamRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWebCam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasInternetConnection(getApplicationContext())) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.content_web_cam), "Internet connection needed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    snackbar.show();
                } else {
                    PicassoTools.clearCache(Picasso.with(WebCamActivity.this));
                    mWebCamRecyclerViewAdapter.clear();
                    updateWebCamRecyclerView();
                }
            }
        });
    }

    private void updateWebCamRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.webcamRecyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGetWebCamData = new GetWebCamData();
        mWebCamRecyclerViewAdapter = new WebCamRecyclerViewAdapter(WebCamActivity.this, mGetWebCamData.getWebCams());
        mRecyclerView.setAdapter(mWebCamRecyclerViewAdapter);
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            WebCam selectedWebCam = getSelectedWebCam(view);
            Log.d(TAG, "onClick: selected WebCam " + selectedWebCam.toString());
            showSelectedWebCam(selectedWebCam);
        }
    }

    private WebCam getSelectedWebCam(View view) {
        int selectedItemPosition = mRecyclerView.getChildAdapterPosition(view);
        Log.d(TAG, "getSelectedWebCam: Position: " + selectedItemPosition);
        return mGetWebCamData.getWebCams().get(selectedItemPosition);
    }

    private void showSelectedWebCam(WebCam selectedWebCam) {
        Intent fullScreenIntent = new Intent(this, FullscreenWebCam.class);
        fullScreenIntent.putExtra("webCamURL", selectedWebCam.getUrl());
        fullScreenIntent.putExtra("webCamDescription", selectedWebCam.getDescription());
        startActivity(fullScreenIntent);
    }
}
