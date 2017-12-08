package org.jesteban.clockomatic;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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


import org.jesteban.clockomatic.fragments.RegisterPageFragment;
import org.jesteban.clockomatic.fragments.ReportPageFragment;
import org.jesteban.clockomatic.helpers.DependencyInjector;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    private StateController stateController = new StateController();
    DependencyInjector<StateController> stateControllerInjector = new DependencyInjector<StateController>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOGGER.info("Creating MainActivy " + BuildConfig.VERSION_NAME);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // If changed orientations, viewPager fragments are no recreated, so it need pointer to stateController
        // In first run fragments are not created
        // https://stackoverflow.com/questions/13815010/showing-specified-page-when-view-pager-is-first-created
        LOGGER.log(Level.INFO,"onCreate injectStateController");
        stateControllerInjector.injectList(stateController,getSupportFragmentManager().getFragments() );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void menuWipeData() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        stateController.wipeStore();
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
            default:
                LOGGER.info("unknown menu item selected");
        }


        return super.onOptionsItemSelected(item);
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
                LOGGER.info("Creating instance for RegisterPageFragment(" + stateController + ")");
                result = RegisterPageFragment.newInstance();
            } else {
                LOGGER.info("Creating instance for ReportPageFragment(" + stateController + ")");
                result = ReportPageFragment.newInstance();
                //fragment.setStateController(stateController);
                //return fragment;
            }
            stateControllerInjector.inject(stateController,result );
            return result;
        }

        @Override
        public int getCount() {
            return 2;
        }


    }


}
