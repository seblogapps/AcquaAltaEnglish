package appinventor.ai_seblog2k.Acqua_Alta;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by stognacci on 12/08/2016.
 */

public class WebCamViewHolder extends RecyclerView.ViewHolder {

    protected ImageView mWebCamThumbnail;
    protected TextView mWebCamDescription;

    public WebCamViewHolder(View itemView) {
        super(itemView);
        this.mWebCamThumbnail = (ImageView) itemView.findViewById(R.id.webcam_thumbnail);
        this.mWebCamDescription = (TextView) itemView.findViewById(R.id.webcam_description);
        itemView.setOnClickListener(WebCamActivity.webCamOnClickListener);
    }
}
