/*
 * Copyright 2015 lujun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sojay.demo.label.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sojay.demo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-30 17:14
 */
public class TagContainerLayout extends ViewGroup {

    /**
     * Vertical interval, default 5(dp)
     */
    private int mVerticalInterval;

    /**
     * Horizontal interval, default 5(dp)
     */
    private int mHorizontalInterval;

    /**
     * TagContainerLayout border width(default 0.5dp)
     */
    private float mBorderWidth = 0.5f;

    /**
     * TagContainerLayout border radius(default 10.0dp)
     */
    private float mBorderRadius = 10.0f;

    /**
     * The sensitive of the ViewDragHelper(default 1.0f, normal)
     */
    private float mSensitivity = 1.0f;

    /**
     * TagView average height
     */
    private int mChildHeight;

    /**
     * TagContainerLayout border color(default #00000000)
     */
    private int mBorderColor = Color.parseColor("#00000000");

    /**
     * TagContainerLayout background color(default #ffffff)
     */
    private int mBackgroundColor = Color.parseColor("#ffffff");

    /**
     * The container layout gravity(default left)
     */
    private int mGravity = Gravity.LEFT;

    /**
     * The max line count of TagContainerLayout
     */
    private int mMaxLines = 0;

    /**
     * The max length for TagView(default max length 23)
     */
    private int mTagMaxLength = 23;

    /**
     * TagView Border width(default 0.5dp)
     */
    private float mTagBorderWidth = 0.5f;

    /**
     * TagView Border radius(default 15.0dp)
     */
    private float mTagBorderRadius = 15.0f;

    /**
     * TagView Text size(default 14sp)
     */
    private float mTagTextSize = 14;

    /**
     * Text direction(support:TEXT_DIRECTION_RTL & TEXT_DIRECTION_LTR, default TEXT_DIRECTION_LTR)
     */
    private int mTagTextDirection = View.TEXT_DIRECTION_LTR;

    /**
     * TagView border color(default #88F44336)
     */
    private int mTagBorderColor = Color.parseColor("#88F44336");

    /**
     * TagView background color(default #33F44336)
     */
    private int mTagBackgroundColor = Color.parseColor("#33F44336");

    /**
     * Selected TagView background color(default #33FF7669)
     */
    private int mSelectedTagBackgroundColor = Color.parseColor("#33FF7669");

    /**
     * TagView text color(default #FF666666)
     */
    private int mTagTextColor = Color.parseColor("#FF666666");

    private int mSelectedTagTextColor = Color.parseColor("#FF666666");

    /**
     * TagView typeface
     */
    private Typeface mTagTypeface = Typeface.DEFAULT;

    /**
     * Whether TagView can clickable(default unclickable)
     */
    private boolean isTagViewClickable;

    /**
     * Whether TagView can selectable(default unselectable)
     */
    private boolean isTagViewSelectable;

    /**
     * Tags
     */
    private List<TagBean> mTags;

    /**
     * Default image for new tags
     */
    private int mDefaultImageDrawableID = -1;

    /**
     * Can drag TagView(default false)
     */
    private boolean mDragEnable;

    /**
     * TagView drag state(default STATE_IDLE)
     */
    private int mTagViewState = ViewDragHelper.STATE_IDLE;

    /**
     * The distance between baseline and descent(default 2.75dp)
     */
    private float mTagBdDistance = 2.75f;

    /**
     * OnTagClickListener for TagView
     */
    private TagView.OnTagClickListener mOnTagClickListener;

    /**
     * Whether to support 'letters show with RTL(eg: Android to diordnA)' style(default false)
     */
    private boolean mTagSupportLettersRTL = false;

    private Paint mPaint;

    private RectF mRectF;

    private ViewDragHelper mViewDragHelper;

    private List<View> mChildViews;

    private int[] mViewPos;

    /**
     * View theme(default PURE_CYAN)
     */
    private int mTheme = ColorFactory.PURE_CYAN;

    /**
     * Default interval(dp)
     */
    private static final float DEFAULT_INTERVAL = 5;

    /**
     * Default tag min length
     */
    private static final int TAG_MIN_LENGTH = 3;

    /**
     * The ripple effect duration(In milliseconds, default 1000ms)
     */
    private int mRippleDuration = 1000;

    /**
     * The ripple effect color(default #EEEEEE)
     */
    private int mRippleColor;

