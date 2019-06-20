package com.hilsha.studio.photomatic.fragments;

import android.content.Context;

import android.util.Log;


import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class FilterCategoryPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = FilterCategoryPagerAdapter.class.getName();
    private List<FilterCategory> categories;
    private Context context;

    public FilterCategoryPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    public FilterCategoryPagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
        categories = FilterFactory.loadFilters(context);
        Log.d(TAG, "size of category: " + categories.size());

    }

    @Override
    public Fragment getItem(int position) {
       return  FiltersFragment.newInstance(categories.get(position));
    }

    @Override
    public int getCount() {
        return categories.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String category = categories.get(position).getTitle();
        String capitalized = category.substring(0,1).toUpperCase() + category.substring(1);

        return capitalized;
    }
}
