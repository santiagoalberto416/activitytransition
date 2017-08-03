package com.greatcall.activitytransition.options;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.greatcall.activitytransition.R;
import com.greatcall.logging.Instrumented;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skirk on 5/19/17.
 */

public final class OptionModel implements Instrumented {

    private Drawable icon;
    private String title;
    private Intent intent;

    public OptionModel() {
        trace();
    }

    private OptionModel(Drawable icon, String title, Intent intent) {
        trace();
        this.icon = icon;
        this.title = title;
        this.intent = intent;
    }

    public Drawable getIcon() {
        trace();
        return icon;
    }

    public void setIcon(Drawable icon) {
        trace();
        this.icon = icon;
    }

    public String getTitle() {
        trace();
        return title;
    }

    public void setTitle(String title) {
        trace();
        this.title = title;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public List<OptionModel> getOptions(Context context){
        trace();
        List<OptionModel> options = new ArrayList<>();

        Object[][] optionConstructorParams = {
                { R.drawable.ic_call_white_48dp, R.string.callback, null },
                { R.drawable.message_icon, R.string.textMessage, null},
                { R.drawable.ic_person_add, R.string.addNewPerson, null },
                { R.drawable.ic_action_add, R.string.addToExisting, null},
                { R.drawable.ic_cancel, R.string.blockThisNumber, null},
                { R.drawable.trash_icon, R.string.deleteFromHistory, null}};

        for( Object[] params : optionConstructorParams ) {
            options.add( new OptionModel(ContextCompat.getDrawable(context, (int) params[0] ),
                    context.getString( (int) params[1] ),
                    (Intent) params[2] ) );
        }
        return options;
    }



}
