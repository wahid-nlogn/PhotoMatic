package com.hilsha.studio.photomatic.fragments;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FilterFactory {

    private static List<FilterCategory> categories = new ArrayList<>();

    public static List<FilterCategory> loadFilters(Context context) {

        if (categories.size() > 0) {
            return categories;
        }

        InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier("filters", "raw", context.getPackageName()));
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            Gson gson = new Gson();
            Type type = new TypeToken<List<FilterCategory>>(){}.getType();
            categories = gson.fromJson(reader, type);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return categories;
    }

}
