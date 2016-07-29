package io.xdevs23.cornowser.browser.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.xdevs23.ui.utils.BarColors;

import io.xdevs23.cornowser.browser.CornBrowser;
import io.xdevs23.cornowser.browser.R;
import io.xdevs23.cornowser.browser.activity.bkmhis.fragments.BookmarkFragment;
import io.xdevs23.cornowser.browser.activity.bkmhis.fragments.HistoryFragment;
import io.xdevs23.cornowser.browser.browser.BrowserStorage;

public class BookmarkHistoryActivity extends AppCompatActivity {

    public static String[] tabTitles;
    private Fragment[] fragments = {
            new BookmarkFragment(),
            new HistoryFragment()
    };

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.bkmhis_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BarColors.enableBarColoring(getWindow(), R.color.bkmhis_statusbar_background);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.bkmhis_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.bkmhis_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.bkmhis_action_delete_all:
                if (mViewPager.getCurrentItem() == 0) {
                    ((BookmarkFragment)fragments[0]).bookmarks.createNew();
                    CornBrowser.getBrowserStorage().putString(
                            BrowserStorage.BPrefKeys.bookmarksPref,
                            ((BookmarkFragment)fragments[0]).bookmarks.toString()
                    );
                    ((BookmarkFragment)fragments[0]).updateAdapter();
                } else {
                    ((HistoryFragment)fragments[1]).history.createNew();
                    CornBrowser.getBrowserStorage().putString(
                            BrowserStorage.BPrefKeys.historyPref,
                            ((HistoryFragment)fragments[1]).history.toString()
                    );
                    ((HistoryFragment)fragments[1]).updateAdapter();
                    CornBrowser.getWebEngine().eraseHistory();
                }
                mSectionsPagerAdapter.notifyDataSetChanged();
                break;
            default: break;
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
            return fragments[position];
        }

        @Override
        public int getCount() {
            try {
                return tabTitles.length;
            } catch(NullPointerException ex) {
                return 1;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                return tabTitles[position];
            } catch(NullPointerException | IndexOutOfBoundsException ex) {
                // We know that (for whatever reason) one of the above exceptions can occur.
                // So let's catch them and give an empty string instead of crashing or
                // giving null back.
                return "";
            }
        }
    }

}
