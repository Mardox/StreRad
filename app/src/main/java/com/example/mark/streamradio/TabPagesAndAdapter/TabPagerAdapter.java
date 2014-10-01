package com.example.mark.streamradio.TabPagesAndAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    MainScreen mainScreen = new MainScreen();
    RadiosScreen radioScreen = new RadiosScreen();
    NewsScreen newsScreen = new NewsScreen();

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return mainScreen;
            case 1:
                return radioScreen;
            case 2:
                return newsScreen;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3; //No of Tabs
    }
}
