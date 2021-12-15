package org.jesteban.clockomatic.providers;


import org.jesteban.clockomatic.helpers.ObservableDispatcher;
import org.jesteban.clockomatic.model.UndoAction;


import java.util.Stack;

public class UndoProvider implements UndoProviderContract {
    Stack<UndoAction> stack = new Stack<>();
    private ObservableDispatcher<UndoProviderContract.Listener> observable = new ObservableDispatcher<>();

    @Override
    public String getName() {
        return UndoProviderContract.KEY_PROVIDER;
    }

    @Override
    public void push(UndoAction action) {
        // Add to pop from stack if executed
        action.actions.add(new UndoActionStepPop(this));
        stack.push(action);
        observable.notify("onPushUndo");
    }
    @Override
    public UndoAction pop(){
        UndoAction action =  stack.pop();
        observable.notify("onPopUndo");
        return action;
    }
    @Override
    public UndoAction peek(){
        return stack.peek();
    }
    @Override
    public Boolean isEmpty(){
        return stack.isEmpty();
    }

    @Override
    public void subscribe(UndoProviderContract.Listener listener) {
        observable.add(listener);
    }

    class UndoActionStepPop implements UndoAction.UndoActionStepContract {
        UndoProvider undoProvider = null;
        public UndoActionStepPop(UndoProvider undoProvider){
            this.undoProvider = undoProvider;
        }
        @Override
        public void executeUndo() {
            undoProvider.pop();
        }
    }


}
