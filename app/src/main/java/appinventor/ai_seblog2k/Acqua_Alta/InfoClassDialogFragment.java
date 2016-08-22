package appinventor.ai_seblog2k.Acqua_Alta;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by stognacci on 19/08/2016.
 */

public class InfoClassDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.InfoDialogTitle)
                .setMessage(R.string.InfoDialogMessage)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // no nothing
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
