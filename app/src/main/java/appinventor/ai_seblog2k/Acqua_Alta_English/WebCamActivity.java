package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webCamOnClickListener = new MyOnClickListener();

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
