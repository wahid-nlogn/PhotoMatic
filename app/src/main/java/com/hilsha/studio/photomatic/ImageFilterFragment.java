package com.hilsha.studio.photomatic;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.io.InputStream;


public class ImageFilterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Unbinder unbinder;
    @BindView(R.id.filterList)
    RecyclerView filterList;
    @BindView(R.id.globalRestoreSeekBar)
    SeekBar globalRestoreSeekBar;
    FilterAdapter filterAdapter;

    FiltersName filtersName;

    SeekbarIntensity seekbarIntensity;
    public ImageFilterFragment() {
        // Required empty public constructor
    }
    public void setSeekbarIntensity(Activity activity){
        seekbarIntensity=(SeekbarIntensity)activity;
    }

    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_image_filter, container, false);

        unbinder = ButterKnife.bind(this, view);
        CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
        filtersName=new FiltersName();
        filtersName.insertData();
        filterList.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        filterList.setLayoutManager(layoutManager);
        filterAdapter=MainActivity.getfiteradapter();
        filterList.setAdapter(filterAdapter);

        globalRestoreSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / 100.0f;
                seekbarIntensity.selectintensity(intensity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return  view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    public CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
        //注意， 这里回传的name不包含任何路径名， 仅为具体的图片文件名如 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            Log.i(Common.LOG_TAG, "Loading file: " + name);
            AssetManager am = getActivity().getAssets();
            InputStream is;
            try {
                is = am.open(name);
            } catch (IOException e) {
                Log.e(Common.LOG_TAG, "Can not open file " + name);
                return null;
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            Log.i(Common.LOG_TAG, "Loading bitmap over, you can choose to recycle or cache");

            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.
            //唯一不需要马上recycle的应用场景为 多个不同的滤镜都使用到相同的bitmap
            //那么可以选择缓存起来。
            bmp.recycle();
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }
    interface SeekbarIntensity{
        void selectintensity(float intensity);
    }
}
