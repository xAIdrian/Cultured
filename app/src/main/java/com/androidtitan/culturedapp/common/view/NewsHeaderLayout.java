package com.androidtitan.culturedapp.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidtitan.culturedapp.R;


/**
 * Created by amohnacs on 9/12/16.
 */

public class NewsHeaderLayout extends RelativeLayout {
    private final String TAG = getClass().getSimpleName();

    private RelativeLayout gradientRelativeLayout;
    private TextView titleTextView;
    private TextView dateTextView;
    private TextView sectionTextView;

    private String titleText;
    private String dateText;
    private String sectionText;
    private Drawable gradientDrawable;

    public NewsHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewsHeaderLayout, 0, 0);

        titleText = typedArray.getString(R.styleable.NewsHeaderLayout_newsTitleText);
        dateText = typedArray.getString(R.styleable.NewsHeaderLayout_newsDateText);
        sectionText = typedArray.getString(R.styleable.NewsHeaderLayout_newsSectionText);
        gradientDrawable = typedArray.getDrawable(R.styleable.NewsHeaderLayout_newsGradientDrawable);

        LayoutInflater layoutInflater = (LayoutInflater.from(context));
        layoutInflater.inflate(R.layout.news_header_layout, this);

        gradientRelativeLayout = (RelativeLayout) findViewById(R.id.gradientRelativeLayout);
        gradientRelativeLayout.setBackgroundDrawable(gradientDrawable);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText(titleText);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(dateText);

        sectionTextView = (TextView) findViewById(R.id.sectionTextView);
        sectionTextView.setText(sectionText);

    }

    //todo: when we start defining our own custom styles we need to give these a go
    public NewsHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NewsHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NewsHeaderLayout(Context context) {
        this(context, null);
    }

    public void setGradientViewHeight(ImageView imageView) {
        int setterHeight = imageView.getMeasuredHeight() / 2;
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) gradientRelativeLayout.getLayoutParams();
        params.height = setterHeight;
        gradientRelativeLayout.setLayoutParams(params);
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        titleTextView.setText(titleText);
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        dateTextView.setText(dateText);
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        sectionTextView.setText(sectionText);
    }

    public Drawable getGradientDrawable() {
        return gradientDrawable;
    }

    public void setGradientDrawable(int gradientDrawable) {
        gradientRelativeLayout.setBackgroundColor(gradientDrawable);
    }
}
