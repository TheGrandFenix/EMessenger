package com.fenix.e.extra;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.fenix.e.fragment.BaseLoginFragment;
import com.fenix.e.fragment.ExtraLoginFragment;


public class LoginPagerAdapter extends FragmentStatePagerAdapter {


    public LoginPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new BaseLoginFragment();
            case 1: return new ExtraLoginFragment();
            default: return new ExtraLoginFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
