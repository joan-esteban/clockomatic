package org.jesteban.clockomatic.fragments.workSchedulerEditDialog;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import org.jesteban.clockomatic.R;

public class WorkSchedulerEditDialog extends Dialog {
    public WorkSchedulerEditDialog(@NonNull Context context) {
        this(context,0);
    }

    public WorkSchedulerEditDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        populate(context);
    }

    protected WorkSchedulerEditDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        populate(context);
    }

    private void populate(Context context){
        setContentView(R.layout.work_scheduler_edit_dialog);
        ImageButton  but = (ImageButton) findViewById(R.id.button_time_enter_early);


        setGroup(R.id.check_box_enter_restrictions,R.id.layout_enter_restrictions);
        setGroup(R.id.check_box_leave_restrictions,R.id.layout_leave_restrictions);
        setGroup(R.id.check_box_midday_restrictions,R.id.layout_midday_restrictions);
        setGroup(R.id.check_box_workingtime_restrictions,R.id.layout_workingtime_restrictions);
    }

    private void setGroup(int id_check_box, final int id_layout){
        CheckBox checkBoxEnterRestrictions = (CheckBox) findViewById(id_check_box);
        TableLayout layout = (TableLayout) findViewById(id_layout);
        for (int i=0 ; i< layout.getChildCount(); i++){
            View child = layout.getChildAt(i);
            TableRow tableRow = (TableRow) child;
            if (tableRow != null){
                bindButtonsWithTextBox(tableRow);
            }
        }
        checkBoxEnterRestrictions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TableLayout l = (TableLayout) findViewById(id_layout);
                if (isChecked) l.setVisibility(View.VISIBLE);
                else l.setVisibility(View.GONE);
            }
        });
    }

    private  View getChild(ViewGroup vg, Class  c){
        for (int i=0 ; i< vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            if (c.isInstance(child)) return child;
        }
        return null;
    }
    private void bindButtonsWithTextBox(TableRow tableRow ){
        ImageButton but = (ImageButton) getChild(tableRow, ImageButton.class);
        final EditText editView = (EditText) getChild(tableRow, EditText.class);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTimePicker(8,00,true, editView);
            }
        });
    }

    public void showDialogTimePicker(int hour, int minute, boolean is24HourView, final EditText editView) {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String s = String.format("%1$02d:%2$02d", hourOfDay,minute);
                editView.setText( s );
            }
        }, hour, minute, is24HourView);
        dialog.show();
    }
}
