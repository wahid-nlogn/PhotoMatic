package com.hilsha.studio.photomatic.Utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataController {


    public Bitmap myBitmap;
    private Map<Integer, List<String>> categoryResources = new HashMap<Integer, List<String>>();
    private FilterSelection selectedFilter;
    private ArrayList<Bitmap>drawables=new ArrayList<Bitmap>();
    private DataController(){

    }
    public void setData(Bitmap x){
        drawables.add(x);
    }
    public ArrayList<Bitmap> getDrawables(){
        return drawables;
    }
    public int getLengthofdrawables(){
        return drawables.size();
    }
    public void putCategoryResources(int categoryIndex, List<String>resourceIds) {
        categoryResources.put(categoryIndex, resourceIds);
    }
    public static DataController sharedInstance = new DataController();
    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }
    public Bitmap getMyBitmap(){
        return myBitmap;
    }


    public List<String> getCategoryResources(int categoryIndex) {
        return categoryResources.get(categoryIndex);
    }

    public void reset() {

        myBitmap = null;

        selectedFilter = null;
        drawables.clear();
    }
    public int position;
    public FilterSelection getSelectedFilter() {
        return selectedFilter;
    }

    public void setPosition(int pos){
        position=pos;
    }
    public int getPosition()
    {
        return position;
    }
    public void setSelectedFilter(FilterSelection selectedFilter) {
        this.selectedFilter = selectedFilter;
    }
    public static class FilterSelection {
        public int categoryIndex;
        public int filterIndex;

        public FilterSelection(int categoryIndex, int filterIndex) {
            this.categoryIndex = categoryIndex;
            this.filterIndex = filterIndex;
        }
    }

}
