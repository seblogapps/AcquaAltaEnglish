package appinventor.ai_seblog2k.Acqua_Alta;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import uk.co.senab.photoview.PhotoView;

public class TideMonthGraphActivity extends AppCompatActivity {
    //private static final String tideGraphURL = "https://www.comune.venezia.it/sites/default/files/publicCPSM/png/bollettino_grafico.jpg"; //Updated to https on 25/08/2020
    private static final String tideMonthGraphURL = "https://www.comune.venezia.it/sites/default/files/publicCPSM2/grafici/astronomica/mensile/astronomica-mensile-corrente.png";
    private PhotoView tideImageView;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tide_month_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //AdView mAdView = (AdView) findViewById(R.id.adViewTideGraph);
        mAdView = findViewById(R.id.adViewTideMonthGraph);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("11EE834DE9176B621F70C33C75B7E126")
                .build();
        mAdView.loadAd(adRequest);

        // Create a PhotoView object to store the tideGraph (PhotoView library used to implement pinch-to-zoom)
        tideImageView = (PhotoView) findViewById(R.id.tideMonthGraph);
        // Use Glide library to load the image
        Glide.with(this)
                .load(tideMonthGraphURL)
                .apply(new RequestOptions()
                        .override(2048, 1600)
                        .placeholder(R.drawable.tidegraphplaceholder)
                        .error(R.drawable.tidegrapherrorplaceholder))
                .into(tideImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabTideGraph);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.hasInternetConnection(getApplicationContext())) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.content_tide_month_graph), R.string.snackbar_InternetConnectionNeeded, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null);
                    snackbar.show();
                } else {
                    Glide.with(TideMonthGraphActivity.this)
                            .load(tideMonthGraphURL)
                            .apply(new RequestOptions()
                                    .override(2048, 1600)
                                    .placeholder(R.drawable.tidegraphplaceholder)
                                    .error(R.drawable.tidegrapherrorplaceholder)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .signature(new ObjectKey(System.currentTimeMillis())))
                            .into(tideImageView);
                }
            }
        });
    }
}
