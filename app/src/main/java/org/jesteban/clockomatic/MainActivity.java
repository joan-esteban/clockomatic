package org.jesteban.clockomatic;

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


import org.jesteban.clockomatic.controllers.MainActivityContract;
import org.jesteban.clockomatic.controllers.MainActivityPresenter;
import org.jesteban.clockomatic.controllers.PresenterBase;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPagePresenter;
import org.jesteban.clockomatic.helpers.SewingBox;
import org.jesteban.clockomatic.fragments.registerpage.RegisterPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageFragment;
import org.jesteban.clockomatic.fragments.reportpage.ReportPageContract;
import org.jesteban.clockomatic.fragments.reportpage.ReportPagePresenter;
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
        if (BuildConfig.DEBUG){
            menuShowDebug();
        }
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

    public void menuWipeData() {
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

    public void menuShowAbout(){
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    public void menuShowDebug(){
        // https://stackoverflow.com/questions/7801954/how-to-programmatically-show-next-view-in-viewpager
        mViewPager.setCurrentItem(3,true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        LOGGER.info("main Menu");
        int id = item.getItemId();
        LOGGER.info("main Menu selected " + Integer.toString(id));
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_wipe_data:
                LOGGER.info("main Menu selected action_wipe_data");
                menuWipeData();
                return true;
            case R.id.action_about:
                LOGGER.info("main Menu selected action_about");
                menuShowAbout();
                return true;
            case R.id.action_debug:
                LOGGER.info("main Menu selected action_debug");
                menuShowDebug();
                return true;
            default:
                LOGGER.info("unknown menu item selected");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(PresenterBase presenter) {

    }

    @Override
    public PresenterBase getPresenter() {
        return this.presenter;
    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void showRegisterPage() {
        mViewPager.setCurrentItem(0,true);
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
