package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
        tideViewHolder.extremalDate.setText(Utils.formatDate(tideItem.getExtremalDate()));
        tideViewHolder.extremalType.setText(formatExtremalType(tideItem.getExtremalType()));
        tideViewHolder.extremalValue.setText(tideItem.getExtremalValue());
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
            return "minimo";
        } else if (rawExtremal.toLowerCase().contains("max")) {
            return "massimo";
        } else {
            return rawExtremal;
        }

    }
}
