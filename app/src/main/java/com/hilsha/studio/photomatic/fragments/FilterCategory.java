package com.hilsha.studio.photomatic.fragments;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FilterCategory implements Serializable {

    @SerializedName("title")
    private String title;

    @SerializedName("order")
    private int order;

    @SerializedName("items")
    private int items;

    private List<String> filterFileNames;

    public List<String> getFilterFileNames(){
        if (filterFileNames == null){
            List<String> filters = new ArrayList<String>();
            for (int i = 0; i < items; i++){
                String fileName = String.format("%s_%d", title.toLowerCase(),i);
                filters.add(fileName);
            }
            filterFileNames = filters;
        }
        return filterFileNames;
    }

    public String getFileName(int index){
        String filterFileName = getFilterFileNames().get(index);
        return filterFileName;
    }

    public int getResourceId(int index, Context context){
        String filterFileName = getFileName(index);
        int identifier = context.getResources().getIdentifier(filterFileName, "drawable", context.getPackageName());
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
}
