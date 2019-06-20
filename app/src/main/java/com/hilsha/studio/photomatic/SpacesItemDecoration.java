package com.hilsha.studio.photomatic;

import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);

        if(position == 0){
            outRect.left = space+5;
            outRect.right = space/2;
        }else if(position == state.getItemCount()-1){
            outRect.right = space+5;
            outRect.left = space/2;
        }else{

            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = 0;
            outRect.top = 0;
        }

    }
}