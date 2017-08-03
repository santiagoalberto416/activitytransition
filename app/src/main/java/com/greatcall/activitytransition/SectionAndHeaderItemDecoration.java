package com.greatcall.activitytransition;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greatcall.logging.Instrumented;

/**
 * Created by Savid Salazar on 6/28/17.
 */

public class SectionAndHeaderItemDecoration extends RecyclerView.ItemDecoration implements Instrumented {

    private final int             headerOffset;
    private final int             sectionOffset;
    private final boolean         sticky;
    private final ItemDecorationCallback sectionCallback;

    private View headerView, sectionView;
    private TextView header, section;

    public SectionAndHeaderItemDecoration(int sectionHeight, int headerHeight, boolean sticky, @NonNull ItemDecorationCallback sectionCallback) {
        trace();
        headerOffset = headerHeight;
        sectionOffset = sectionHeight;
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        trace();
        super.getItemOffsets(outRect, view, parent, state);
        int height = 0;
        int pos = parent.getChildAdapterPosition(view);
        if (sectionCallback.isSection(pos)) {
            height = height + sectionOffset;
        }
        if (sectionCallback.isHeader(pos)) {
            height = height + headerOffset;
        }
        outRect.top = height;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        trace();
        super.onDrawOver(c, parent, state);

        if (sectionView == null) {
            sectionView = inflateSectionView(parent);
            section = (TextView) sectionView.findViewById(R.id.txt_section_name);
            fixLayoutSize(sectionView, parent);
        }

        if (headerView == null) {
            headerView = inflateHeaderView(parent);
            header = (TextView) headerView.findViewById(R.id.txt_header_name);
            fixLayoutSize(headerView, parent);
        }

        CharSequence previousHeader = "", previousSection = "";
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);

            CharSequence headerTitle = sectionCallback.getHeaderName(position);
            header.setText(headerTitle);
            CharSequence sectionTitle = sectionCallback.getSectionName(position);
            section.setText(sectionTitle);
            if (!previousHeader.equals(headerTitle) || sectionCallback.isHeader(position)) {
                drawHeader(c, child, headerView);
                previousHeader = headerTitle;
            }
            if (!previousSection.equals(sectionTitle) || sectionCallback.isSection(position)) {
                drawSection(c, child, sectionView, sectionCallback.isHeader(position) ? headerOffset : 0);
                previousSection = sectionTitle;
            }
        }

    }

    /**
     * Draw a section layout on RecyclerView
     * @param c
     * @param child
     * @param sectionView
     * @param heigh
     */
    private void drawSection(Canvas c, View child, View sectionView, int heigh) {
        trace();
        log("draw section");
        c.save();
        c.translate(0, child.getTop() - (sectionView.getHeight() + heigh));
        sectionView.draw(c);
        c.restore();
    }

    /**
     * Draw a header layout on RecyclerView
     * @param c
     * @param child
     * @param headerView
     */
    private void drawHeader(Canvas c, View child, View headerView) {
        trace();
        c.save();
        if (sticky) {
            log("draw sticky header");
            c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
        } else {
            log("draw no sticky header");
            c.translate(0, child.getTop() - headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    /**
     * Inflate header layout
     * @param parent
     * @return View
     */
    private View inflateHeaderView(RecyclerView parent) {
        trace();
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_header, parent, false);
    }

    /***
     * inflate a section layout
     * @param parent
     * @return View
     */
    private View inflateSectionView(RecyclerView parent) {
        trace();
        return LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_list_section, parent, false);
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     */
    private void fixLayoutSize(View view, ViewGroup parent) {
        trace();
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height);

        view.measure(childWidth, childHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public interface ItemDecorationCallback {

        boolean isHeader(int position);

        CharSequence getHeaderName(int position);

        boolean isSection(int position);

        CharSequence getSectionName(int position);
    }
}