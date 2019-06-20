package com.hilsha.studio.photomatic.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilsha.studio.photomatic.AppCommonUtils;
import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.Utils.BitmapUtils;
import com.hilsha.studio.photomatic.Utils.DataController;


import java.util.ArrayList;
import java.util.List;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import jp.co.cyberagent.android.gpuimage.GPUImage;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;


public class FiltersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static String TAG = FiltersRecyclerViewAdapter.class.getName();
    private List<String> lookupImagesName;

    private Context context;
    private Bitmap targetBitmap;
    private int currentlyShowingCategoryIndex;
    private GPUImage gpuImage;

    private List<Bitmap> filterBitmaps = new ArrayList<>();
    private Handler handler;

    public void setCurrentlyShowingCategoryIndex(int currentlyShowingCategoryIndex) {
        this.currentlyShowingCategoryIndex = currentlyShowingCategoryIndex;
    }

    public void setTargetBitmap(Bitmap targetBitmap) {
        this.targetBitmap = targetBitmap;
    }


    public FiltersRecyclerViewAdapter(List<String> lookupImages, Context context) {
        this.lookupImagesName = lookupImages;

        handler = new Handler(Looper.getMainLooper());
        loadLookupImageFromDrawableInBackgroundAndReload(lookupImages, context);
    }

    private void loadLookupImageFromDrawableInBackgroundAndReload(final List<String> lookupImages, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                List<String> resourceIds = new ArrayList<>();

                List<String> categoryResources = DataController.sharedInstance.getCategoryResources(currentlyShowingCategoryIndex);
                if (categoryResources == null) {

                    for (String image : lookupImages) {

                        int imageResourceId = AppCommonUtils.getResId(image, R.drawable.class);

                        resourceIds.add(imageResourceId + "");
                        Log.d("malisha", imageResourceId+" "+image);
                    }

                    DataController.sharedInstance.putCategoryResources(currentlyShowingCategoryIndex, resourceIds);

                } else {
                    resourceIds = categoryResources;
                    Log.d("malisha", resourceIds.toString()+" ");

                }

                for (String resId : resourceIds) {


                    Bitmap bitmapFromResource = BitmapUtils.decodeSampledBitmapFromResource(context.getResources(), Integer.parseInt(resId), 50, 50);
                    Log.d(TAG, "bitmap width: " + bitmapFromResource.getWidth() + " height: " + bitmapFromResource.getHeight());

                        Bitmap bitmap = getBitmapWithFilterApplied(context, bitmapFromResource);
                        filterBitmaps.add(bitmap);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View inflatedView = layoutInflater.inflate(R.layout.recycler_view_item_filter_image, null);
        context = parent.getContext();






        return new ItemViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        itemViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataController.sharedInstance.setSelectedFilter(new DataController.FilterSelection(currentlyShowingCategoryIndex, position));
                sendFilterSelectedBroadcast(context);
                notifyDataSetChanged();
            }
        });

        int dimension = (int) context.getResources().getDimension(R.dimen.filter_image_size);
        /*setViewSizeForContainer(itemViewHolder.container, 80);
        setViewSize(itemViewHolder.secondContainer, 80);
        setViewSizeInPixels(itemViewHolder.imageContainerView, dimension, dimension); //*/

        itemViewHolder.filterNameTextView.setText(getFilterName(position));

        ImageView imageView = itemViewHolder.imageView;
        setImageInImageView(position, imageView);

        itemViewHolder.imageContainerView.setBackground(null);
        itemViewHolder.filterNameTextView.setTextColor(context.getResources().getColor(R.color.defaulttextcolor));
        //itemViewHolder.filterNameTextView.setTypeface(ResourcesCompat.getFont(context, R.font.ladylike),Typeface.BOLD);
        itemViewHolder.filterNameTextView.setBackgroundColor(context.getResources().getColor(R.color.white));
        DataController.FilterSelection selectedFilter = DataController.sharedInstance.getSelectedFilter();
        if (selectedFilter != null) {
            if (isFilterSelected(position, selectedFilter)) {
                itemViewHolder.imageContainerView.setBackgroundResource(R.drawable.rect_border);
                itemViewHolder.filterNameTextView.setTextColor(context.getResources().getColor(R.color.white));
                itemViewHolder.filterNameTextView.setBackgroundColor(context.getResources().getColor(R.color.tabSelectedColor));
            }
        }
    }

    private boolean isFilterSelected(int position, DataController.FilterSelection selectedFilter) {
        if (currentlyShowingCategoryIndex == selectedFilter.categoryIndex && position == selectedFilter.filterIndex) {
            return true;
    }

        return false;
    }

    private void setImageInImageView(int position, ImageView imageView) {
        if (isAllBitmapsLoaded()) {
//            imageView.setImageBitmap(getBitmapWithFilterApplied(context, filterBitmaps.get(position)));
            imageView.setImageBitmap(filterBitmaps.get(position));
        }
    }

    private boolean isAllBitmapsLoaded() {
        return filterBitmaps.size() == lookupImagesName.size();
    }

    private void setViewSize(View targetView, int widthDps, int heightDps) {
        int pixelsW = AppCommonUtils.getDpToPixels(widthDps, context);
        int pixelsH = AppCommonUtils.getDpToPixels(heightDps, context);

        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
        layoutParams.width = pixelsW;
        layoutParams.height = pixelsH;
        targetView.setLayoutParams(layoutParams);
    }

    private void setViewSizeInPixels(View targetView, int widthPixels, int heightPixels) {
        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
        layoutParams.width = widthPixels;
        layoutParams.height = heightPixels;
        targetView.setLayoutParams(layoutParams);
    }

    private void setViewSizeForContainer(View targetView, int dps) {
        int pixels = AppCommonUtils.getDpToPixels(dps, context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(pixels, pixels + 20);
        targetView.setLayoutParams(layoutParams);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public int getItemCount() {
        return lookupImagesName.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        View container;
        TextView filterNameTextView;
        View imageContainerView;

        ItemViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.filterImageView);
            container = itemView.findViewById(R.id.containerView);
            filterNameTextView = itemView.findViewById(R.id.filterName);
            imageContainerView = itemView.findViewById(R.id.imageContainer);
        }
    }

    private void sendFilterSelectedBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(AppCommon.FILTER_SELECTED);
        context.sendBroadcast(intent);
    }

    private Bitmap getBitmapWithFilterApplied(Context context, Bitmap bitmap) {

        if (gpuImage == null) {
            gpuImage = new GPUImage(context.getApplicationContext());
            gpuImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE);
        }

        GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        lookupFilter.setBitmap(bitmap);

        gpuImage.setFilter(lookupFilter);

        Log.d(TAG, "bitmap width: " + targetBitmap.getWidth() + " height: " + targetBitmap.getHeight());

        Bitmap bitmapWithFilterApplied = gpuImage.getBitmapWithFilterApplied(targetBitmap);

        Log.d(TAG, "bitmap width: " + targetBitmap.getWidth() + " height: " + targetBitmap.getHeight());

        return bitmapWithFilterApplied;

    }

    private String getFilterName(int position) {
        String filterName;
        FilterCategory category = FilterFactory.loadFilters(context).get(currentlyShowingCategoryIndex);
        if (position == 0) {
            filterName = "None";
        } else {
            filterName = category.getFileName(position).substring(0,1).toUpperCase() + position;
        }

        return filterName;
    }


}


