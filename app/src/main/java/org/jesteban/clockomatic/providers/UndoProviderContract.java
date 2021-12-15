package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.model.UndoAction;

public interface UndoProviderContract extends Provider{
    static final String KEY_PROVIDER ="undo";

    void push(UndoAction action);
    UndoAction pop();
    UndoAction peek();
    Boolean isEmpty();

    void subscribe(UndoProviderContract.Listener listener);

    public interface Listener {
        void onPushUndo();
        void onPopUndo();
    }
}
