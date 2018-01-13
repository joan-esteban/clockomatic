package org.jesteban.clockomatic.mainactivity;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import org.jesteban.clockomatic.BuildConfig;
import org.jesteban.clockomatic.R;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPagePresenter;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageContract;
import org.jesteban.clockomatic.fragments.reportpage.ReportPagePresenter;
import org.jesteban.clockomatic.providers.ShowPageProviderContract;
import org.jesteban.clockomatic.views.DebugFragment;

import java.util.logging.Level;
import java.util.logging.Logger;

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
                presenter.OnViewChangeShowPage(ShowPageProviderContract.PageId.values()[position]);
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
                    SewingBox.sewPresentersView(new RegisterPagePresenter(registerPageFragment),presenter,registerPageFragment);

                }
            }
        }
        presenter.startUi();

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
                .setAction("Action", null).show();
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
                SewingBox.sewPresentersView(new RegisterPagePresenter(registerPageFragment),presenter,registerPageFragment);
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
