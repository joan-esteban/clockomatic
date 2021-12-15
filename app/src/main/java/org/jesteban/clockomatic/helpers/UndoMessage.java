package org.jesteban.clockomatic.helpers;


import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.model.UndoAction;

public class UndoMessage {

    public static void showUndoMessage(final UndoAction undoAction, View view,Resources resources) {
        Snackbar.make(view, undoAction.description, Snackbar.LENGTH_INDEFINITE )
                .setAction(resources.getString(R.string.msg_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoAction.execute();
                    }
                }).setDuration(Constants.TIME_TO_SHOW_UNDO_MESSAGE_IN_MS)
                .show();
    }

}
