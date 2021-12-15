package org.jesteban.clockomatic.fragments.infoperiodview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jesteban.clockomatic.R;

public class InfoPeriodView extends LinearLayout implements  InfoPeriodViewContract.View{
    InfoPeriodViewContract.Presenter presenter = null;
    TextView textNumWorkingDays = null;
    TextView textTotalWorkingTime = null;
    TextView textHrWorkingTime = null;
    public InfoPeriodView(Context context) {
        this(context,null);
    }

    public InfoPeriodView(Context context, AttributeSet attrs) {
        super(context, attrs);
        populate(this);
    }

    public InfoPeriodView(Context context, AttributeSet attrs, ViewGroup viewGroup){
        super(context, attrs);
        populate(viewGroup);
    }

    private void populate(ViewGroup viewGroup) {
        inflate(getContext(), R.layout.view_info_period, viewGroup);
        textNumWorkingDays = (TextView) this.findViewById(R.id.text_num_working_days);
        textTotalWorkingTime = (TextView) this.findViewById(R.id.text_total_working_time);
        textHrWorkingTime = (TextView) this.findViewById(R.id.text_rh_working_time);
    }

    @Override
    public void setPresenter(InfoPeriodViewContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public InfoPeriodViewContract.Presenter getPresenter() {
        return presenter;
    }


    @Override
    public void showData(InfoPeriodVisualData data) {
        if (textNumWorkingDays!=null) textNumWorkingDays.setText(data.numWorkingDays);
        if (textTotalWorkingTime!=null) textTotalWorkingTime.setText(data.totalWorkingTime);
        if (textHrWorkingTime!=null) textHrWorkingTime.setText(data.hrWorkingTime);

    }
}
