package org.jesteban.clockomatic.activities.companieseditoractivity;


import android.app.Activity;
import android.os.Bundle;

public class CompaniesEditorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CompaniesEditorFragment companiesEditorFragment = new CompaniesEditorFragment();
        companiesEditorFragment.setPresenter(new CompaniesEditorPresenter(companiesEditorFragment, this));
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, companiesEditorFragment)
                .commit();
    }
}
