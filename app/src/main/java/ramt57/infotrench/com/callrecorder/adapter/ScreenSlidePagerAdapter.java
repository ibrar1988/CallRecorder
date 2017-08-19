package ramt57.infotrench.com.callrecorder.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ramt57.infotrench.com.callrecorder.fragments.AllFragment;

/**
 * Created by sandhya on 19-Aug-17.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new AllFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
