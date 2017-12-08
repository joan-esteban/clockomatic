package org.jesteban.clockomatic.fragments.report_page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.StateController;
import org.jesteban.clockomatic.helpers.DependencyInjector;
import org.jesteban.clockomatic.model.Entry;
import org.jesteban.clockomatic.model.EntrySet;
import org.jesteban.clockomatic.model.State;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ViewClocksMonthFragment extends Fragment implements Observer,DependencyInjector.Injectable<StateController> {
    private static final Logger  LOGGER = Logger.getLogger(ViewClocksMonthFragment.class.getName());
    private String showBelongingMonthPrefix = null;
    private StateController stateController = null;
    private View view = null;

    public ViewClocksMonthFragment() {
        // Required empty public constructor
    }

    public static ViewClocksMonthFragment newInstance() {
        return new ViewClocksMonthFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_view_clocks_month, container, false);
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
        Calendar cal = Calendar.getInstance();
        showBelongingMonthPrefix = df.format(cal.getTime());
        TextView textView = (TextView) view.findViewById(R.id.month_text_view);
        textView.setMovementMethod(new ScrollingMovementMethod());

        if (stateController!= null) syncWithState(stateController.getState());
        return view;
    }

    public void setShowBelongingMonthPrefix(String dayPrefix) {
        LOGGER.log(Level.INFO,String.format("setShowBelongingDayPrefix dayPrefix = %s" , dayPrefix));
        this.showBelongingMonthPrefix = dayPrefix;
        if (stateController!= null) syncWithState(stateController.getState());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (stateController!= null) stateController.addObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (stateController!= null) stateController.deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stateController!= null) stateController.deleteObserver(this);
    }

    @Override // DependencyInjector.Injectable<StateController>
    public boolean setDependency(StateController dependency) {
        if (dependency==null) return false;
        stateController = dependency;
        stateController.addObserver(this);
        syncWithState(stateController.getState());
        return true;
    }


    private String getBelongingMonthPretty(){
        if (showBelongingMonthPrefix==null) return "?";
        DateFormat df = new SimpleDateFormat(Entry.FORMAT_BELONGING_MONTH);
        Date result;
        try {
            result = df.parse(this.showBelongingMonthPrefix);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE,"set a wrong belonging month", e);
            return "";
        }
        DateFormat pretty = new SimpleDateFormat("y");
        return new DateFormatSymbols().getMonths()[result.getMonth()] + " " + pretty.format(result);
    }

    public void syncWithState(State state){
        TextView textView = (TextView) view.findViewById(R.id.month_text_view);
        EntrySet entries = state.getEntries().getEntriesBelongingDayStartWith(this.showBelongingMonthPrefix);

        StringBuilder html = new StringBuilder();
        html.append("<center><h1>");
        html.append(getBelongingMonthPretty());
        html.append("</h1></center>");

        Set<String> days = entries.getDistintBelongingDays();
        for (String day : days) {
            html.append("<h3>" + day + "</h3>");
            for (Entry entry : entries.getEntriesBelongingDayStartWith(day)) {
                html.append("<li>" + entry + "</li>");
            }
        }
        textView.setText(Html.fromHtml(html.toString()));
    }

    @Override
    public void update(Observable observable, Object arg) {
        LOGGER.log(Level.FINE, "update from a Observable");
        if (observable instanceof StateController) {
            StateController state = (StateController) observable;
            LOGGER.log(Level.FINE, "update from StateController");
            syncWithState(state.getState());
        }
    }

}
