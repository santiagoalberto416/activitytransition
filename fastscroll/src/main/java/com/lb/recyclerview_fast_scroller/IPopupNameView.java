package com.lb.recyclerview_fast_scroller;

import android.support.annotation.NonNull;

/**
 * Created by Savid Salazar on 6/8/17.
 */

public interface IPopupNameView {

    /**
     * @param position
     *     the item position
     * @return the section name for this item
     */
    @NonNull
    CharSequence getLabel(int position);
}
