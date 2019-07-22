package com.speedata.jinhualajidemo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.speedata.jinhualajidemo.R;

public class TitleBarView  extends LinearLayout
{
    private OnLeftButtonClickListener mLeftButtonClickListener;
    private OnRightButtonClickListener mRightButtonClickListener;
    private MyViewHolder mViewHolder;
    private View viewAppTitle;

    public TitleBarView(Context context)
    {
        super(context);

        init();
    }

    public TitleBarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TitleBarView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        init();
    }

    private void init()
    {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        viewAppTitle = inflater.inflate(R.layout.title_layout, null);
        this.addView(viewAppTitle, layoutParams);

        mViewHolder = new MyViewHolder(this);
        mViewHolder.llLeftGoBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (Utility.isFastDoubleClick())
//                {
//                    return;
//                }

                if (mLeftButtonClickListener != null)
                {
                    mLeftButtonClickListener.onLeftButtonClick(v);
                }
            }
        });
        mViewHolder.llRight.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                if (Utility.isFastDoubleClick())
//                {
//                    return;
//                }

                if (mRightButtonClickListener != null)
                {
                    mRightButtonClickListener.OnRightButtonClick(v);
                }
            }
        });
    }

    public void initViewsVisible(boolean isLeftButtonVisile, boolean isCenterTitleVisile, boolean isRightIconVisile, boolean isRightTitleVisile)
    {
        // 左侧返回
        mViewHolder.llLeftGoBack.setVisibility(isLeftButtonVisile ? View.VISIBLE : View.INVISIBLE);

        // 中间标题
        mViewHolder.tvCenterTitle.setVisibility(isCenterTitleVisile ? View.VISIBLE : View.INVISIBLE);

        // 右侧返回图标,文字
        if (!isRightIconVisile && !isRightTitleVisile)
        {
            mViewHolder.llRight.setVisibility(View.INVISIBLE);
        }
        else
        {
            mViewHolder.llRight.setVisibility(View.VISIBLE);
        }
        mViewHolder.ivRightComplete.setVisibility(isRightIconVisile ? View.VISIBLE : View.GONE);
        mViewHolder.tvRightComplete.setVisibility(isRightTitleVisile ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setAppTitle(String title)
    {
        if (!TextUtils.isEmpty(title))
        {
            mViewHolder.tvCenterTitle.setText(title);
        }
    }

    public void setRightTitle(String text)
    {
        if (!TextUtils.isEmpty(text))
        {
            mViewHolder.tvRightComplete.setText(text);
        }
    }

    public void setRightIcon(int sourceID)
    {
        mViewHolder.ivRightComplete.setImageResource(sourceID);
    }

    public void setLeftOnclick(OnLeftButtonClickListener mOnLeftButtonClickListener)
    {
        if (mOnLeftButtonClickListener != null)
        {
        }
    }

    public void setAppBackground(int color)
    {
        viewAppTitle.setBackgroundColor(color);
    }

    public void setOnLeftButtonClickListener(OnLeftButtonClickListener listen)
    {
        mLeftButtonClickListener = listen;
    }

    public void setOnRightButtonClickListener(OnRightButtonClickListener listen)
    {
        mRightButtonClickListener = listen;
    }

    public static abstract interface OnLeftButtonClickListener
    {
        public abstract void onLeftButtonClick(View v);
    }

    public static abstract interface OnRightButtonClickListener
    {
        public abstract void OnRightButtonClick(View v);
    }

    static class MyViewHolder
    {
        LinearLayout llLeftGoBack;
        TextView tvCenterTitle;
        LinearLayout llRight;
        ImageView ivRightComplete;
        TextView tvRightComplete;

        public MyViewHolder(View v)
        {
            llLeftGoBack = (LinearLayout) v.findViewById(R.id.llLeftGoBack);
            tvCenterTitle = (TextView) v.findViewById(R.id.tvCenterTitle);
            llRight = (LinearLayout) v.findViewById(R.id.llRight);
            ivRightComplete = (ImageView) v.findViewById(R.id.ivRightComplete);
            tvRightComplete = (TextView) v.findViewById(R.id.tvRightComplete);
        }
    }
}
