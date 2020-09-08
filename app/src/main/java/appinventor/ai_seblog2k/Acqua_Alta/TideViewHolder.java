package appinventor.ai_seblog2k.Acqua_Alta;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by stognacci on 10/08/2016.
 */

public class TideViewHolder extends RecyclerView.ViewHolder {

    protected TextView extremalDate;
    protected TextView extremalType;
    protected TextView extremalValue;

    public TideViewHolder(View itemView) {
        super(itemView);
        this.extremalDate = (TextView) itemView.findViewById(R.id.extremalDate);
        this.extremalType = (TextView) itemView.findViewById(R.id.extremalType);
        this.extremalValue = (TextView) itemView.findViewById(R.id.extremalValue);
    }
}
