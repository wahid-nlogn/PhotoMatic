package com.hilsha.studio.photomatic;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Random;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link LandingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LandingFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.galleryicon)
    ImageView galleryicon;

    @BindView(R.id.cameraicon)
    ImageView cameraicon;

    @BindView(R.id.landingFrag)
    RelativeLayout landingFrag;

    int min=1;
    int max=9;
    Random random=new Random();

    public LandingFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.cameraicon)
    public void onCameraclicked(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            ImagePicker.cameraOnly().start(getActivity());


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }
    @OnClick(R.id.galleryicon)
    public void onGalleryIconClick(){
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            ImagePicker.create(getActivity())
                                    .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                                    .folderMode(true) // folder mode (false by default)
                                    .toolbarFolderTitle("Folder") // folder selection title
                                    .toolbarImageTitle("Tap to select") // image selection title
                                    .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                                    .single() // single mode

                                    .limit(1) // max images can be selected (99 by default)
                                    .showCamera(true) // show camera or not (true by default)
                                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                                    // custom image loader, must be serializeable
                                    .start(); // start image picker activity with request code


                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public static LandingFragment newInstance() {
        LandingFragment fragment = new LandingFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        int randomVal=min+random.nextInt(max);
        Log.d("randomval",randomVal+"");
        if(landingFrag!=null){
        if(randomVal%3==1){
            landingFrag.setBackground(getActivity().getResources().getDrawable(R.drawable.london));

        }else if(randomVal%3==2){
            landingFrag.setBackground(getActivity().getResources().getDrawable(R.drawable.nyc));
        }else{
            landingFrag.setBackground(getActivity().getResources().getDrawable(R.drawable.pariss));
        }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_landing, container, false);

        unbinder = ButterKnife.bind(this, view);
       return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }


}
