package com.hilsha.studio.photomatic.fragments;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.SpacesItemDecoration;
import com.hilsha.studio.photomatic.Utils.BitmapUtils;
import com.hilsha.studio.photomatic.Utils.DataController;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



public class FiltersFragment extends Fragment {
    private static String TAG = FiltersFragment.class.getName();
    private RecyclerView categoryFiltersRecyclerView;
    private static String SERIALIZABLE_KEY = "category";
    private FiltersRecyclerViewAdapter filtersAdapter;

    public static FiltersFragment newInstance(FilterCategory category) {
        FiltersFragment imagesFragment = new FiltersFragment();

        Bundle args = new Bundle();
        args.putSerializable(SERIALIZABLE_KEY, category);
        imagesFragment.setArguments(args);

        return imagesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category_filters, container, false);

        FilterCategory category = (FilterCategory) getArguments().getSerializable(SERIALIZABLE_KEY);

        double scalingFactor = .2;//getResources().getInteger(R.integer.bitmap_size);
        final Bitmap workingBitmap = DataController.sharedInstance.getMyBitmap();
        final int width = (int) (workingBitmap.getWidth() * scalingFactor);
        final int height = (int) (workingBitmap.getHeight() * scalingFactor);
        Bitmap thumbBitmap = BitmapUtils.decodeSampledBitmapFromBitmap(workingBitmap, width, height);

        filtersAdapter = new FiltersRecyclerViewAdapter(category.getFilterFileNames(), getContext());
        filtersAdapter.setTargetBitmap(thumbBitmap);
        filtersAdapter.setCurrentlyShowingCategoryIndex(category.getOrder());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryFiltersRecyclerView = rootView.findViewById(R.id.filtersRecyclerView);
        categoryFiltersRecyclerView.setLayoutManager(layoutManager);
        categoryFiltersRecyclerView.setAdapter(filtersAdapter);
        categoryFiltersRecyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getInteger(R.integer.filter_item_space)));
        categoryFiltersRecyclerView.setAlpha(0f);
        categoryFiltersRecyclerView.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        ObjectAnimator animator = ObjectAnimator.ofFloat(categoryFiltersRecyclerView, "alpha", 0.0f, 0.5f, 1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1500);
        animator.setStartDelay(400);
        animator.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d(TAG, "is visible to user: " + getUserVisibleHint() + " " + isVisibleToUser);

        if (getUserVisibleHint()) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(categoryFiltersRecyclerView, "alpha", 0.0f, 0.5f, 1f);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(1500);
            animator.start();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFilterSelectionUpdated(FilterSelectionEvent event) {
        if (event.getEventType() == FilterSelectionEvent.FULL_REFRESH) {
            filtersAdapter.notifyDataSetChanged();

        } else if (event.getEventType() == FilterSelectionEvent.FIRST_INDEX_REFRESH) {
            filtersAdapter.notifyItemChanged(0);
        }
    }
}
