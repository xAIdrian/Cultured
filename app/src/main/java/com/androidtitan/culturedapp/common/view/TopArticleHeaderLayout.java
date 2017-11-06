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
 * Created by Adrian Mohnacs on 12/12/16.
 */

public class TopArticleHeaderLayout extends RelativeLayout{
    private final String TAG = getClass().getSimpleName();

    private RelativeLayout gradientRelativeLayout;
    private TextView titleTextView;
    private TextView abstractTextView;
    private TextView dateTextView;
    private TextView sectionTextView;

    private String titleText;
    private String abstractText;
    private String dateText;
    private String sectionText;
    private Drawable gradientDrawable;

    public TopArticleHeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopArticleHeaderLayout, 0, 0);

        try {
            titleText = typedArray.getString(R.styleable.TopArticleHeaderLayout_articleTitleText);
            abstractText = typedArray.getString(R.styleable.TopArticleHeaderLayout_articleAbstractText);
            dateText = typedArray.getString(R.styleable.TopArticleHeaderLayout_articleDateText);
            sectionText = typedArray.getString(R.styleable.TopArticleHeaderLayout_articleSectionText);
            gradientDrawable = typedArray.getDrawable(R.styleable.TopArticleHeaderLayout_articleGradientDrawable);
        } finally {
            typedArray.recycle();
        }

        LayoutInflater layoutInflater = (LayoutInflater.from(context));
        layoutInflater.inflate(R.layout.top_article_header_layout, this);

        gradientRelativeLayout = (RelativeLayout) findViewById(R.id.gradientRelativeLayout);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        abstractTextView = (TextView) findViewById(R.id.abstractTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        sectionTextView = (TextView) findViewById(R.id.sectionTextView);

        gradientRelativeLayout.setBackgroundDrawable(gradientDrawable);
        titleTextView.setText(titleText);
        abstractTextView.setText(abstractText);
        dateTextView.setText(dateText);
        sectionTextView.setText(sectionText);

        invalidate();
        requestLayout();

    }

    //todo: when we start defining our own custom styles we need to give these a go
    public TopArticleHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopArticleHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TopArticleHeaderLayout(Context context) {
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

        invalidate();
        requestLayout();
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        abstractTextView.setText(abstractText);

        invalidate();
        requestLayout();
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        dateTextView.setText(dateText);

        invalidate();
        requestLayout();
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        sectionTextView.setText(sectionText);

        invalidate();
        requestLayout();
    }

    public Drawable getGradientDrawable() {
        return gradientDrawable;
    }

    public void setGradientDrawable(int gradientDrawable) {
        gradientRelativeLayout.setBackgroundColor(gradientDrawable);

        invalidate();
        requestLayout();
    }
}
