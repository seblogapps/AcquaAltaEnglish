package appinventor.ai_seblog2k.Acqua_Alta_English;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by stognacci on 10/08/2016.
 */

public class TideRecyclerViewAdapter extends RecyclerView.Adapter<TideViewHolder> {

    private static final String TAG = TideRecyclerViewAdapter.class.getSimpleName();
    private static final String JSONDateTimePattern = "yyyy-MM-dd HH:mm:ss";
    private static final String defaultDateTimePattern = "dd/MM/yyyy HH:mm";

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
        tideViewHolder.extremalDate.setText(formatDate(tideItem.getExtremalDate()));
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

    private String formatDate(String rawDate) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SimpleDateFormat inFormatter = new SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(rawDate);
                String formattedDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(dateObj);
                Log.d(TAG, "formatDate: Formatted date (API>=24): " + formattedDateTime);
                return formattedDateTime;
            } else {
                java.text.SimpleDateFormat inFormatter = new java.text.SimpleDateFormat(JSONDateTimePattern);
                Date dateObj = inFormatter.parse(rawDate);
                java.text.SimpleDateFormat outFormatter = new java.text.SimpleDateFormat(defaultDateTimePattern);
                String formattedDateTime = outFormatter.format(dateObj);
                Log.d(TAG, "formatDate: Formatted date (API<24): " + formattedDateTime);
                return formattedDateTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "formatDate: Error formatting date from JSON");
            return rawDate;
        }
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
