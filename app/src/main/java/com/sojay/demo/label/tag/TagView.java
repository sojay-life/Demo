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

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.ViewDragHelper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;


public class TagView extends View {

    /*********************************************************************************************/

    //Tag圆角
    private float mRadius;

    //Tag背景色
    private int mBackgroundColor;

    //Tag选中背景色
    private int mSelectedBackgroundColor;

    //Tag字体大小
    private float mTextSize;

    //Tag字体颜色
    private int mTextColor;

    //Tag选中字体颜色
    private int mSelectedTextColor;

    //边线宽
    private float mBorderWidth;

    //边线颜色
    private int mBorderColor;

    //内容边距
    private float mTagLeftPadding;
    private float mTagTopPadding;
    private float mTagRightPadding;
    private float mTagBottomPadding;
    
    //"X"边距
    private float mCrossLeftMargin;
    private float mCrossTopMargin;
    private float mCrossRightMargin;
    private float mCrossBottomMargin;
    private float mCrossPadding;

    //Tag是否可点击
    private boolean isViewClickable;

    //Tag是否可选中
    private boolean isViewSelectable;
    

    /*********************************************************************************************/

    /** Whether this view selected*/
    private boolean isViewSelected;

    /** The max length for this tag view*/
    private int mTagMaxLength;

    /** OnTagClickListener for click action*/
    private OnTagClickListener mOnTagClickListener;

    /** Move slop(default 5dp)*/
    private int mMoveSlop = 5;

    /** Scroll slop threshold 4dp*/
    private int mSlopThreshold = 4;

    /** How long trigger long click callback(default 500ms)*/
    private int mLongPressTime = 500;

    /** Text direction(support:TEXT_DIRECTION_RTL & TEXT_DIRECTION_LTR, default TEXT_DIRECTION_LTR)*/
    private int mTextDirection = View.TEXT_DIRECTION_LTR;

    /** The distance between baseline and descent*/
    private float bdDistance;

    /** Whether to support 'letters show with RTL(eg: Android to diordnA)' style(default false)*/
    private boolean mTagSupportLettersRTL = false;

    private Paint mPaint, mRipplePaint;

    private RectF mRectF;

    private String mAbstractText, mOriginText;

    private TagBean mTagBean;

    private boolean isUp, isMoved, isExecLongClick;

    private int mLastX, mLastY;

    private float fontH, fontW;

    private float mTouchX, mTouchY;

    /** The ripple effect duration(default 1000ms)*/
    private int mRippleDuration = 1000;

    private float mRippleRadius;

    private int mRippleColor;

    private int mRippleAlpha;

    private Path mPath;

    private Typeface mTypeface;

    private ValueAnimator mRippleValueAnimator;

    private Bitmap mBitmapImage;

    private boolean mEnableCross;

    private float mCrossAreaWidth;

    private int mCrossColor;

    private float mCrossLineWidth;

    private boolean unSupportedClipPath = false;

    private Runnable mLongClickHandle = new Runnable() {
        @Override
        public void run() {
            if (!isMoved && !isUp){
                int state = ((TagContainerLayout)getParent()).getTagViewState();
                if (state == ViewDragHelper.STATE_IDLE){
                    isExecLongClick = true;
                    mOnTagClickListener.onTagLongClick((int) getTag(), getTagBean());
                }
            }
        }
    };

    public TagView(Context context, TagBean bean){
        super(context);
        init(context, bean);
    }

    public TagView(Context context, TagBean bean, int defaultImageID){
        super(context);
        init(context, bean);
        mBitmapImage = BitmapFactory.decodeResource(getResources(), defaultImageID);
    }

    private void init(Context context, TagBean bean){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRipplePaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF();
        mPath = new Path();
        mTagBean = bean;
        mOriginText = bean.getTag() == null ? "" : bean.getTag();
        mMoveSlop = (int) dp2px(context, mMoveSlop);
        mSlopThreshold = (int) dp2px(context, mSlopThreshold);
    }

