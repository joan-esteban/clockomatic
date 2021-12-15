package org.jesteban.clockomatic.fragments.editclocksday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.entryeditdialog.EntryEditDialogContract;
import org.jesteban.clockomatic.fragments.entryeditdialog.EntryEditDialogPresenter;
import org.jesteban.clockomatic.fragments.entryeditdialog.EntryEditDialogView;
import org.jesteban.clockomatic.helpers.Entry2Html;
import org.jesteban.clockomatic.helpers.EntryKindSupport;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;

import java.io.IOException;
import java.util.logging.Logger;


// Example animations: https://proandroiddev.com/enter-animation-using-recyclerview-and-layoutanimation-part-1-list-75a874a5d213
public class EditClocksDayV2Fragment extends Fragment implements EditClockDayContract.View {
    private static final Logger LOGGER = Logger.getLogger(EditClocksDayV2Fragment.class.getName());
    private EditClockDayContract.Presenter presenter = null;
    private RecyclerView recyclerView = null;
    private ClocksAdapter adapter = null;

    @Override
    public EditClockDayContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void setPresenter(EditClockDayContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEntries(EntrySet entries) {
        EntrySet oldEntries = adapter.entries;
        adapter.setData(entries);

        if (oldEntries == null || oldEntries.getEntries().isEmpty()) {
            adapter.notifyDataSetChanged();
            return;
        }
        int nNew = entries.getEntries().size();
        int nOld = oldEntries.getEntries().size();
        if (nNew > nOld) {
            adapter.notifyItemRangeChanged(0, nOld);
            adapter.notifyItemRangeInserted(nOld, (nNew - nOld));
        } else {
            if (nNew > 0) adapter.notifyItemRangeChanged(0, nNew);
            adapter.notifyItemRangeRemoved(nNew, (nOld - nNew));
        }
    }

    @Override
    public void deleteEntry(int position) {
        adapter.entries.getEntries().remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public EntryEditDialogContract.Presenter showEditEntryDialog(int position, Entry entry) throws IOException {
        FragmentManager fm = getChildFragmentManager();
        EntryEditDialogView dialog = EntryEditDialogView.newInstance(entry);
        EntryEditDialogContract.Presenter entryEditDialogPresenter = new EntryEditDialogPresenter(dialog, getContext());
        SewingBox.sewPresentersView(entryEditDialogPresenter, getPresenter(), dialog);

        dialog.setPresenter(entryEditDialogPresenter);
        // https://stackoverflow.com/questions/18561119/android-gettargetfragment-and-settargetfragment-what-are-they-used-for
        dialog.setTargetFragment(EditClocksDayV2Fragment.this, 300);
        dialog.show(fm, EntryEditDialogView.class.getName());
        return entryEditDialogPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_clocks_day, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_view_clocks);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ClocksAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }


    class ClocksAdapter extends RecyclerView.Adapter<ClocksAdapter.ClocksViewHolder> {

        EntrySet entries = null;

        public void setData(EntrySet data) {
            entries = data;
        }

        @Override
        public ClocksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.cardview_register_clock, null);

            return new ClocksViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ClocksViewHolder holder, final int position) {
            final Entry entry = entries.getEntries().get(position);
            Entry2Html aux = new Entry2Html();
            holder.main.setText(aux.getJustHoursPlain(entry));
            if (entry.getDescription().length() == 0) {
                holder.detail.setVisibility(View.GONE);
            } else holder.detail.setVisibility(View.VISIBLE);
            holder.detail.setText(entry.getDescription());
            String distanceStr = "";
            try {
                distanceStr = entry.getDayOffsetBetweenBelongingDayAndRegisterAsString();

            } catch (Exception e) {
                LOGGER.warning("error getDayOffsetBetweenBelongingDayAndRegister " + e.getMessage());
                distanceStr = "?";
            }
            holder.offsetDays.setText(distanceStr);
            if (distanceStr.length() > 0) holder.offsetDays.setVisibility(View.VISIBLE);
            else holder.offsetDays.setVisibility(View.GONE);
            holder.image.setImageResource(EntryKindSupport.getIconId(entry.getKind()));
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        presenter.selectedEntry(position, entry);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOGGER.warning("Cant launch dialog edit entry: " + e.toString());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if (entries == null) return 0;
            return entries.getEntries().size();
        }

        class ClocksViewHolder extends RecyclerView.ViewHolder {
            public TextView main;
            public TextView detail;
            public TextView offsetDays;
            public CardView card;
            public ImageView image;

            public ClocksViewHolder(View itemView) {
                super(itemView);
                main = (TextView) itemView.findViewById(R.id.main_text);
                detail = (TextView) itemView.findViewById(R.id.detail_text);
                card = (CardView) itemView.findViewById(R.id.card_entry);
                image = (ImageView) itemView.findViewById(R.id.icon_image);
                offsetDays = (TextView) itemView.findViewById(R.id.offset_day_text);

            }
        }

    }
}
