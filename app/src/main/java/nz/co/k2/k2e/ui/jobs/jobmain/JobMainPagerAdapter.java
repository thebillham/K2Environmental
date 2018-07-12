package nz.co.k2.k2e.ui.jobs.jobmain;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class JobMainPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;

    public JobMainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // General Info
                return InfoFragment.newInstance(0, "General");
            case 1: // Tasks
                return TasksFragment.newInstance(1, "Tasks");
            case 2: // Samples
                return SamplesFragment.newInstance(2, "Samples");
            case 3: // Check
                return CheckFragment.newInstance(2, "Check");
            default:
                return null;
        }
    }
}
