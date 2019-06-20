package com.hilsha.studio.photomatic.fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.Utils.DeviceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends BottomSheetDialogFragment {

    Unbinder unbinder;

    @BindView(R.id.facebookshare)
    LinearLayout facebookshare;

    @BindView(R.id.instashare)
    LinearLayout instashare;

    @BindView(R.id.sharetwitter)
    LinearLayout sharetwitter;
    public SaveFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.facebookshare)
    void facebookShare(){
        Uri uri=DeviceUtils.getMyimageURL();
        DeviceUtils.shareImageViaFacebook(getContext(),uri);
    }
    @OnClick(R.id.instashare)
    void instashare(){
        Uri uri=DeviceUtils.getMyimageURL();
        DeviceUtils.shareImageInstagram(uri,getActivity());
    }
    @OnClick(R.id.sharetwitter)
    void twittershare(){
        Uri uri=DeviceUtils.getMyimageURL();
        DeviceUtils.sharetwitter(uri,getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_save, container, false);

        unbinder = ButterKnife.bind(this, view);
        return  view;
    }
    @Override
    public void onStart() {
        super.onStart();
// I'm using null here because drawing nothing is faster than drawing transparent pixels.
       /* getActivity().getWindow().setBackgroundDrawable(null);
        getView().setBackground(null);*/
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();


        unbinder.unbind();
    }

}
