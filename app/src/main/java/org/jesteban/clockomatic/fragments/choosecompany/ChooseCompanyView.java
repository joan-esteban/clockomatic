package org.jesteban.clockomatic.fragments.choosecompany;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.jesteban.clockomatic.model.Company;

import java.util.List;

public class ChooseCompanyView extends android.support.v7.widget.AppCompatSpinner implements ChooseCompanyContract.View {
    public ChooseCompanyView(Context context) {
        this(context,null);
    }

    public ChooseCompanyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.style.Widget_Spinner);
    }

    public ChooseCompanyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,Spinner.MODE_DROPDOWN);
    }

    public ChooseCompanyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        populate();
    }
    protected void populate(){
        String[] listItems = {"Main Company","Another Job","que","tal"};
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.setAdapter(adapter);
        this.setEnabled(true);
        this.setSelection(2);
    }
    @Override
    public void setPresenter(ChooseCompanyContract.Presenter presenter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ChooseCompanyContract.Presenter getPresenter() {
        return null;
    }

    @Override
    public void showCompanies(List<Company> companies) {
        throw new UnsupportedOperationException();
    }
}
