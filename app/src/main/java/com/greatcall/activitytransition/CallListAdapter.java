package com.greatcall.activitytransition;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greatcall.logging.Instrumented;

import java.util.List;

import static com.greatcall.activitytransition.CallViewModel.NORMAL_ROW;

class CallListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Instrumented {

    private List<CallViewModel> appsDataList;
    private Context mContext;
    private Activity mActivity;

    public CallListAdapter(Activity activity) {
        trace();
        this.mActivity = activity;
        this.mContext = activity;
    }

    List<CallViewModel> getAppsDataList() {
        trace();
        return appsDataList;
    }

    void setAppsDataList(List<CallViewModel> appsDataList) {
        trace();
        if(this.appsDataList != null) {
            this.appsDataList.clear();
        }
        this.appsDataList = appsDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        trace();
        CallViewModel appData = appsDataList.get(position);
        return appData.getRowType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        trace();
        switch (viewType) {
            case NORMAL_ROW:
                log("Normal Row");
                View normalViewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_history, parent, false);
                return new NormalViewHolder(normalViewRow);
            default:
                log("Empty Row");
                View emptyViewRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_empty, parent, false);
                return new EmptyViewHolder(emptyViewRow);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        trace();
        CallViewModel appData = appsDataList.get(position);
        if (appData != null && viewHolder != null) {
            switch (appData.getRowType()) {
                case NORMAL_ROW:
                    log("Normal Row");
                    NormalViewHolder normalViewHolder = (NormalViewHolder) viewHolder;
                    normalViewHolder.typeAndDate.setText(appData.getPhoneNumber());
                    if(appData.getName().equals("") && appData.getPhoneNumber().length()>0 ){
                        normalViewHolder.header.setText(appData.getPhoneNumber());
                    }else if(appData.getName().length()>0){
                        normalViewHolder.header.setText(appData.getName());
                    }else {
                        normalViewHolder.header.setText("No Caller id ");
                    }
                    if(appData.getIcon()!=null) {
                        normalViewHolder.icon.setImageBitmap(appData.getIcon());
                    }
                    normalViewHolder.checkedLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trace();
                            Intent i = new Intent(mContext, SharedElementActivity.class);
                            i.putExtra("call", appData);
                            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(mActivity, false,
                                    new Pair<>(viewHolder.itemView, mActivity.getString(R.string.shared_view)),
                                    new Pair<>(mActivity.findViewById(R.id.action_bar), mActivity.getString(R.string.bar)));
                            ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, pairs);
                            mContext.startActivity(i, transitionActivityOptions.toBundle());
                        }
                    });
                    break;
                default :
                    log("Empty Row");
                    EmptyViewHolder emptyViewHolder = (EmptyViewHolder) viewHolder;
                    emptyViewHolder.txtEmptyName.setText(appData.getName());
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        trace();
        return appsDataList != null ? appsDataList.size() : 0;
    }



    private class NormalViewHolder extends RecyclerView.ViewHolder implements Instrumented {
        RelativeLayout checkedLinearLayout;
        private TextView header;
        private ImageView icon;
        private TextView typeAndDate;

        NormalViewHolder(View itemView) {
            super(itemView);
            trace();
            log("Normal holder");
            this.typeAndDate = (TextView)itemView.findViewById(R.id.dateAndTypeOfPhone);
            this.checkedLinearLayout = (RelativeLayout) itemView.findViewById(R.id.checkedLinearLayout);
            this.header = (TextView) itemView.findViewById(R.id.text_from_item);
            this.icon = (ImageView) itemView.findViewById(R.id.imageApp);
        }
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder implements Instrumented {
        LinearLayout llApplicationContent;
        TextView txtEmptyName;

        EmptyViewHolder(View itemView) {
            super(itemView);
            trace();
            log("Empty holder");
            this.llApplicationContent = (LinearLayout) itemView.findViewById(R.id.ll_application_content);
            this.txtEmptyName = (TextView) itemView.findViewById(R.id.txt_empty_name);
        }
    }
}
