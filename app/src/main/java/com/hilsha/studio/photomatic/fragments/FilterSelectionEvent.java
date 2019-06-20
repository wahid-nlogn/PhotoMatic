package com.hilsha.studio.photomatic.fragments;



public class FilterSelectionEvent {

    public static int FULL_REFRESH = 100;
    public static int FIRST_INDEX_REFRESH = 200;

    private int eventType;

    public int getEventType() {
        return eventType;
    }

    public FilterSelectionEvent(int eventType) {
        this.eventType = eventType;
    }

}
