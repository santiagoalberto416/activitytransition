package com.greatcall.activitytransition.options;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.greatcall.activitytransition.R;
import com.greatcall.logging.Instrumented;

import java.util.List;

/**
 * Created by skirk on 5/19/17.
 */

public class AdapterOptions extends RecyclerView.Adapter<AdapterOptions.ViewHolder> implements Instrumented{
    private List<OptionModel> mDataset;
    private Context context;
    private int lastPosition = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder implements Instrumented{
        public ImageView imageOption;
        public TextView textOption;

        public ViewHolder(View v) {
            super(v);
            trace();
            imageOption = (ImageView)v.findViewById(R.id.iconOption);
            textOption = (TextView)v.findViewById(R.id.textOption);
        }
    }

    public AdapterOptions(Context context) {
        trace();
        this.context = context;
    }

    public void setDataSet(List<OptionModel> options){
        trace();
        if(mDataset != null){
            mDataset.clear();
        }
        mDataset = options;
        notifyDataSetChanged();
    }

    @Override
    public AdapterOptions.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        trace();
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_call, parent, false);
        ViewHolder vh = new ViewHolder(viewHolder);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterOptions.ViewHolder holder, final int position) {
        holder.imageOption.setImageDrawable(mDataset.get(position).getIcon());
        holder.textOption.setText(mDataset.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trace();
                log("Click option item to launch intent");
                Intent intent = mDataset.get(position).getIntent();
                if(intent!=null){
                    context.startActivity(intent);
                }
            }
        });
        setAnimation(holder.itemView, position);


    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset != null ? mDataset.size() : 0;
    }
}

