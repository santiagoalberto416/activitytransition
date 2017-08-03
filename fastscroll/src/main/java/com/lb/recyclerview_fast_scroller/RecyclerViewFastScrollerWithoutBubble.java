package com.lb.recyclerview_fast_scroller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class RecyclerViewFastScrollerWithoutBubble extends LinearLayout {
    private static final int BUBBLE_ANIMATION_DURATION = 100;
    private static final int TRACK_SNAP_RANGE = 5;

    private View handle;
    private View scrollbar;
    private RecyclerView recyclerView;
    private int height;
    private boolean isInitialized = false;
    private ObjectAnimator currentAnimator = null;

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
            updateBubbleAndHandlePosition();
        }
    };

    public interface BubbleTextGetter {
        String getTextToShowInBubble(int pos);
    }

    public RecyclerViewFastScrollerWithoutBubble(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public RecyclerViewFastScrollerWithoutBubble(final Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewFastScrollerWithoutBubble(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    protected void init(Context context) {
        if (isInitialized)
            return;
        isInitialized = true;
        setOrientation(HORIZONTAL);
        setClipChildren(false);
    }

    private void setViewsToUse(@LayoutRes int layoutResId, @IdRes int scrollbar, @IdRes int handleResId) {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(layoutResId, this, true);
        handle = findViewById(handleResId);
        this.scrollbar = findViewById(scrollbar);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        updateBubbleAndHandlePosition();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("ACTION HANDLE X", handle.getX() +"");
                Log.d("ACTION EVENT X", event.getX()+"");
                Log.d("ACTION PADDING", ViewCompat.getPaddingStart(handle)+"");
                if (event.getX() < scrollbar.getX() - ViewCompat.getPaddingStart(scrollbar)) {
                    Log.d("ACTION","Log Down return false");
                    return false;
                }
                if (currentAnimator != null)
                    currentAnimator.cancel();
                handle.setSelected(true);
                Log.d("ACTION","DOWN");
            case MotionEvent.ACTION_MOVE:
                final float y = event.getY();
                setBubbleAndHandlePosition(y);
                setRecyclerViewPosition(y);
                Log.d("ACTION","MOVE");
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handle.setSelected(false);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setRecyclerView(final RecyclerView recyclerView) {
        if (this.recyclerView != recyclerView) {
            if (this.recyclerView != null)
                this.recyclerView.removeOnScrollListener(onScrollListener);
            this.recyclerView = recyclerView;
            if (this.recyclerView == null)
                return;
            recyclerView.addOnScrollListener(onScrollListener);
            setViewsToUse(R.layout.fast_scroll_layout_without_bubble, R.id.scrollbar, R.id.fastscroller_handle);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(onScrollListener);
            recyclerView = null;
        }
    }

    private void setRecyclerViewPosition(float y) {
        if (recyclerView != null) {
            final int itemCount = recyclerView.getAdapter().getItemCount();
            float proportion;
            if (handle.getY() == 0)
                proportion = 0f;
            else if (handle.getY() + handle.getHeight() >= height - TRACK_SNAP_RANGE)
                proportion = 1f;
            else
                proportion = y / (float) height;
            final int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(targetPos, 0);
        }
    }

    private int getValueInRange(int min, int max, int value) {
        int minimum = Math.max(min, value);
        return Math.min(minimum, max);
    }

    private void updateBubbleAndHandlePosition() {
        if (handle.isSelected())
            return;
        final int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
        final int verticalScrollRange = recyclerView.computeVerticalScrollRange();
        float proportion = (float) verticalScrollOffset / ((float) verticalScrollRange - height);
        setBubbleAndHandlePosition(height * proportion);
    }

    private void setBubbleAndHandlePosition(float y) {
        final int handleHeight = handle.getHeight();
        handle.setY(getValueInRange(0, height - handleHeight, (int) (y - handleHeight / 2)));
    }
}
