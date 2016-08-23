package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by stognacci on 12/08/2016.
 */

public class WebCamRecyclerViewAdapter extends RecyclerView.Adapter<WebCamViewHolder> {

    private static final String TAG = WebCamRecyclerViewAdapter.class.getSimpleName();
    private List<WebCam> mWebCamList;
    private Context mContext;

    public WebCamRecyclerViewAdapter(Context context, List<WebCam> webCamList) {
        this.mContext = context;
        this.mWebCamList = webCamList;
    }

    @Override
    public WebCamViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.webcam_browse, null);
        WebCamViewHolder webCamViewHolder = new WebCamViewHolder(view);
        return webCamViewHolder;
    }

    @Override
    public void onBindViewHolder(WebCamViewHolder webCamViewHolder, int position) {
        WebCam webCamItem = mWebCamList.get(position);
        String webCamUrl = webCamItem.getUrl();
        Log.d(TAG, "onBindViewHolder: Processing: " + webCamItem.getDescription() + " position --> " + position + " url --> " + webCamUrl);
        webCamViewHolder.mWebCamDescription.setText(webCamItem.getDescription());
        // To avoid duplicate images in RecyclerView, always check that webCamUrl is not null before loading image with Picasso
        if (webCamUrl != null) {
            Picasso.with(mContext).load(webCamUrl)
                    .fit().centerCrop()
                    .error(R.drawable.webcamerrorplaceholder)
                    .placeholder(R.drawable.webcamplaceholder)
                    .into(webCamViewHolder.mWebCamThumbnail);
        } else {
            webCamViewHolder.mWebCamThumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.webcamplaceholder));
        }
    }

    @Override
    public int getItemCount() {
        return (null != mWebCamList ? mWebCamList.size() : 0);
    }

    public void clear() {
        mWebCamList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<WebCam> webCamList) {
        mWebCamList.addAll(webCamList);
        notifyDataSetChanged();
    }
}
