package org.jesteban.clockomatic.activities.mainactivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import org.jesteban.clockomatic.BuildConfig;
import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.activities.companieseditoractivity.CompaniesEditorActivity;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPagePresenter;
import org.jesteban.clockomatic.activities.settingactivity.SettingsActivity;
import org.jesteban.clockomatic.helpers.Constants;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageContract;
import org.jesteban.clockomatic.fragments.reportpage.ReportPagePresenter;
import org.jesteban.clockomatic.helpers.UndoMessage;
import org.jesteban.clockomatic.model.UndoAction;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;
import org.jesteban.clockomatic.views.DebugFragment;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    private static final Logger  LOGGER = Logger.getLogger(MainActivity.class.getName());

    private MainActivityContract.Presenter presenter = null;
    private ReportPageContract.Presenter reportPagePresenter = null;
    private ViewPager mViewPager=null;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOGGER.info("Creating MainActivy " + BuildConfig.VERSION_NAME + " debug=" + BuildConfig.DEBUG);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        presenter = new MainActivityPresenter(this, getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Not need to do something
            }

            @Override
            public void onPageSelected(int position) {
                presenter.onViewChangeShowPage(ShowPageProviderContract.PageId.values()[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Not need to do something
            }
        });
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // If changed orientations, viewPager fragments are no recreated, so it need pointer to stateController
        // In first run fragments are not created
        // https://stackoverflow.com/questions/13815010/showing-specified-page-when-view-pager-is-first-created

        LOGGER.log(Level.INFO,"onCreate patch to already created fragments");
        if (getSupportFragmentManager().getFragments()!=null) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment instanceof ReportPageFragment) {
                    ReportPageFragment reportPageFragment = (ReportPageFragment) fragment;
                    reportPagePresenter = new ReportPagePresenter(reportPageFragment);
                    SewingBox.sewPresentersView(reportPagePresenter, presenter, reportPageFragment);
                }
                if (fragment instanceof RegisterPageFragment) {
                    RegisterPageFragment registerPageFragment = (RegisterPageFragment) fragment;
                    SewingBox.sewPresentersView(new RegisterPagePresenter(registerPageFragment, this),presenter,registerPageFragment);

                }
            }
        }
        Spinner spinner = (Spinner) findViewById(R.id.main_spinner_choose_company);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onSelectedCompany(parent.getItemAtPosition(position).toString(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                presenter.onSelectedCompany(null, -1);

            }
        });
        spinner.setVisibility(GONE);
        presenter.startUi();

    }

    @Override
    public void setAvailableCompanies(List<String> companies){
        Spinner spinner = (Spinner) findViewById(R.id.main_spinner_choose_company);
        CardView cardCompanyPicker = (CardView) findViewById(R.id.card_company_picker);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, companies.toArray(new String[0]));
        spinner.setAdapter(adapter);

    }
    @Override
    public void setVisibleAvailableCompanies(Boolean visible){
        Spinner spinner = (Spinner) findViewById(R.id.main_spinner_choose_company);
        CardView cardCompanyPicker = (CardView) findViewById(R.id.card_company_picker);
        if (visible){
            cardCompanyPicker.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }   else {
            cardCompanyPicker.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSelectedCompany(int index, int color){
        Spinner spinner = (Spinner) findViewById(R.id.main_spinner_choose_company);
        CardView cardCompanyPicker = (CardView) findViewById(R.id.card_company_picker);
        cardCompanyPicker.setBackgroundColor(color);
        spinner.setSelection(index);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (BuildConfig.DEBUG){
            MenuItem menuItemDebug = menu.findItem(R.id.action_debug);
            if (menuItemDebug!=null){
                menuItemDebug.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public void askConfirmWipeData() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        presenter.wipeStore();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                    default:
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_wipe_all_are_you_sure).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    @Override
    public void showAbout(){
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.ic_launcher);
        String title = getResources().getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME;
        if (BuildConfig.DEBUG) {
            title += " debug";
        }
        builder.setTitle( title );
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @Override
    public void showSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    final static int REQUEST_CODE_FOR_COMPANIES_EDITOR = 1;
    @Override
    public void showCompaniesEditor(){
        Intent intent = new Intent(this, CompaniesEditorActivity.class);
        startActivityForResult(intent,REQUEST_CODE_FOR_COMPANIES_EDITOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== REQUEST_CODE_FOR_COMPANIES_EDITOR){
            presenter.onFinishCompaniesEditor((resultCode== Activity.RESULT_OK));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LOGGER.info("main Menu");
        int id = item.getItemId();
        LOGGER.info("main Menu selected " + Integer.toString(id));
        presenter.onSelectedMenuItem(id);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(MainActivityContract.Presenter presenter) {
        throw new RuntimeException("This class create his own presenter!");
    }

    @Override
    public MainActivityContract.Presenter getPresenter() {
        return this.presenter;
    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .show();
    }
    @Override
    public void showUndoMessage(final UndoAction undoAction) {
        UndoMessage.showUndoMessage(undoAction,coordinatorLayout,getResources());
    }

    @Override
    public void showPage(ShowPageProviderContract.PageId id) {
        int item = 0;
        switch (id){
            case REGISTER_PAGE:
                item = 0;
                break;
            case REPORT_PAGE:
                item = 1;
                break;
            case DEBUG_PAGE:
                item = 2;
                break;
        }
        mViewPager.setCurrentItem(item,true);
    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment result = null;
            LOGGER.info("SectionsPagerAdapter getItem(" + Integer.toString(position));
            if (position == 0) {
                RegisterPageFragment registerPageFragment = RegisterPageFragment.newInstance();
                SewingBox.sewPresentersView(new RegisterPagePresenter(registerPageFragment,getBaseContext()),presenter,registerPageFragment);
                result = registerPageFragment;

            } else if (position == 1) {
                ReportPageFragment reportPageFragment = ReportPageFragment.newInstance();
                reportPagePresenter = new ReportPagePresenter(reportPageFragment);
                SewingBox.sewPresentersView(reportPagePresenter,presenter,reportPageFragment);
                result = reportPageFragment;
            } else{
                return new DebugFragment();
            }
            return result;
        }

        @Override
        public int getCount() {

            if (BuildConfig.DEBUG){
                return 3;
            }
            return 2;
        }


    }


}
