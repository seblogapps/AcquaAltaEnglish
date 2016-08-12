package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class WebCamActivity extends AppCompatActivity {

    private static final String LOG = WebCamActivity.class.getSimpleName();
    private List<WebCam> mWebCamList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WebCamRecyclerViewAdapter mWebCamRecyclerViewAdapter;
    private GetWebCamData mGetWebCamData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_cam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.webcamRecyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mGetWebCamData = new GetWebCamData();
        mWebCamRecyclerViewAdapter = new WebCamRecyclerViewAdapter(WebCamActivity.this, mGetWebCamData.getWebCams());
        mRecyclerView.setAdapter(mWebCamRecyclerViewAdapter);
    }

}
