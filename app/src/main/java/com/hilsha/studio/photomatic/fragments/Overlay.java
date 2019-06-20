package com.hilsha.studio.photomatic.fragments;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.hilsha.studio.photomatic.AppCommonUtils;
import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.Utils.BitmapUtils;
import com.hilsha.studio.photomatic.Utils.DataController;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Overlay extends Fragment {

    RecyclerView overlayListView;
    SeekBar seekbarforoverlay;
    OverLayAdapter overLayAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<Bitmap> drawables;
    ArrayList<String> nameList;
    ChangeOverlay changeOverlay;
    public Overlay() {
        // Required empty public constructor

    }

    public void setChangeOverlay(Activity activity){
        changeOverlay=(ChangeOverlay) activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_overlay, container, false);
        drawables=DataController.sharedInstance.getDrawables();
        nameList=new ArrayList<String>();

        overlayListView=(RecyclerView) view.findViewById(R.id.overlayListView);
        seekbarforoverlay=(SeekBar)view.findViewById(R.id.seekbarforoverlay);
        seekbarforoverlay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeOverlay.changeOverlay(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
         layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        overLayAdapter=new OverLayAdapter(drawables,getContext());
         overlayListView.setLayoutManager(layoutManager);
         overlayListView.setAdapter(overLayAdapter);
         overLayAdapter.notifyDataSetChanged();
        return  view;
    }

    public interface ChangeOverlay{
        void changeOverlay(int pos);
    }

}