    private void onDealText(){
        if(!TextUtils.isEmpty(mOriginText)) {
            mAbstractText = mOriginText.length() <= mTagMaxLength ? mOriginText
                    : mOriginText.substring(0, mTagMaxLength - 3) + "...";
        }else {
            mAbstractText = "";
        }
        mPaint.setTypeface(mTypeface);
        mPaint.setTextSize(mTextSize);
        final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        fontH = fontMetrics.descent - fontMetrics.ascent;
        if (mTagTopPadding == 0 && mTagBottomPadding == 0) {
            mTagTopPadding = mTagBottomPadding = mBorderWidth - fontH / 2;
        }
        if (mTextDirection == View.TEXT_DIRECTION_RTL){
            fontW = 0;
            for (char c : mAbstractText.toCharArray()) {
                String sc = String.valueOf(c);
                fontW += mPaint.measureText(sc);
            }
        }else {
            fontW = mPaint.measureText(mAbstractText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = (int)(mTagTopPadding + mCrossTopMargin + fontH + mCrossBottomMargin + mTagBottomPadding);
        int width = (int)((isEnableImage() ? height : mTagLeftPadding) + fontW + mTagRightPadding + (isEnableCross() ? (mCrossLeftMargin +  fontH + mCrossRightMargin) : 0));
        mCrossAreaWidth = Math.min(Math.max(mCrossAreaWidth, height), width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw background
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getIsViewSelected() ? mSelectedBackgroundColor : mBackgroundColor);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

        // draw border
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);

        // draw ripple for TagView
        drawRipple(canvas);

        // draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getIsViewSelected() ? mSelectedTextColor : mTextColor);

        if (mTextDirection == View.TEXT_DIRECTION_RTL) {
            if (mTagSupportLettersRTL){
                float tmpX = (isEnableCross() ? getWidth() + getHeight() : getWidth()) / 2
                        + fontW / 2;
                for (char c : mAbstractText.toCharArray()) {
                    String sc = String.valueOf(c);
                    tmpX -= mPaint.measureText(sc);
                    canvas.drawText(sc, tmpX, getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
                }
            }else {
                canvas.drawText(mAbstractText,
                        getWidth() - mTagRightPadding - fontW,
                        getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
            }
        } else {
            canvas.drawText(mAbstractText, mTagLeftPadding + (isEnableImage() ? getHeight() / 2 : 0), getHeight() / 2 + fontH / 2 - bdDistance, mPaint);
        }

        // draw cross
        drawCross(canvas);

        // draw image
        drawImage(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isViewClickable){
            int y = (int) event.getY();
            int x = (int) event.getX();
            int action = event.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    mLastY = y;
                    mLastX = x;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (!isViewSelected && (Math.abs(mLastY - y) > mSlopThreshold
                            || Math.abs(mLastX - x) > mSlopThreshold)){
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        isMoved = true;
                        return false;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mRippleRadius = 0.0f;
            mTouchX = event.getX();
            mTouchY = event.getY();
            splashRipple();
        }
        if (isEnableCross() && isClickCrossArea(event) && mOnTagClickListener != null){
            if (action == MotionEvent.ACTION_UP) {
                mOnTagClickListener.onTagCrossClick((int) getTag(), getTagBean());
            }
            return true;
        }else if (isViewClickable && mOnTagClickListener != null){
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    mLastY = y;
                    mLastX = x;
                    isMoved = false;
                    isUp = false;
                    isExecLongClick = false;
                    postDelayed(mLongClickHandle, mLongPressTime);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isMoved){
                        break;
                    }
                    if (Math.abs(mLastX - x) > mMoveSlop || Math.abs(mLastY - y) > mMoveSlop){
                        isMoved = true;
                        if (isViewSelected){
                            mOnTagClickListener.onSelectedTagDrag((int) getTag(), getTagBean());
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    isUp = true;
                    if (!isExecLongClick && !isMoved) {
                        mOnTagClickListener.onTagClick((int) getTag(), getTagBean());
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private boolean isClickCrossArea(MotionEvent event){
        if (mTextDirection == View.TEXT_DIRECTION_RTL){
            return event.getX() <= mCrossAreaWidth;
        }
        return event.getX() >= getWidth() - mCrossAreaWidth;
    }

    private void drawImage(Canvas canvas){
        if (isEnableImage()) {
            Bitmap scaledImageBitmap = Bitmap.createScaledBitmap(mBitmapImage, Math.round(getHeight() - mBorderWidth), Math.round(getHeight() - mBorderWidth), false);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(scaledImageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            RectF rect = new RectF(mBorderWidth, mBorderWidth, getHeight() - mBorderWidth, getHeight() - mBorderWidth);
            canvas.drawRoundRect(rect, rect.height()/2, rect.height()/2, paint);
        }
    }

    private void drawCross(Canvas canvas){
        if (isEnableCross()){
            int ltX, ltY, rbX, rbY, lbX, lbY, rtX, rtY;

            ltX = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontW - mCrossLeftMargin - mCrossPadding) :
                    (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontH + mCrossPadding);

            ltY = (int)(mTagTopPadding + mCrossTopMargin + mCrossPadding);

            rbX = mTextDirection == View.TEXT_DIRECTION_RTL ?
                    (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontW - mCrossLeftMargin - fontH + mCrossPadding) :
                    (int)(getWidth() - mTagRightPadding - mCrossRightMargin - mCrossPadding);

            rbY = (int)(mTagTopPadding + mCrossTopMargin + fontH - mCrossPadding);

            lbX = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontW - mCrossLeftMargin - fontH + mCrossPadding) :
                    (int)(getWidth() - mTagRightPadding - mCrossRightMargin - mCrossPadding);

            lbY = (int)(mTagTopPadding + mCrossTopMargin + mCrossPadding);

            rtX = mTextDirection == View.TEXT_DIRECTION_RTL ? (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontW - mCrossLeftMargin - mCrossPadding) :
                    (int)(getWidth() - mTagRightPadding - mCrossRightMargin - fontH + mCrossPadding);

            rtY = (int)(mTagTopPadding + mCrossTopMargin + fontH - mCrossPadding);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mCrossColor);
            mPaint.setStrokeWidth(mCrossLineWidth);
            canvas.drawLine(ltX, ltY, rbX, rbY, mPaint);
            canvas.drawLine(lbX, lbY, rtX, rtY, mPaint);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void drawRipple(Canvas canvas){
        if (isViewClickable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&
                canvas != null && !unSupportedClipPath){

            // Disable hardware acceleration for 'Canvas.clipPath()' when running on API from 11 to 17
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2){
                setLayerType(LAYER_TYPE_SOFTWARE, null);
            }
            try {
                canvas.save();
                mPath.reset();

                canvas.clipPath(mPath);
                mPath.addRoundRect(mRectF, mRadius, mRadius, Path.Direction.CCW);

//                bug: https://github.com/whilu/AndroidTagView/issues/88
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    canvas.clipPath(mPath);
                } else {
                    canvas.clipPath(mPath, Region.Op.REPLACE);
                }

                canvas.drawCircle(mTouchX, mTouchY, mRippleRadius, mRipplePaint);
                canvas.restore();
            }catch (UnsupportedOperationException e){
                unSupportedClipPath = true;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void splashRipple(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && mTouchX > 0 && mTouchY > 0){
            mRipplePaint.setColor(mRippleColor);
            mRipplePaint.setAlpha(mRippleAlpha);
            final float maxDis = Math.max(Math.max(Math.max(mTouchX, mTouchY),
                    Math.abs(getMeasuredWidth() - mTouchX)), Math.abs(getMeasuredHeight() - mTouchY));

            mRippleValueAnimator = ValueAnimator.ofFloat(0.0f, maxDis).setDuration(mRippleDuration);
            mRippleValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animValue = (float) animation.getAnimatedValue();
                    mRippleRadius = animValue >= maxDis ? 0 : animValue;
                    postInvalidate();
                }
            });
            mRippleValueAnimator.start();
        }
    }

    public String getText(){
        return mOriginText;
    }

    public TagBean getTagBean() {
        return mTagBean;
    }

    public boolean getIsViewSelected(){
        return isViewSelected;
    }

    public void setTagMaxLength(int maxLength){
        this.mTagMaxLength = maxLength;
        onDealText();
    }

    public void setOnTagClickListener(OnTagClickListener listener){
        this.mOnTagClickListener = listener;
    }

    //TODO change background color
    public void selectView() {
        if (isViewSelectable && !getIsViewSelected()) {
            this.isViewSelected = true;
            postInvalidate();
        }
    }

    public void deselectView() {
        if (isViewSelectable && getIsViewSelected()) {
            this.isViewSelected = false;
            postInvalidate();
        }
    }

    public interface OnTagClickListener{
        void onTagClick(int position, TagBean bean);
        void onTagLongClick(int position, TagBean bean);
        void onSelectedTagDrag(int position, TagBean bean);
        void onTagCrossClick(int position, TagBean bean);
    }

    public int getTextDirection() {
        return mTextDirection;
    }

    public void setTextDirection(int textDirection) {
        this.mTextDirection = textDirection;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        onDealText();
    }

    public void setRippleAlpha(int mRippleAlpha) {
        this.mRippleAlpha = mRippleAlpha;
    }

    public void setRippleColor(int mRippleColor) {
        this.mRippleColor = mRippleColor;
    }

    public void setRippleDuration(int mRippleDuration) {
        this.mRippleDuration = mRippleDuration;
    }

    public void setBdDistance(float bdDistance) {
        this.bdDistance = bdDistance;
    }

    public boolean isEnableImage() { return mBitmapImage != null && mTextDirection != View.TEXT_DIRECTION_RTL; }

    public boolean isEnableCross() {
        return mEnableCross;
    }

    public void setEnableCross(boolean mEnableCross) {
        this.mEnableCross = mEnableCross;
    }

    public void setCrossAreaWidth(float mCrossAreaWidth) {
        this.mCrossAreaWidth = mCrossAreaWidth;
    }

    public void setCrossLineWidth(float mCrossLineWidth) {
        this.mCrossLineWidth = mCrossLineWidth;
    }

    public void setCrossColor(int mCrossColor) {
        this.mCrossColor = mCrossColor;
    }

    public void setTagSupportLettersRTL(boolean mTagSupportLettersRTL) {
        this.mTagSupportLettersRTL = mTagSupportLettersRTL;
    }

    private float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }


    /****************************************************************************************/

    public void setTagRadius(float radius) {
        mRadius = radius;
    }

    public void setTagBackgroundColor(int color){
        this.mBackgroundColor = color;
    }

    public void setTagSelectedBackgroundColor(int color){
        this.mSelectedBackgroundColor = color;
    }

    public void setTextSize(float size) {
        this.mTextSize = size;
        onDealText();
    }

    public void setTagTextColor(int color){
        this.mTextColor = color;
    }

    public void setTagSelectedTextColor(int selectedTextColor) {
        mSelectedTextColor = selectedTextColor;
    }

    public void setTagBorderWidth(float width) {
        this.mBorderWidth = width;
    }

    public void setTagBorderColor(int color){
        this.mBorderColor = color;
    }

    public void setTagLeftPadding(float padding) {
        mTagLeftPadding = padding;
    }

    public void setTagTopPadding(float padding) {
        mTagTopPadding = padding;
    }

    public void setTagRightPadding(float padding) {
        mTagRightPadding = padding;
    }

    public void setTagBottomPadding(float padding) {
        mTagBottomPadding = padding;
    }

    public void setTagCrossLeftMargin(float margin) {
        mCrossLeftMargin = margin;
    }

    public void setTagCrossTopMargin(float margin) {
        mCrossTopMargin = margin;
    }

    public void setTagCrossRightMargin(float margin) {
        mCrossRightMargin = margin;
    }

    public void setTagCrossBottomMargin(float margin) {
        mCrossBottomMargin = margin;
    }

    public void setTagCrossPadding(float padding) {
        mCrossPadding = padding;
    }

    public void setTagIsViewClickable(boolean clickable) {
        this.isViewClickable = clickable;
    }

    public void setTagIsViewSelectable(boolean viewSelectable) {
        isViewSelectable = viewSelectable;
    }

}
