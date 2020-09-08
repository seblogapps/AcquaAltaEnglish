package appinventor.ai_seblog2k.Acqua_Alta;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by stognacci on 10/08/2016.
 */

public class TideRecyclerViewAdapter extends RecyclerView.Adapter<TideViewHolder> {

    private static final String TAG = TideRecyclerViewAdapter.class.getSimpleName();
    private static final int EXTREMAL_VALUE_THRESHOLD = 80;

    private List<Tide> mTideList;
    private Context mContext;

    public TideRecyclerViewAdapter(Context context, List<Tide> tideList) {
        this.mContext = context;
        this.mTideList = tideList;
    }

    @Override
    public TideViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tide_browse, null);
        TideViewHolder tideViewHolder = new TideViewHolder(view);
        return tideViewHolder;
    }

    @Override
    public void onBindViewHolder(TideViewHolder tideViewHolder, int position) {
        Tide tideItem = mTideList.get(position);
        Log.d(TAG, "onBindViewHolder: Processing: " + tideItem.getExtremalDate() + " position --> " + position);
        tideViewHolder.extremalDate.setText(Utils.formatJSONDateTime(tideItem.getExtremalDate()));
        tideViewHolder.extremalType.setText(formatExtremalType(tideItem.getExtremalType()));
        tideViewHolder.extremalValue.setText(tideItem.getExtremalValue());
        if (Integer.parseInt(tideItem.getExtremalValue()) >= EXTREMAL_VALUE_THRESHOLD) {
            tideViewHolder.extremalValue.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return (null != mTideList ? mTideList.size() : 0);
    }

    public void clear() {
        mTideList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tide> tideList) {
        mTideList.addAll(tideList);
        notifyDataSetChanged();
    }

    private String formatExtremalType(String rawExtremal) {
        if (rawExtremal.toLowerCase().contains("min")) {
            return mContext.getString(R.string.extremal_Min);
        } else if (rawExtremal.toLowerCase().contains("max")) {
            return mContext.getString(R.string.extremal_Max);
        } else {
            return rawExtremal;
        }

    }
}
