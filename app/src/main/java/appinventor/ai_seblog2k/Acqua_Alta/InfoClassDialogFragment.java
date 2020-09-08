package appinventor.ai_seblog2k.Acqua_Alta;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by stognacci on 19/08/2016.
 */

public class InfoClassDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        String versionName = BuildConfig.VERSION_NAME;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.app_name) + " - " + versionName)
                .setMessage(R.string.info_DialogMessage)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // no nothing
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