    /**
     * The ripple effect color alpha(the value may between 0 - 255, default 128)
     */
    private int mRippleAlpha = 128;

    /**
     * Enable draw cross icon(default false)
     */
    private boolean mEnableCross = false;

    /**
     * The cross area width(your cross click area, default equal to the TagView's height)
     */
    private float mCrossAreaWidth = 0.0f;

    /**
     * The padding of the cross area(default 10dp)
     */
    private float mCrossAreaPadding = 10.0f;

    /**
     * The cross icon color(default Color.BLACK)
     */
    private int mCrossColor = Color.BLACK;

    /**
     * The cross line width(default 1dp)
     */
    private float mCrossLineWidth = 1.0f;

    /**
     * TagView background resource
     */
    private int mTagBackgroundResource;
    private float mCrossLeftMargin = 3.0f;
    private float mCrossTopMargin;
    private float mCrossRightMargin;
    private float mCrossBottomMargin;
    private float mCrossPadding = 4.0f;

    private float mBorderLeftPadding = 20.0f;
    private float mBorderTopPadding = 9.0f;
    private float mBorderRightPadding = 20.0f;
    private float mBorderBottomPadding = 9.0f;


    public TagContainerLayout(Context context) {
        this(context, null);
    }

    public TagContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AndroidTagView,
                defStyleAttr, 0);
        mVerticalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_vertical_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mHorizontalInterval = (int) attributes.getDimension(R.styleable.AndroidTagView_horizontal_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_container_border_width,
                dp2px(context, mBorderWidth));
        mBorderRadius = attributes.getDimension(R.styleable.AndroidTagView_container_border_radius,
                dp2px(context, mBorderRadius));
        mTagBdDistance = attributes.getDimension(R.styleable.AndroidTagView_tag_bd_distance,
                dp2px(context, mTagBdDistance));
        mBorderColor = attributes.getColor(R.styleable.AndroidTagView_container_border_color,
                mBorderColor);
        mBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_container_background_color,
                mBackgroundColor);
        mDragEnable = attributes.getBoolean(R.styleable.AndroidTagView_container_enable_drag, false);
        mSensitivity = attributes.getFloat(R.styleable.AndroidTagView_container_drag_sensitivity,
                mSensitivity);
        mGravity = attributes.getInt(R.styleable.AndroidTagView_container_gravity, mGravity);
        mMaxLines = attributes.getInt(R.styleable.AndroidTagView_container_max_lines, mMaxLines);
        mTagMaxLength = attributes.getInt(R.styleable.AndroidTagView_tag_max_length, mTagMaxLength);
        mTheme = attributes.getInt(R.styleable.AndroidTagView_tag_theme, mTheme);
        mTagBorderWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_border_width,
                dp2px(context, mTagBorderWidth));
        mTagBorderRadius = attributes.getDimension(
                R.styleable.AndroidTagView_tag_corner_radius, dp2px(context, mTagBorderRadius));
        mTagTextSize = attributes.getDimension(R.styleable.AndroidTagView_tag_text_size,
                sp2px(context, mTagTextSize));
        mTagBorderColor = attributes.getColor(R.styleable.AndroidTagView_tag_border_color,
                mTagBorderColor);
        mTagBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_background_color,
                mTagBackgroundColor);
        mSelectedTagBackgroundColor = attributes.getColor(R.styleable.AndroidTagView_tag_select_background_color,
                mSelectedTagBackgroundColor);
        mTagTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_text_color, mTagTextColor);
        mSelectedTagTextColor = attributes.getColor(R.styleable.AndroidTagView_tag_select_text_color, mSelectedTagTextColor);
        mTagTextDirection = attributes.getInt(R.styleable.AndroidTagView_tag_text_direction, mTagTextDirection);
        isTagViewClickable = attributes.getBoolean(R.styleable.AndroidTagView_tag_clickable, false);
        isTagViewSelectable = attributes.getBoolean(R.styleable.AndroidTagView_tag_selectable, false);
        mRippleColor = attributes.getColor(R.styleable.AndroidTagView_tag_ripple_color, Color.parseColor("#EEEEEE"));
        mRippleAlpha = attributes.getInteger(R.styleable.AndroidTagView_tag_ripple_alpha, mRippleAlpha);
        mRippleDuration = attributes.getInteger(R.styleable.AndroidTagView_tag_ripple_duration, mRippleDuration);
        mEnableCross = attributes.getBoolean(R.styleable.AndroidTagView_tag_enable_cross, mEnableCross);
        mCrossAreaWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_width,
                dp2px(context, mCrossAreaWidth));
        mCrossAreaPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_area_padding,
                dp2px(context, mCrossAreaPadding));
        mCrossColor = attributes.getColor(R.styleable.AndroidTagView_tag_cross_color, mCrossColor);
        mCrossLineWidth = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_line_width,
                dp2px(context, mCrossLineWidth));
        mTagSupportLettersRTL = attributes.getBoolean(R.styleable.AndroidTagView_tag_support_letters_rlt,
                mTagSupportLettersRTL);
        mTagBackgroundResource = attributes.getResourceId(R.styleable.AndroidTagView_tag_background,
                mTagBackgroundResource);


        mBorderLeftPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_border_left_padding, dp2px(context, mBorderLeftPadding));
        mBorderTopPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_border_top_padding, dp2px(context, mBorderTopPadding));
        mBorderRightPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_border_right_padding, dp2px(context, mBorderRightPadding));
        mBorderBottomPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_border_bottom_padding, dp2px(context, mBorderBottomPadding));
        mCrossPadding = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_padding, dp2px(context, mCrossPadding));
        mCrossLeftMargin = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_left_margin, dp2px(context, mCrossLeftMargin));
        mCrossTopMargin = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_top_margin, dp2px(context, mCrossTopMargin));
        mCrossRightMargin = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_right_margin, dp2px(context, mCrossRightMargin));
        mCrossBottomMargin = attributes.getDimension(R.styleable.AndroidTagView_tag_cross_bottom_margin, dp2px(context, mCrossBottomMargin));


        attributes.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mChildViews = new ArrayList<>();
        mViewDragHelper = ViewDragHelper.create(this, mSensitivity, new DragHelperCallBack());
        setWillNotDraw(false);
        setTagMaxLength(mTagMaxLength);
        if (isInEditMode()) {
            addTag(new TagBean("1", "星球大战"));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        int lines = childCount == 0 ? 0 : getChildLines(childCount);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (mVerticalInterval + mChildHeight) * lines
                    - mVerticalInterval + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount;
        if ((childCount = getChildCount()) <= 0) {
            return;
        }
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int curRight = getMeasuredWidth() - getPaddingRight();
        int curTop = getPaddingTop();
        int curLeft = getPaddingLeft();
        int sPos = 0;
        mViewPos = new int[childCount * 2];

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                if (mGravity == Gravity.RIGHT) {
                    if (curRight - width < getPaddingLeft()) {
                        curRight = getMeasuredWidth() - getPaddingRight();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curRight - width;
                    mViewPos[i * 2 + 1] = curTop;
                    curRight -= width + mHorizontalInterval;
                } else if (mGravity == Gravity.CENTER) {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        int leftW = getMeasuredWidth() - mViewPos[(i - 1) * 2]
                                - getChildAt(i - 1).getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < i; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                        sPos = i;
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;

                    if (i == childCount - 1) {
                        int leftW = getMeasuredWidth() - mViewPos[i * 2]
                                - childView.getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < childCount; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                    }
                } else {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;
                }
            }
        }

        // layout all child views
        for (int i = 0; i < mViewPos.length / 2; i++) {
            View childView = getChildAt(i);
            childView.layout(mViewPos[i * 2], mViewPos[i * 2 + 1],
                    mViewPos[i * 2] + childView.getMeasuredWidth(),
                    mViewPos[i * 2 + 1] + mChildHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            requestLayout();
        }
    }

    private int getChildLines(int childCount) {
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + mHorizontalInterval;
            int height = childView.getMeasuredHeight();
            mChildHeight = i == 0 ? height : Math.min(mChildHeight, height);
            curLineW += dis;
            if (curLineW - mHorizontalInterval > availableW) {
                lines++;
                curLineW = dis;
            }
        }

        return mMaxLines <= 0 ? lines : mMaxLines;
    }

    private void onSetTag() {
        if (mTags == null) {
            throw new RuntimeException("NullPointer exception!");
        }
        removeAllTags();
        if (mTags.size() == 0) {
            return;
        }
        for (int i = 0; i < mTags.size(); i++) {
            onAddTag(mTags.get(i), mChildViews.size());
        }
        postInvalidate();
    }

    private void onAddTag(TagBean bean, int position) {
        if (position < 0 || position > mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        TagView tagView;
        if (mDefaultImageDrawableID != -1) {
            tagView = new TagView(getContext(), bean, mDefaultImageDrawableID);
        } else {
            tagView = new TagView(getContext(), bean);
        }
        initTagView(tagView);
        mChildViews.add(position, tagView);
        if (position < mChildViews.size()) {
            for (int i = position; i < mChildViews.size(); i++) {
                mChildViews.get(i).setTag(i);
            }
        } else {
            tagView.setTag(position);
        }
        addView(tagView, position);
    }

    private void initTagView(TagView tagView) {
        tagView.setTagBackgroundColor(mTagBackgroundColor);
        tagView.setTagSelectedBackgroundColor(mSelectedTagBackgroundColor);
        tagView.setTagBorderColor(mBorderColor);
        tagView.setTagTextColor(mTagTextColor);
        tagView.setTagSelectedTextColor(mSelectedTagTextColor);
        tagView.setTagMaxLength(mTagMaxLength);
        tagView.setTextDirection(mTagTextDirection);
        tagView.setTypeface(mTagTypeface);
        tagView.setTagBorderWidth(mTagBorderWidth);
        tagView.setTagRadius(mTagBorderRadius);
        tagView.setTextSize(mTagTextSize);
        tagView.setTagIsViewClickable(isTagViewClickable);
        tagView.setTagIsViewSelectable(isTagViewSelectable);
        tagView.setBdDistance(mTagBdDistance);
        tagView.setOnTagClickListener(mOnTagClickListener);
        tagView.setRippleAlpha(mRippleAlpha);
        tagView.setRippleColor(mRippleColor);
        tagView.setRippleDuration(mRippleDuration);
        tagView.setEnableCross(mEnableCross);
        tagView.setCrossAreaWidth(mCrossAreaWidth);
        tagView.setCrossColor(mCrossColor);
        tagView.setCrossLineWidth(mCrossLineWidth);
        tagView.setTagSupportLettersRTL(mTagSupportLettersRTL);
        tagView.setBackgroundResource(mTagBackgroundResource);
        tagView.setTagLeftPadding(mBorderLeftPadding);
        tagView.setTagTopPadding(mBorderTopPadding);
        tagView.setTagRightPadding(mBorderRightPadding);
        tagView.setTagBottomPadding(mBorderBottomPadding);
        tagView.setTagCrossLeftMargin(mCrossLeftMargin);
        tagView.setTagCrossTopMargin(mCrossTopMargin);
        tagView.setTagCrossRightMargin(mCrossRightMargin);
        tagView.setTagCrossBottomMargin(mCrossBottomMargin);
        tagView.setTagCrossPadding(mCrossPadding);
    }

    private void invalidateTags() {
        for (View view : mChildViews) {
            final TagView tagView = (TagView) view;
            tagView.setOnTagClickListener(mOnTagClickListener);
        }
    }

    private void onRemoveTag(int position) {
        if (position < 0 || position >= mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        mChildViews.remove(position);
        removeViewAt(position);
        for (int i = position; i < mChildViews.size(); i++) {
            mChildViews.get(i).setTag(i);
        }
        // TODO, make removed view null?
    }

    private void onRemoveConsecutiveTags(List<Integer> positions) {
        int smallestPosition = Collections.min(positions);
        for (int position : positions) {
            if (position < 0 || position >= mChildViews.size()) {
                throw new RuntimeException("Illegal position!");
            }
            mChildViews.remove(smallestPosition);
            removeViewAt(smallestPosition);
        }
        for (int i = smallestPosition; i < mChildViews.size(); i++) {
            mChildViews.get(i).setTag(i);
        }
        // TODO, make removed view null?
    }

    private int[] onGetNewPosition(View view) {
        int left = view.getLeft();
        int top = view.getTop();
        int bestMatchLeft = mViewPos[(int) view.getTag() * 2];
        int bestMatchTop = mViewPos[(int) view.getTag() * 2 + 1];
        int tmpTopDis = Math.abs(top - bestMatchTop);
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (Math.abs(top - mViewPos[i * 2 + 1]) < tmpTopDis) {
                bestMatchTop = mViewPos[i * 2 + 1];
                tmpTopDis = Math.abs(top - mViewPos[i * 2 + 1]);
            }
        }
        int rowChildCount = 0;
        int tmpLeftDis = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (mViewPos[i * 2 + 1] == bestMatchTop) {
                if (rowChildCount == 0) {
                    bestMatchLeft = mViewPos[i * 2];
                    tmpLeftDis = Math.abs(left - bestMatchLeft);
                } else {
                    if (Math.abs(left - mViewPos[i * 2]) < tmpLeftDis) {
                        bestMatchLeft = mViewPos[i * 2];
                        tmpLeftDis = Math.abs(left - bestMatchLeft);
                    }
                }
                rowChildCount++;
            }
        }
        return new int[]{bestMatchLeft, bestMatchTop};
    }

    private int onGetCoordinateReferPos(int left, int top) {
        int pos = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (left == mViewPos[i * 2] && top == mViewPos[i * 2 + 1]) {
                pos = i;
            }
        }
        return pos;
    }

    private void onChangeView(View view, int newPos, int originPos) {
        mChildViews.remove(originPos);
        mChildViews.add(newPos, view);
        for (View child : mChildViews) {
            child.setTag(mChildViews.indexOf(child));
        }

        removeViewAt(originPos);
        addView(view, newPos);
    }

    private int ceilTagBorderWidth() {
        return (int) Math.ceil(mTagBorderWidth);
    }

    private class DragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            mTagViewState = state;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            requestDisallowInterceptTouchEvent(true);
            return mDragEnable;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftX = getPaddingLeft();
            final int rightX = getWidth() - child.getWidth() - getPaddingRight();
            return Math.min(Math.max(left, leftX), rightX);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topY = getPaddingTop();
            final int bottomY = getHeight() - child.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topY), bottomY);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            requestDisallowInterceptTouchEvent(false);
            int[] pos = onGetNewPosition(releasedChild);
            int posRefer = onGetCoordinateReferPos(pos[0], pos[1]);
            onChangeView(releasedChild, posRefer, (int) releasedChild.getTag());
            mViewDragHelper.settleCapturedViewAt(pos[0], pos[1]);
            invalidate();
        }
    }

    /**
     * Get current drag view state.
     *
     * @return
     */
    public int getTagViewState() {
        return mTagViewState;
    }

    /**
     * Get TagView text baseline and descent distance.
     *
     * @return
     */
    public float getTagBdDistance() {
        return mTagBdDistance;
    }

    /**
     * Set TagView text baseline and descent distance.
     *
     * @param tagBdDistance
     */
    public void setTagBdDistance(float tagBdDistance) {
        this.mTagBdDistance = dp2px(getContext(), tagBdDistance);
    }

    /**
     * Set tags
     *
     * @param tags
     */
    public void setTags(List<TagBean> tags) {
        mTags = tags;
        onSetTag();
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the end.
     *
     * @param bean
     */
    public void addTag(TagBean bean) {
        addTag(bean, mChildViews.size());
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the specified location.
     * The TagView is inserted before the current element at the specified location.
     *
     * @param bean
     * @param position
     */
    public void addTag(TagBean bean, int position) {
        onAddTag(bean, position);
        postInvalidate();
    }

    /**
     * Remove a TagView in specified position.
     *
     * @param position
     */
    public void removeTag(int position) {
        onRemoveTag(position);
        postInvalidate();
    }

    /**
     * Remove TagView in multiple consecutive positions.
     *
     *
     */
    public void removeConsecutiveTags(List<Integer> positions) {
        onRemoveConsecutiveTags(positions);
        postInvalidate();
    }

    /**
     * Remove all TagViews.
     */
    public void removeAllTags() {
        mChildViews.clear();
        removeAllViews();
        postInvalidate();
    }

    /**
     * Set OnTagClickListener for TagView.
     *
     * @param listener
     */
    public void setOnTagClickListener(TagView.OnTagClickListener listener) {
        mOnTagClickListener = listener;
        invalidateTags();
    }

    /**
     * Toggle select a tag
     *
     * @param position
     */
    public void toggleSelectTagView(int position) {
        if (isTagViewSelectable){
            TagView tagView = ((TagView)mChildViews.get(position));
            if (tagView.getIsViewSelected()){
                tagView.deselectView();
            } else {
                tagView.selectView();
            }
        }
    }

    /**
     * Select a tag
     *
     * @param position
     */
    public void selectTagView(int position) {
        if (isTagViewSelectable)
            ((TagView)mChildViews.get(position)).selectView();
    }

    /**
     * Deselect a tag
     *
     * @param position
     */
    public void deselectTagView(int position) {
        if (isTagViewSelectable)
            ((TagView)mChildViews.get(position)).deselectView();
    }

    /**
     * Return selected TagView positions
     *
     * @return list of selected positions
     */
    public List<Integer> getSelectedTagViewPositions() {
        List<Integer> selectedPositions = new ArrayList<>();
        for (int i = 0; i < mChildViews.size(); i++){
            if (((TagView)mChildViews.get(i)).getIsViewSelected()){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }

    /**
     * Return selected TagView text
     *
     * @return list of selected tag text
     */
    public List<String> getSelectedTagViewText() {
        List<String> selectedTagText = new ArrayList<>();
        for (int i = 0; i < mChildViews.size(); i++){
            TagView tagView = (TagView)mChildViews.get(i);
            if ((tagView.getIsViewSelected())){
                selectedTagText.add(tagView.getText());
            }
        }
        return selectedTagText;
    }

    /**
     * Return number of child tags
     *
     * @return size
     */
    public int size() {
        return mChildViews.size();
    }

    /**
     * Get TagView text.
     *
     * @param position
     * @return
     */
    public String getTagText(int position) {
        return ((TagView) mChildViews.get(position)).getText();
    }

    /**
     * Get a string list for all tags in TagContainerLayout.
     *
     * @return
     */
    public List<String> getTags() {
        List<String> tmpList = new ArrayList<String>();
        for (View view : mChildViews) {
            if (view instanceof TagView) {
                tmpList.add(((TagView) view).getText());
            }
        }
        return tmpList;
    }

    /**
     * Set whether the child view can be dragged.
     *
     * @param enable
     */
    public void setDragEnable(boolean enable) {
        this.mDragEnable = enable;
    }

    /**
     * Get current view is drag enable attribute.
     *
     * @return
     */
    public boolean getDragEnable() {
        return mDragEnable;
    }

    /**
     * Set vertical interval
     *
     * @param interval
     */
    public void setVerticalInterval(float interval) {
        mVerticalInterval = (int) dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Get vertical interval in this view.
     *
     * @return
     */
    public int getVerticalInterval() {
        return mVerticalInterval;
    }

    /**
     * Set horizontal interval.
     *
     * @param interval
     */
    public void setHorizontalInterval(float interval) {
        mHorizontalInterval = (int) dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Get horizontal interval in this view.
     *
     * @return
     */
    public int getHorizontalInterval() {
        return mHorizontalInterval;
    }

    /**
     * Get TagContainerLayout border width.
     *
     * @return
     */
    public float getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * Set TagContainerLayout border width.
     *
     * @param width
     */
    public void setBorderWidth(float width) {
        this.mBorderWidth = width;
    }

    /**
     * Get TagContainerLayout border radius.
     *
     * @return
     */
    public float getBorderRadius() {
        return mBorderRadius;
    }

    /**
     * Set TagContainerLayout border radius.
     *
     * @param radius
     */
    public void setBorderRadius(float radius) {
        this.mBorderRadius = radius;
    }

    /**
     * Get TagContainerLayout border color.
     *
     * @return
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * Set TagContainerLayout border color.
     *
     * @param color
     */
    public void setBorderColor(int color) {
        this.mBorderColor = color;
    }

    /**
     * Get TagContainerLayout background color.
     *
     * @return
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Set TagContainerLayout background color.
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
    }

    /**
     * Get container layout gravity.
     *
     * @return
     */
    public int getGravity() {
        return mGravity;
    }

    /**
     * Set container layout gravity.
     *
     * @param gravity
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * Get TagContainerLayout ViewDragHelper sensitivity.
     *
     * @return
     */
    public float getSensitivity() {
        return mSensitivity;
    }

    /**
     * Set TagContainerLayout ViewDragHelper sensitivity.
     *
     * @param sensitivity
     */
    public void setSensitivity(float sensitivity) {
        this.mSensitivity = sensitivity;
    }

    /**
     * Get default tag image
     *
     * @return
     */
    public int getDefaultImageDrawableID() {
        return mDefaultImageDrawableID;
    }

    /**
     * Set default image for tags.
     *
     * @param imageID
     */
    public void setDefaultImageDrawableID(int imageID) {
        this.mDefaultImageDrawableID = imageID;
    }

    /**
     * Set max line count for TagContainerLayout
     *
     * @param maxLines max line count
     */
    public void setMaxLines(int maxLines) {
        mMaxLines = maxLines;
        postInvalidate();
    }

    /**
     * Get TagContainerLayout's max lines
     *
     * @return maxLines
     */
    public int getMaxLines() {
        return mMaxLines;
    }

    /**
     * Set the TagView text max length(must greater or equal to 3).
     *
     * @param maxLength
     */
    public void setTagMaxLength(int maxLength) {
        mTagMaxLength = maxLength < TAG_MIN_LENGTH ? TAG_MIN_LENGTH : maxLength;
    }

    /**
     * Get TagView max length.
     *
     * @return
     */
    public int getTagMaxLength() {
        return mTagMaxLength;
    }

    /**
     * Set TagView theme.
     *
     * @param theme
     */
    public void setTheme(int theme) {
        mTheme = theme;
    }

    /**
     * Get TagView theme.
     *
     * @return
     */
    public int getTheme() {
        return mTheme;
    }

    /**
     * Get TagView is clickable.
     *
     * @return
     */
    public boolean getIsTagViewClickable() {
        return isTagViewClickable;
    }

    /**
     * Set TagView is clickable
     *
     * @param clickable
     */
    public void setIsTagViewClickable(boolean clickable) {
        this.isTagViewClickable = clickable;
    }

    /**
     * Get TagView is selectable.
     *
     * @return
     */
    public boolean getIsTagViewSelectable() {
        return isTagViewSelectable;
    }

    /**
     * Set TagView is selectable
     *
     * @param selectable
     */
    public void setIsTagViewSelectable(boolean selectable) {
        this.isTagViewSelectable= selectable;
    }

    /**
     * Get TagView border width.
     *
     * @return
     */
    public float getTagBorderWidth() {
        return mTagBorderWidth;
    }

    /**
     * Set TagView border width.
     *
     * @param width
     */
    public void setTagBorderWidth(float width) {
        this.mTagBorderWidth = width;
    }

    /**
     * Get TagView border radius.
     *
     * @return
     */
    public float getTagBorderRadius() {
        return mTagBorderRadius;
    }

    /**
     * Set TagView border radius.
     *
     * @param radius
     */
    public void setTagBorderRadius(float radius) {
        this.mTagBorderRadius = radius;
    }

    /**
     * Get TagView text size.
     *
     * @return
     */
    public float getTagTextSize() {
        return mTagTextSize;
    }

    /**
     * Set TagView text size.
     *
     * @param size
     */
    public void setTagTextSize(float size) {
        this.mTagTextSize = size;
    }
    /**
     * Get TagView border color.
     *
     * @return
     */
    public int getTagBorderColor() {
        return mTagBorderColor;
    }

    /**
     * Set TagView border color.
     *
     * @param color
     */
    public void setTagBorderColor(int color) {
        this.mTagBorderColor = color;
    }

    /**
     * Get TagView background color.
     *
     * @return
     */
    public int getTagBackgroundColor() {
        return mTagBackgroundColor;
    }

    /**
     * Set TagView background color.
     *
     * @param color
     */
    public void setTagBackgroundColor(int color) {
        this.mTagBackgroundColor = color;
    }

    /**
     * Get TagView text color.
     *
     * @return
     */
    public int getTagTextColor() {
        return mTagTextColor;
    }

    /**
     * Set tag text direction, support:View.TEXT_DIRECTION_RTL and View.TEXT_DIRECTION_LTR,
     * default View.TEXT_DIRECTION_LTR
     *
     * @param textDirection
     */
    public void setTagTextDirection(int textDirection) {
        this.mTagTextDirection = textDirection;
    }

    /**
     * Get TagView typeface.
     *
     * @return
     */
    public Typeface getTagTypeface() {
        return mTagTypeface;
    }

    /**
     * Set TagView typeface.
     *
     * @param typeface
     */
    public void setTagTypeface(Typeface typeface) {
        this.mTagTypeface = typeface;
    }

    /**
     * Get tag text direction
     *
     * @return
     */
    public int getTagTextDirection() {
        return mTagTextDirection;
    }

    /**
     * Set TagView text color.
     *
     * @param color
     */
    public void setTagTextColor(int color) {
        this.mTagTextColor = color;
    }

    /**
     * Get the ripple effect color's alpha.
     *
     * @return
     */
    public int getRippleAlpha() {
        return mRippleAlpha;
    }

    /**
     * Set TagView ripple effect alpha, the value may between 0 to 255, default is 128.
     *
     * @param mRippleAlpha
     */
    public void setRippleAlpha(int mRippleAlpha) {
        this.mRippleAlpha = mRippleAlpha;
    }

    /**
     * Get the ripple effect color.
     *
     * @return
     */
    public int getRippleColor() {
        return mRippleColor;
    }

    /**
     * Set TagView ripple effect color.
     *
     * @param mRippleColor
     */
    public void setRippleColor(int mRippleColor) {
        this.mRippleColor = mRippleColor;
    }

    /**
     * Get the ripple effect duration.
     *
     * @return
     */
    public int getRippleDuration() {
        return mRippleDuration;
    }

    /**
     * Set TagView ripple effect duration, default is 1000ms.
     *
     * @param mRippleDuration
     */
    public void setRippleDuration(int mRippleDuration) {
        this.mRippleDuration = mRippleDuration;
    }

    /**
     * Set TagView cross color.
     *
     * @return
     */
    public int getCrossColor() {
        return mCrossColor;
    }

    /**
     * Set TagView cross color, default Color.BLACK.
     *
     * @param mCrossColor
     */
    public void setCrossColor(int mCrossColor) {
        this.mCrossColor = mCrossColor;
    }

    /**
     * Get agView cross area's padding.
     *
     * @return
     */
    public float getCrossAreaPadding() {
        return mCrossAreaPadding;
    }

    /**
     * Set TagView cross area padding, default 10dp.
     *
     * @param mCrossAreaPadding
     */
    public void setCrossAreaPadding(float mCrossAreaPadding) {
        this.mCrossAreaPadding = mCrossAreaPadding;
    }

    /**
     * Get is the TagView's cross enable, default false.
     *
     * @return
     */
    public boolean isEnableCross() {
        return mEnableCross;
    }

    /**
     * Enable or disable the TagView's cross.
     *
     * @param mEnableCross
     */
    public void setEnableCross(boolean mEnableCross) {
        this.mEnableCross = mEnableCross;
    }

    /**
     * Get TagView cross area width.
     *
     * @return
     */
    public float getCrossAreaWidth() {
        return mCrossAreaWidth;
    }

    /**
     * Set TagView area width.
     *
     * @param mCrossAreaWidth
     */
    public void setCrossAreaWidth(float mCrossAreaWidth) {
        this.mCrossAreaWidth = mCrossAreaWidth;
    }

    /**
     * Get TagView cross line width.
     *
     * @return
     */
    public float getCrossLineWidth() {
        return mCrossLineWidth;
    }

    /**
     * Set TagView cross line width, default 1dp.
     *
     * @param mCrossLineWidth
     */
    public void setCrossLineWidth(float mCrossLineWidth) {
        this.mCrossLineWidth = mCrossLineWidth;
    }

    /**
     * Get the 'letters show with RTL(like: Android to diordnA)' style if it's enabled
     *
     * @return
     */
    public boolean isTagSupportLettersRTL() {
        return mTagSupportLettersRTL;
    }

    /**
     * Set whether the 'support letters show with RTL(like: Android to diordnA)' style is enabled.
     *
     * @param mTagSupportLettersRTL
     */
    public void setTagSupportLettersRTL(boolean mTagSupportLettersRTL) {
        this.mTagSupportLettersRTL = mTagSupportLettersRTL;
    }

    /**
     * Get TagView in specified position.
     *
     * @param position the position of the TagView
     * @return
     */
    public TagView getTagView(int position){
        if (position < 0 || position >= mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        return (TagView) mChildViews.get(position);
    }

    /**
     * Get TagView background resource
     * @return
     */
    public int getTagBackgroundResource() {
        return mTagBackgroundResource;
    }

    /**
     * Set TagView background resource
     * @param tagBackgroundResource
     */
    public void setTagBackgroundResource(@DrawableRes int tagBackgroundResource) {
        this.mTagBackgroundResource = tagBackgroundResource;
    }

    private float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    private float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
