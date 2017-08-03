package com.greatcall.activitytransition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.greatcall.logging.Instrumented;

/**
 * Created by israel on 5/30/17.
 */

public final class CallModel implements Instrumented {

    public String nameOfUser;
    public String phoneNumber;
    public Bitmap icon;
    public Long favoriteAddedTime;

    public CallModel() {
    }

    public CallModel(String nameOfUser, String phoneNumber, Bitmap icon, Long favoriteAddedTime) {
        this.nameOfUser = nameOfUser;
        this.phoneNumber = phoneNumber;
        this.icon = icon;
        this.favoriteAddedTime = favoriteAddedTime;
    }

    public static Bitmap byteArrayToBitmap(byte[] blob) {
        return BitmapFactory.decodeByteArray(blob, 0, blob.length);
    }

    public boolean isFavorite() {
        return favoriteAddedTime != null && !favoriteAddedTime.equals(new Long(0));
    }

}
