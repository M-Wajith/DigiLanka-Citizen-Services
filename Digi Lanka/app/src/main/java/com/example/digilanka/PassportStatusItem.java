package com.example.digilanka;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PassportStatusItem extends LinearLayout {

    private TextView statusText;
    private View statusIndicator;

    public PassportStatusItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.passport_status_item, this, true);

        statusText = findViewById(R.id.status_text);
        statusIndicator = findViewById(R.id.status_indicator);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PassportStatusItem,
                0, 0);

        try {
            String text = a.getString(R.styleable.PassportStatusItem_statusText);
            boolean completed = a.getBoolean(R.styleable.PassportStatusItem_statusCompleted, false);
            setStatusText(text);
            setStatusCompleted(completed);
        } finally {
            a.recycle();
        }
    }

    public void setStatusText(String text) {
        statusText.setText(text);
    }

    public void setStatusCompleted(boolean completed) {
        if (completed) {
            statusIndicator.setBackgroundResource(R.drawable.circle_shape_completed);
        } else {
            statusIndicator.setBackgroundResource(R.drawable.circle_shape); // Default color
        }
    }
}
