package journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Network;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;

/**
 * Created by Prof-Mohamed Atef on 2/3/2019.
 */

public class SnackBarClassLauncher {
    public void SnackBarInitializer(Snackbar snackbar) {
        ShowSnackMessage(snackbar);
    }

    private void ShowSnackMessage(Snackbar snackbar) {
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    public void SnackBarLoadedData(Snackbar snackbar) {
        ShowSnackMessage(snackbar);
    }


}