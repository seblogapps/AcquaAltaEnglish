package appinventor.ai_seblog2k.Acqua_Alta_English;

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
        Log.d(TAG, "onBindViewHolder: Processing: " + webCamItem.getDescription() + " position --> " + position + " url --> " + webCamItem.getUrl());
        webCamViewHolder.mWebCamDescription.setText(webCamItem.getDescription());
        String webCamUrl = webCamItem.getUrl();
        if (webCamUrl != null) {
            //Picasso.with(mContext).invalidate(webCamItem.getUrl());
            Picasso.with(mContext).load(webCamItem.getUrl())
                    // Try to avoid duplicate images -- START
                    //.networkPolicy(NetworkPolicy.NO_CACHE)
                    //.memoryPolicy(MemoryPolicy.NO_CACHE)
                    // Try to avoid duplicate images -- END
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
