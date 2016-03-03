package me.venj.criminalintent;

import java.util.UUID;

/**
 * Created by venj on 16/3/2.
 */
public class Crime {
    private UUID mID;
    private String mTitle;

    public Crime() {
        mID = UUID.randomUUID();
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
