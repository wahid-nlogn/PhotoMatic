package com.hilsha.studio.photomatic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.sample.GPUImageFilterTools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.hilsha.studio.photomatic.Utils.AdjustConfig;


public class ImageEditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    Unbinder unbinder;
    @BindView(R.id.globalbrightnessSeekBar)
    SeekBar globalbrightnessSeekBar;

    ChangeBrightness changeBrightness;
    ChangeContrast changeContrast;
    ChangeSatuarion changeSatuarion;
    ChangeSharpness changeSharpness;
    ChangeGama changeGama;

    @BindView(R.id.globalcontrastSeekBar)
    SeekBar globalcontrastSeekBar;

    @BindView(R.id.globalSaturationSeekBar)
    SeekBar globalSaturationSeekBar;

    @BindView(R.id.globalSharpenSeekBar)
    SeekBar globalSharpenSeekBar;

    @BindView(R.id.globalgamaSeekbar)
    SeekBar globalgamaSeekbar;



    public ImageEditFragment() {
        // Required empty public constructor

    }
    public void setgamaListener(Activity activity){

        changeGama=(ChangeGama) activity;
        }
    public void setChangeBrightness(Activity activity, Bitmap bitmap){

        changeBrightness=(ChangeBrightness)activity;



    }
    public void setContrastlistener(Activity activity){
        changeContrast=(ChangeContrast) activity;
    }
    public void setChangeSatuarionlistener(Activity activity){
        changeSatuarion=(ChangeSatuarion)activity;
    }

    public void setSharpnessListener(Activity activity){
        changeSharpness=(ChangeSharpness)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_image_edit, container, false);

        unbinder = ButterKnife.bind(this, view);
        setSeekbarforBrightness();
        setseekbarforContrast();
        setssekbarforsaturation();
        setseekbarforSharpness();
        setseekbarforGama();
        return view;
    }

    private void setseekbarforGama() {
        globalgamaSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeGama.changegama(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public  void setconfigsaturation(AdjustConfig config){
        globalSaturationSeekBar.setProgress((int) (config.slierIntensity * globalbrightnessSeekBar.getMax()));
}
    public void setActiveConfig(AdjustConfig config) {

        globalbrightnessSeekBar.setProgress((int) (config.slierIntensity * globalbrightnessSeekBar.getMax()));
    }
    public void setConfigforContrast(AdjustConfig config){
        globalcontrastSeekBar.setProgress((int) (config.slierIntensity * globalbrightnessSeekBar.getMax()));
    }
    public void setconfigforsharpness(AdjustConfig config){
        globalSharpenSeekBar.setProgress((int) (config.slierIntensity * globalbrightnessSeekBar.getMax()));
    }
    private void setSeekbarforBrightness() {

        globalbrightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / (float)seekBar.getMax();

               // adjsutconfig.setIntensity(intensity,true);
                changeBrightness.changeBrightness(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void setseekbarforContrast(){
        AdjustConfig adjsutconfig1= new AdjustConfig(1, 0.1f, 1.0f, 3.0f);
        setConfigforContrast(adjsutconfig1);
        globalcontrastSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float intensity = progress / (float)seekBar.getMax();

                adjsutconfig1.setIntensity(intensity,true);
                changeContrast.changeContrast(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void setssekbarforsaturation() {

        globalSaturationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                changeSatuarion.changeSaturation(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event

    public void setseekbarforSharpness(){

        globalSharpenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                changeSharpness.changeSharpeness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    interface ChangeBrightness{
        void changeBrightness(int intensity);
    }
    interface ChangeContrast{
        void changeContrast(int intensity);
    }
    interface ChangeSatuarion{
        void changeSaturation(int intensity);
    }
    interface  ChangeSharpness{
        void changeSharpeness(int intensity);
    }
    interface  ChangeGama{
        void changegama(int intensity);
    }
}
