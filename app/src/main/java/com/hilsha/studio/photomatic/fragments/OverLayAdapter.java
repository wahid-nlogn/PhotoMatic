package com.hilsha.studio.photomatic.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hilsha.studio.photomatic.R;
import com.hilsha.studio.photomatic.Utils.DataController;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OverLayAdapter extends RecyclerView.Adapter<OverLayAdapter.MyViewHolder> {

    private ArrayList<Bitmap> mDataset;
    Context context;
    public  OverLayAdapter(){

    }
    public OverLayAdapter(ArrayList<Bitmap> dataset,Context c){
        mDataset=dataset;
        context=c;
        Log.d("malisha",mDataset.size()+" ");
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.overlayitem,parent,false);
        return  new MyViewHolder(view);
    }

    int row_position;
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.overlayImageView.setImageBitmap(mDataset.get(position));
        holder.overlayName.setText("Overlay "+position);

        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_position=position;
                notifyDataSetChanged();
                DataController.sharedInstance.setPosition(position+1);
                sendFilterSelectedBroadcast(context);
            }
        });
        if(row_position==position){
            holder.imageContainer.setBackgroundResource(R.drawable.rect_border);
            holder.overlayName.setTextColor(Color.parseColor("#ffffff"));
            holder.overlayName.setBackgroundColor(Color.parseColor("#ff1a1a"));
        }else{
            holder.overlayName.setTextColor(Color.parseColor("#1a1a1a"));
            holder.imageContainer.setBackground(null);
            holder.overlayName.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    private void sendFilterSelectedBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(AppCommon.POSITION_SELECTED);
        context.sendBroadcast(intent);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout containerView;
        ImageView overlayImageView;
        RelativeLayout imageContainer;
        TextView overlayName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            containerView=(LinearLayout) itemView.findViewById(R.id.containerView);
            overlayImageView=(ImageView) itemView.findViewById(R.id.overlayImageView);
            imageContainer=(RelativeLayout) itemView.findViewById(R.id.imageContainer);
            overlayName=(TextView) itemView.findViewById(R.id.overlayName);
        }
    }

}
