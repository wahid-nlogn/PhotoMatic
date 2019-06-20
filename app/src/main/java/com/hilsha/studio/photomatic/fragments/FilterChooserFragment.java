package com.hilsha.studio.photomatic.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.Utils.DataController;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FilterChooserFragment extends Fragment {

    private ScrollingControllableViewPager categoryPager;
    private PagerSlidingTabStrip categoryPagerStrip;
    private FilterCategoryPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter_chooser, container, false);

        categoryPager = rootView.findViewById(R.id.categoryViewPager);
        categoryPager.setPagingEnabled(false);
        adapter = new FilterCategoryPagerAdapter(getChildFragmentManager(), getContext());
        categoryPager.setAdapter(adapter);

        categoryPagerStrip = rootView.findViewById(R.id.categorySlidingTabStrip);
        categoryPagerStrip.setViewPager(categoryPager);

        categoryPagerStrip.setTabClickListener(new TabClickListener() {
            @Override
            public void onTabClicked(int tabIndex) {
               // AnalyticsManager.logEvent(AnalyticsConstants.FILTER_CATEGORY_CHANGED);

                // if no explicit selection or none filter selected
               /* if (DataController.sharedInstance.getSelectedFilter().filterIndex == 0) {
                    DataController.sharedInstance.setSelectedFilter(new DataController.FilterSelection(tabIndex, 0)); //resetting filter selection
                }*/
                EventBus.getDefault().post(new FilterSelectionEvent(FilterSelectionEvent.FULL_REFRESH)); //post notify filterrecyclerviewadapter notify change
            }
        });

        return rootView;
    }

    //Do nothing if received on back press event

}
