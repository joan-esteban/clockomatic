package org.jesteban.clockomatic.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.jesteban.clockomatic.R;

// Animation https://medium.com/@Sserra90/android-writing-a-compound-view-1eacbf1957fc

public class MyLinkEntryView extends LinearLayout {

    public MyLinkEntryView(Context context) {
        this(context,null);
    }

    public MyLinkEntryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinkEntryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        populate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyLinkEntryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populate();
    }
    private void populate(){
        inflate(getContext(), R.layout.view_link_entry, this);

        //View view = inflater.inflate(R.layout.view_link_entry, this, false);
    }


}



