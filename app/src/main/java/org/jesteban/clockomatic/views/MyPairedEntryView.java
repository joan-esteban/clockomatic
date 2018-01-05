package org.jesteban.clockomatic.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jesteban.clockomatic.R;

// Animation https://medium.com/@Sserra90/android-writing-a-compound-view-1eacbf1957fc

public class MyPairedEntryView extends LinearLayout implements  MyPairedEntryViewContract.View {
    TextView textViewInitialHour = null;
    TextView textViewFinalHour = null;
    ImageView imageViewLinkIcon = null;
    public MyPairedEntryView(Context context) {
        this(context,null);
    }

    public MyPairedEntryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPairedEntryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyPairedEntryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populate();
    }
    private void populate(){
        inflate(getContext(), R.layout.view_link_entry, this);
        textViewInitialHour = (TextView) this.findViewById(R.id.text_view_initial_hour);
        textViewFinalHour = (TextView) this.findViewById(R.id.text_view_final_hour);
        imageViewLinkIcon = (ImageView) this.findViewById(R.id.image_view_link_icon);
    }


    @Override
    public void showPairedEntry(MyPairedEntryViewContract.PairedEntryVisualData data) {
        String textInitial = data.getInitialHour();
        String textFinal = data.getEndHour();

        if (textInitial != null){
            textViewInitialHour.setText(textInitial);
        } else textViewInitialHour.setText("");
        if (textFinal != null){
            textViewFinalHour.setText(textFinal);
        }else textViewFinalHour.setText("");
        if (textInitial != null && textFinal != null)
            imageViewLinkIcon.setVisibility(VISIBLE);
        else imageViewLinkIcon.setVisibility(GONE);
    }
}



