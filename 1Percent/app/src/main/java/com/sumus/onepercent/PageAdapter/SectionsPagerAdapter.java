package com.sumus.onepercent.PageAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sumus.onepercent.Fragment.MainFragment;
import com.sumus.onepercent.Fragment.MoreFragment;
import com.sumus.onepercent.Fragment.PrizeFragment;
import com.sumus.onepercent.Fragment.VoteFragment;

import java.lang.ref.WeakReference;
import java.util.Hashtable;


/**
 * Created by MINI on 2016-10-27.
 */

 public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Bundle args = null;
        switch (position) {
            case 0:
                fragment =  MainFragment.newInstance("hello","world");
                break;
            case 1:
                fragment = VoteFragment.newInstance("hello", "world");
                break;
            case 2:
                fragment = PrizeFragment.newInstance("hello", "world");
                break;
            case 3:
                fragment = MoreFragment.newInstance("hello", "world");
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
        }
        return null;
    }
}