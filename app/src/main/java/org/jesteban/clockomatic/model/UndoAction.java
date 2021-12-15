package org.jesteban.clockomatic.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class UndoAction {
    public interface UndoActionStepContract {
        void executeUndo() throws ParseException;
    }
    public UndoAction(){
        this.description ="no description";
        this.actions = new ArrayList<>();
    }
    public UndoAction(String description, List<UndoActionStepContract> actions){
        this.description = description;
        this.actions = actions;
    }
    public String description;
    public List<UndoActionStepContract> actions;

    public void add(UndoAction undo){
        this.actions.addAll(undo.actions);
        this.description += "+"+undo.description;
    }
    public void execute(){
        for (UndoActionStepContract undo : actions){
            try {
                undo.executeUndo();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
