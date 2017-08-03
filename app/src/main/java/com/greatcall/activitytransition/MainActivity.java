package com.greatcall.activitytransition;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.greatcall.logging.Instrumented;
import com.lb.recyclerview_fast_scroller.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Instrumented {

    RecyclerView mRecyclerView;
    private RecyclerViewFastScroller mFastScroller;
    private CallListAdapter mCallListAdapter;
    private SectionAndHeaderItemDecoration mItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_alternative);
//        View blueIconImageView = findViewById(R.id.blueView);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewApps);
        this.mFastScroller = (RecyclerViewFastScroller) findViewById(R.id.fastscroller);
        this.mCallListAdapter = new CallListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mFastScroller.setRecyclerView(mRecyclerView);
        this.mRecyclerView.setAdapter(mCallListAdapter);

//        blueIconImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, SharedElementActivity.class);
//                i.putExtra("textid", ((TextView)findViewById(R.id.textExample)).getText().toString());
//
//                View sharedView = findViewById(R.id.blueView);
//                String transitionName = getString(R.string.blue_name);
//
//                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, transitionName);
//                startActivity(i, transitionActivityOptions.toBundle());
//            }
//        });
    }

    @Override
    protected void onResume() {
        trace();
        super.onResume();
        updateView();
    }

    private SectionAndHeaderItemDecoration.ItemDecorationCallback getSectionCallback(final List<CallViewModel> apps) {
        trace();

        return new SectionAndHeaderItemDecoration.ItemDecorationCallback() {
            @Override
            public boolean isHeader(int position) {
                trace();
                log("Verify if CallViewModel has header on ItemDecorationCallback ");
                if(apps.get(position).getHasHeader())
                    return position == 0 || apps.get(position).getName().toUpperCase().charAt(0) != apps.get(position - 1).getName().toUpperCase().charAt(0);
                else
                    return false;
            }

            @Override
            public CharSequence getHeaderName(int position) {
                trace();
                log("Return header name on ItemDecorationCallback");
                if(apps.get(position).getHasHeader())
                    return apps.get(position).getName().toUpperCase().subSequence(0, 1);
                else
                    return "";
            }

            @Override
            public boolean isSection(int position) {
                trace();
                log("Verify if CallViewModel has section on ItemDecorationCallback");
                return position == 0 || !apps.get(position).getSectionName().equals(apps.get(position - 1).getSectionName());
            }

            @Override
            public CharSequence getSectionName(int position) {
                trace();
                log("Return section name on ItemDecorationCallback");
                return apps.get(position).getSectionName();
            }
        };
    }

    /**
     * Update list from content provider
     */
    private void updateView() {
        trace();
        log("Loading list of all apps on app drawer");
        List<CallViewModel> allApps = getAllApp(this);
        this.mCallListAdapter.setAppsDataList(allApps);
        addItemsDecorations(allApps);
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                trace();
                log("List loaded");
                if(isRecyclerScrollable(mRecyclerView)){
                    log("Recycler does include scroll");
                    int right = getResources().getDimensionPixelSize(R.dimen.thumb_width);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, right, 0);
                    mRecyclerView.setLayoutParams(marginLayoutParams);
                }else{
                    log("Recycler doesn't have scroll");
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
                    marginLayoutParams.setMargins(0, 0, 0, 0);
                    mRecyclerView.setLayoutParams(marginLayoutParams);
                }
                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public List<CallViewModel> getAllApp(Context context) {
        trace();
        List<CallModel> callModels = getApps();
        List<CallViewModel> allApps = assignSectionAndHeader(context, callModels);
        return allApps;
    }

    private List<CallModel> getApps(){
        List<CallModel> appsByContentProviderWithFavorites =  new ArrayList<>(
                Arrays.asList(
                        new CallModel("First Lastname", "com.package.name.A01", null, System.currentTimeMillis()),
                        new CallModel("First Lastname", "com.package.name.M10", null, System.currentTimeMillis()),
                        new CallModel("", "(000) 000-0000", null, System.currentTimeMillis()),
                        new CallModel("", "", null, null)
                )
        );
        return appsByContentProviderWithFavorites;
    }

    public List<CallViewModel> assignSectionAndHeader(Context context, List<CallModel> callModels) {
        trace();
        log("Assign section and header position to the app list");
        ArrayList<CallViewModel> assignApps = new ArrayList<>();
        for (int i = 0; i < callModels.size(); i++) {
            CallModel callModel = callModels.get(i);
            CallViewModel callViewModel = new CallViewModel();
            callViewModel.setName(callModel.nameOfUser);
            callViewModel.setPhoneNumber(callModel.phoneNumber);
            callViewModel.setIcon(callModel.icon);
            callViewModel.setFavorite(callModel.isFavorite());
            if (callModel.isFavorite()) {
                callViewModel.setSectionName(context.getString(R.string.today));
                callViewModel.setHasHeader(false);
            } else {
                callViewModel.setSectionName(context.getString(R.string.yesterday));
                callViewModel.setHasHeader(false);
            }
            if (callModel.phoneNumber == null) {
                callViewModel.setHasHeader(false);
                callViewModel.setRowType(CallViewModel.EMPTY_ROW);
            }
            assignApps.add(callViewModel);
        }
        return assignApps;
    }

    /**
     * Add section and header to RecyclerView
     */
    private void addItemsDecorations(List<CallViewModel> allApps){
        trace();
        if (mItemDecoration != null) {
            mRecyclerView.removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = new SectionAndHeaderItemDecoration(getResources().getDimensionPixelSize(R.dimen.height_section), getResources().getDimensionPixelSize(R.dimen.height_header), true, getSectionCallback(allApps));
        mRecyclerView.addItemDecoration(mItemDecoration);
    }

    public boolean isRecyclerScrollable(RecyclerView recyclerView) {
        trace();
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (layoutManager == null || adapter == null) {
            return false;
        }
        int itemsShown = layoutManager.findLastCompletelyVisibleItemPosition() - layoutManager.findFirstCompletelyVisibleItemPosition();
        boolean isScrollable = adapter.getItemCount() -1 > itemsShown ;
        mFastScroller.setVisibility(isScrollable ? View.VISIBLE : View.GONE);
        return isScrollable;
    }

}
