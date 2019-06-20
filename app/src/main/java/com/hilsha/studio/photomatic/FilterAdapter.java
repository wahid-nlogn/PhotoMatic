package com.hilsha.studio.photomatic;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hilsha.studio.photomatic.Utils.DataController;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;
import org.wysaid.view.ImageGLSurfaceView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    public List<String> listOfFilters;
    public static  String mCurrentConfig;
    int count=0;
    int rowpostion=0;
    Context context;
    public OnFilterSelected onFilterSelected;
    public void setOnFilterSelected(Activity activity){
        onFilterSelected=(OnFilterSelected)activity;
    }

    public FilterAdapter(){

    }
    public  FilterAdapter(List<String> map,Context con){
        context=con;
        listOfFilters=map;
    }
    public static  Bitmap myBitmap;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout layout;
        TextView filtername;
        ImageGLSurfaceView imageView;

        public MyViewHolder(LinearLayout v) {
            super(v);
            layout=v;
            filtername=(TextView) layout.findViewById(R.id.filtername);
            imageView=(ImageGLSurfaceView) layout.findViewById(R.id.filterImage);
            myBitmap=DataController.sharedInstance.getMyBitmap();
            imageView.setSurfaceCreatedCallback(new ImageGLSurfaceView.OnSurfaceCreatedCallback() {
                @Override
                public void surfaceCreated() {
                    imageView.setImageBitmap(myBitmap);

                    imageView.setFilterWithConfig(mCurrentConfig);
                }
            });
            imageView.setDisplayMode(ImageGLSurfaceView.DisplayMode.DISPLAY_ASPECT_FILL);

        }
    }


    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filteritem_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.filtername.setText(listOfFilters.get(position));


            holder.imageView.post(new Runnable() {
                @Override
                public void run() {


                    //mCurrentConfig = MainActivity.EFFECT_CONFIGS[position];

                    holder.imageView.setFilterWithConfig(mCurrentConfig);
                    count++;
                    Log.d("wahidpp","count "+count+" "+mCurrentConfig );
                }
            });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterSelected.onSelectedFilter(position);
              /*  rowpostion=position;
                notifyDataSetChanged();*/
            }
        });
       /* if(rowpostion==position){
            holder.imageView.setBackground(context.getDrawable(R.drawable.background));

        }else {
            holder.imageView.setBackground(null);
        }*/
    }

    @Override
    public int getItemCount() {
        return listOfFilters.size();
    }
    interface OnFilterSelected{
        void onSelectedFilter(int pos);
    }
}
