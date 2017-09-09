package com.hm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hm.indicator.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8/008.
 */

public class ViewPagerIndicator extends LinearLayout {


    private Paint mPaint;
    private Path mPath;
    private int mTriangleWith;
    private int mTriangleHeight;
    private final static float TRIANGLE_ON_TAB = 1 / 6F;
    private int mVisibleCount;
    private int mTabWidth;

    private int mInitTransitionX;
    private int mTransitonX;

    private final static int DEFAULT_VISIBLE_COUNT = 4;
    private final static int TEXT_COLOR_NORMAL = 0x77ffffff;
    private final static int TEXT_COLOR_HIGHTED = 0xffffffff;
    private int indicatorStayPosition;

    private List<String> mTitles;

    private ViewPager mViewPager;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * 初始化画笔
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_visibleCount, DEFAULT_VISIBLE_COUNT);
        if (mVisibleCount <= 0) {
            mVisibleCount = DEFAULT_VISIBLE_COUNT;
        }
        indicatorStayPosition = mVisibleCount % 2 == 0 ? mVisibleCount / 2 : (mVisibleCount + 1) / 2;
        a.recycle();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTransitionX + mTransitonX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w / mVisibleCount;
        mTriangleWith = (int) (mTabWidth * TRIANGLE_ON_TAB);
        mTriangleHeight = mTriangleWith / 2;
        mInitTransitionX = mTabWidth / 2 - mTriangleWith / 2;

        initTriangle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) return;
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;

            lp.width = getScrollWidth() / mVisibleCount;
            view.setLayoutParams(lp);
        }
    }


    /**
     * 初始化 path（三角形）
     */
    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWith, 0);
        mPath.lineTo(mTriangleWith / 2, -mTriangleHeight);
        mPath.close();
    }

    /**
     * 动态设置titles
     */

    public void setTitles(List<String> titles) {
        this.removeAllViews();
        if (titles != null && titles.size() > 0) {
            this.mTitles = titles;
            for (String title : titles) {
                addView(generateTextView(title));
            }
            textOnClickListener();
        }
    }

    /**
     * 设置可见个数
     *
     * @param count
     */
    public void setVisibleCount(int count) {
        this.mVisibleCount = count;
        indicatorStayPosition = mVisibleCount % 2 == 0 ? mVisibleCount / 2 : (mVisibleCount + 1) / 2;
    }

    private View generateTextView(String title) {

        TextView tv = new TextView(getContext());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        lp.weight = 0;
        lp.width = getScrollWidth() / mVisibleCount;
        tv.setTextColor(
                TEXT_COLOR_NORMAL
        );
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(lp);
        tv.setText(title);
        return tv;

    }

    private int getScrollWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void scroll(int position, float positionOffset) {
        mTransitonX = (int) (mTabWidth * (position + positionOffset));

        if (mVisibleCount != 1) {
            if (position >= (mVisibleCount - indicatorStayPosition) && getChildCount() > mVisibleCount && positionOffset > 0 && position < getChildCount() - indicatorStayPosition) {
//            if (position >= (mVisibleCount - 2) && getChildCount() > mVisibleCount && positionOffset > 0 && (getChildAt(getChildCount() - 1).getRight() != mTabWidth * getChildCount())) {
                scrollTo((int) (((position - (mVisibleCount - indicatorStayPosition)) + positionOffset) * mTabWidth), 0);
            }
        } else {
            scroll((int) (mTabWidth * (position + positionOffset)), 0);
        }
        invalidate();
    }

    public void setViewPager(ViewPager mViewPager, final int pos) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mlisterner != null) {
                    mlisterner.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                onTextHight(position);
                if (mlisterner != null) {
                    mlisterner.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mlisterner != null) {
                    mlisterner.onPageScrollStateChanged(state);
                }
            }
        });
        this.mViewPager = mViewPager;
        mViewPager.setCurrentItem(pos);
        onTextHight(pos);
    }

    private OnPageChangeListener mlisterner;

    public void setOnPagerChangeListener(OnPageChangeListener l) {
        this.mlisterner = l;
    }

    public interface OnPageChangeListener {

        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }

    private void textOnClickListener() {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            final int j = i;

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    /**
     * 选择高亮
     */
    private void onTextHight(int pos) {
        resetTextColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(TEXT_COLOR_HIGHTED);
        }

    }

    /**
     * 重置高亮
     */

    private void resetTextColor() {
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(TEXT_COLOR_NORMAL);
            }
        }

    }
}
