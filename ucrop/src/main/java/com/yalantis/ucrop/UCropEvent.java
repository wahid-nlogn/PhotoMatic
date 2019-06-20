package com.yalantis.ucrop;

/**
 * Created by minhaz on 10/10/17.
 */

public class UCropEvent {

    public static int UCROP_ACTIVITY_LAUNCHED = 1;
    public static int UCROP_NEXT_CLICKED = 2;

    private int eventFor;

    public int getEventFor() {
        return eventFor;
    }

    public UCropEvent(int eventFor) {
        this.eventFor = eventFor;
    }
}
