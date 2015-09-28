package pt.gngtv.main.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import pt.gngtv.R;

public class TextViewObliqueStrike extends TextView {
    private Paint paint;

    public TextViewObliqueStrike(Context context) {
        super(context);
        init(context);
    }

    public TextViewObliqueStrike(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewObliqueStrike(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        final int dividerColor = resources.getColor(android.R.color.white);

        paint = new Paint();
        paint.setColor(dividerColor);
        paint.setStrokeWidth(resources.getDimension(R.dimen.text_strike_stroke_width));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getPaddingBottom(), getHeight() - getPaddingBottom(), getWidth() - getPaddingTop(), getPaddingTop(), paint);
    }
}