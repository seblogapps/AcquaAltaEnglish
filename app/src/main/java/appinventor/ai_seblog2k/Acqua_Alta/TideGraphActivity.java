package appinventor.ai_seblog2k.Acqua_Alta;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class TideGraphActivity extends AppCompatActivity {
    private static final String tideGraphURL = "http://comune.venezia.it/flex/AppData/Local/bollettino_grafico.jpg";
    private PhotoView tideImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tide_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        AdView mAdView = (AdView) findViewById(R.id.adViewTideGraph);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("11EE834DE9176B621F70C33C75B7E126")
                .build();
        mAdView.loadAd(adRequest);

        // Create a PhotoView object to store the tideGraph (PhotoView library used to implement pinch-to-zoom)
        tideImageView = (PhotoView) findViewById(R.id.tideGraph);
        // Use Picasso library to load the image
        Picasso.with(this)
                .load(tideGraphURL)
                .placeholder(R.drawable.tidegraphplaceholder)
                .error(R.drawable.tidegrapherrorplaceholder)
                .into(tideImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTideGraph);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasInternetConnection(getApplicationContext())) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.content_tide_graph), R.string.snackbar_InternetConnectionNeeded, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    snackbar.show();
                } else {
                    Picasso.with(TideGraphActivity.this).invalidate(tideGraphURL);
                    Picasso.with(TideGraphActivity.this)
                            .load(tideGraphURL)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .placeholder(R.drawable.tidegraphplaceholder)
                            .error(R.drawable.tidegrapherrorplaceholder)
                            .into(tideImageView);
                }
            }
        });
    }

}
