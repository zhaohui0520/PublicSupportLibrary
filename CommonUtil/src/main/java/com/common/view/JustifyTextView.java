package com.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * 解决行尾空格的问题
 */
public class JustifyTextView extends AppCompatTextView {

    private int mLineY;
    private int mViewWidth;

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    protected void onDraw(Canvas canvas) {
        TextPaint paint = this.getPaint();
        paint.setColor(this.getCurrentTextColor());
        paint.drawableState = this.getDrawableState();
        this.mViewWidth = this.getMeasuredWidth();
        if (this.getText() != null) {
            String text = this.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                this.mLineY = 0;
                this.mLineY = (int) ((float) this.mLineY + this.getTextSize());
                Layout layout = this.getLayout();

                for (int i = 0; i < layout.getLineCount(); ++i) {
                    int lineStart = layout.getLineStart(i);
                    int lineEnd = layout.getLineEnd(i);
                    String line = text.substring(lineStart, lineEnd);
                    float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, this.getPaint());
                    if (this.needScale(line)) {
                        this.drawScaledText(canvas, lineStart, line, width);
                    } else {
                        canvas.drawText(line, 0.0F, (float) this.mLineY, paint);
                    }
                    this.mLineY += this.getLineHeight();
                }
            }
        }
    }

    private void drawScaledText(Canvas canvas, int lineStart, String line, float lineWidth) {
        float x = 0.0F;
        if (this.isFirstLineOfParagraph(lineStart, line)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, (float) this.mLineY, this.getPaint());
            float bw = StaticLayout.getDesiredWidth(blanks, this.getPaint());
            x += bw;
            line = line.substring(3);
        }

        float d = 0;
        if (line.length() > 1) {
            d = ((float) this.mViewWidth - lineWidth) / (float) (line.length() - 1) - 1.0F;
        }

        for (int i = 0; i < line.length(); ++i) {
            String c = String.valueOf(line.charAt(i));
            float cw = StaticLayout.getDesiredWidth(c, this.getPaint());
            canvas.drawText(c, x, (float) this.mLineY, this.getPaint());
            x += cw + d;
        }

    }

    private boolean isFirstLineOfParagraph(int lineStart, String line) {
        return line.length() > 3 && line.charAt(0) == 32 && line.charAt(1) == 32;
    }

    private boolean needScale(String line) {
        return line.length() == 0 ? false : line.charAt(line.length() - 1) != 10;
    }
}
