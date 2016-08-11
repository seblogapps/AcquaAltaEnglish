package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;

public class TideGraphActivity extends AppCompatActivity {
private static final String tideGraphURL = "http://archive.comune.venezia.it/flex/AppData/Local/bollettino_grafico.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tide_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create a PhotoView object to store the tideGraph (PhotoView library used to implement pinch-to-zoom)
        PhotoView tideImageView = (PhotoView) findViewById(R.id.tideGraph);
        // Use Picasso library to load the image
        Picasso.with(this)
                .load(tideGraphURL)
                .placeholder(R.drawable.tidegraphplaceholder)
                .error(R.drawable.tidegrapherrorplaceholder)
                .into(tideImageView);
    }

}
