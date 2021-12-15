package org.jesteban.clockomatic.views;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;

/**
 * This show multiples entries
 */

public class MyPairedEntriesGridView extends GridView implements  MyPairedEntriesGridViewContract.View{

    List<MyPairedEntryViewContract.PairedEntryVisualData> entries = new ArrayList<>();


    public MyPairedEntriesGridView(Context context) {
        this(context,null);
    }

    public MyPairedEntriesGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyPairedEntriesGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public MyPairedEntriesGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        GridView gridView = this;
        gridView.setAdapter(new LinkEntriesAdapter(context));
        //ViewGroup.LayoutParams params = gridView.getLayoutParams();
        //params.height = LayoutParams.WRAP_CONTENT;
        //gridView.setLayoutParams(params);
        /*
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        gridView.setNumColumns(2);
        else
            gridView.setNumColumns(4);
            */
    }
    // If not is affected by ListView scroll and only show 1 row
    //https://stackoverflow.com/questions/16819135/set-fixed-gridview-row-height
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        } else {
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    @Override
    public void showPairedEntries(List<MyPairedEntryViewContract.PairedEntryVisualData> entries) {
        LinkEntriesAdapter adapter = (LinkEntriesAdapter) getAdapter();
        this.entries = entries;
        adapter.set(entries);
        adapter.notifyDataSetChanged();
    }

    public void add(){
        LinkEntriesAdapter adapter = (LinkEntriesAdapter) getAdapter();
        entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("13:02", "14:05"));
        adapter.set(entries);
        adapter.notifyDataSetChanged();
        this.requestLayout();
    }

    public class LinkEntriesAdapter extends BaseAdapter {
        List<MyPairedEntryViewContract.PairedEntryVisualData> entries = new ArrayList<>();

        private Context mContext;
        public LinkEntriesAdapter(Context c) {
            mContext = c;
            MyPairedEntryViewContract.PairedEntryVisualData entriesExample = new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00");
            entries.add(entriesExample);
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));
            entries.add(new MyPairedEntryViewContract.PairedEntryVisualData("10:00","20:00"));

        }

        public int getCount() {
            if (entries!=null)
                return entries.size();
            else return 0;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            MyPairedEntryView linkEntryView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                linkEntryView = new MyPairedEntryView(mContext);
                //linkEntryView.setPadding(8, 8, 8, 8);
                linkEntryView.setGravity(CENTER);
            } else {
                linkEntryView = (MyPairedEntryView) convertView;
            }
            MyPairedEntryViewContract.PairedEntryVisualData data = entries.get(position);
            linkEntryView.showPairedEntry(data);
            return linkEntryView;
        }

        public void set(List<MyPairedEntryViewContract.PairedEntryVisualData> entries){
            this.entries = entries;
        }
    }
}
