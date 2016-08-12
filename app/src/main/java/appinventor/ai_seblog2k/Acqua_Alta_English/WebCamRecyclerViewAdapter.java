package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Context;
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
        Log.d(TAG, "onBindViewHolder: Processing: " + webCamItem.getDescription() + " position --> " + position);
        webCamViewHolder.mWebCamDescription.setText(webCamItem.getDescription());
        Picasso.with(mContext).load(webCamItem.getUrl())
                .error(R.drawable.webcamerrorplaceholder)
                .placeholder(R.drawable.webcamgraphplaceholder)
                .into(webCamViewHolder.mWebCamThumbnail);
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
