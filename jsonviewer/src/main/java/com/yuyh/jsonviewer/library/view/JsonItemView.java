package com.yuyh.jsonviewer.library.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuyh.jsonviewer.library.R;
import com.yuyh.jsonviewer.library.adapter.BaseJsonViewerAdapter;

/**
 * Created by yuyuhang on 2017/11/29.
 */
public class JsonItemView extends LinearLayout {

    public static int TEXT_SIZE_DP = 12;

    private Context mContext;

    private TextView mTvLeft, mTvRight;
    private TextView mTvIcon;

    OnClickListener mListener;

    // 默认为展开状态，默认的展开状态是否赋值给view过了
    private boolean mDefaultExpandHasBeenApplied = false;

    public JsonItemView(Context context) {
        this(context, null);
    }

    public JsonItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsonItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        LayoutInflater.from(mContext).inflate(R.layout.layout_json_item_view, this, true);

        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mTvIcon = findViewById(R.id.tv_icon);

        if (TEXT_SIZE_DP < 10) {
            TEXT_SIZE_DP = 10;
        } else if (TEXT_SIZE_DP > 30) {
            TEXT_SIZE_DP = 30;
        }

        mTvLeft.setTextSize(TEXT_SIZE_DP);
        mTvRight.setTextSize(TEXT_SIZE_DP);
        mTvRight.setTextColor(BaseJsonViewerAdapter.BRACES_COLOR);
        mTvIcon.setTextSize(TEXT_SIZE_DP - 4);
        mTvLeft.setTextIsSelectable(true);
        mTvRight.setTextIsSelectable(true);
        mDefaultExpandHasBeenApplied = false;
    }

    public void hideLeft() {
        mTvLeft.setVisibility(GONE);
    }

    public void showLeft(CharSequence text) {
        mTvLeft.setVisibility(VISIBLE);
        if (text != null) {
            mTvLeft.setText(text);
        }
    }

    public void hideRight() {
        mTvRight.setVisibility(GONE);
    }

    public void showRight(CharSequence text) {
        mTvRight.setVisibility(VISIBLE);
        if (text != null) {
            mTvRight.setText(text);
        }
    }

    public CharSequence getRightText() {
        return mTvRight.getText();
    }

    public void hideIcon() {
        mTvIcon.setVisibility(GONE);
    }

    public void showIcon(boolean isPlus) {
        mTvIcon.setVisibility(VISIBLE);
        mTvIcon.setText(getResources().getString(isPlus ? R.string.icon_plus : R.string.icon_minus));
    }

    public void setIconClickListener(OnClickListener listener) {
        this.mListener = listener;
        mTvIcon.setOnClickListener(listener);
        if (!mDefaultExpandHasBeenApplied) {
            if (mListener != null) {
                // 执行一次点击事件
                mListener.onClick(mTvIcon);
            }
            mDefaultExpandHasBeenApplied = true;
        }
    }

    public TextView getTvIcon() {
        return mTvIcon;
    }

    public void addViewNoInvalidate(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
            }
        }
        addViewInLayout(child, -1, params);
    }

    public OnClickListener getListener() {
        return mListener;
    }
}
