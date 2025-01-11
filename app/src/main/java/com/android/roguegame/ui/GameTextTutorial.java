package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.android.roguegame.R;

import java.util.ResourceBundle;

public class GameTextTutorial extends View {
    private Paint highlightPaint;
    private Paint textPaint;
    private Paint backgroundPaint;
    private int currentIndex = 0;
    private String[] messages;
    private int tileSize;
    private int offsetX=0,offsetY=0;
    private int screenWidth,screenHeight;

    public GameTextTutorial(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    public void initialize(int tileSize, String[] messages) {
        this.tileSize = tileSize;
        this.messages = messages;
    }

    // 画笔设置
    public void initPaints() {
        int mapWidth = 6 * 64;
        int mapHeight = 6 * 64;
        // 计算偏移量
        offsetX = (screenWidth - mapWidth) / 2;
        offsetY = (screenHeight - mapHeight) / 2;

        highlightPaint = new Paint();
        highlightPaint.setColor(Color.YELLOW);
        highlightPaint.setStyle(Paint.Style.STROKE);
        highlightPaint.setStrokeWidth(4);

        // 提示文字的画笔
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.medievalsharp);
        textPaint.setTypeface(typeface);

        // 背景的画笔
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAlpha(100);
    }

    // 屏幕宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.screenWidth = w;
        this.screenHeight = h;
    }

    // 切换到下一个高亮格子和提示信息
    public void nextTutorial() {
        if (messages != null && messages.length > 0) {
            currentIndex = (currentIndex + 1) % messages.length;
            invalidate(); // 刷新视图
        }
    }

    // 绘制提示信息
    private void drawMessages(Canvas canvas) {
        if (messages != null && messages.length > 0) {
            String message = messages[currentIndex];
            String[] lines = message.split("\n"); // 支持多行提示信息

            // 计算总高度和宽度
            float maxWidth = 0;
            float totalHeight = lines.length * textPaint.getTextSize() + (lines.length - 1) * 20;

            for (String line : lines) {
                maxWidth = Math.max(maxWidth, textPaint.measureText(line));
            }

            // 背景位置
            float backgroundLeft = (screenWidth - maxWidth) / 2 - 20;
            float backgroundTop = screenHeight - totalHeight - 500;
            float backgroundRight = (screenWidth + maxWidth) / 2 + 20;
            float backgroundBottom = screenHeight - 480;

            // 绘制背景
            canvas.drawRect(backgroundLeft, backgroundTop, backgroundRight, backgroundBottom, backgroundPaint);

            // 绘制每一行文字
            float textX = (screenWidth - maxWidth) / 2;
            float textY = backgroundTop + textPaint.getTextSize();
            for (String line : lines) {
                canvas.drawText(line, textX, textY, textPaint);
                textY += textPaint.getTextSize() + 20; // 行间距 20
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制提示信息
        drawMessages(canvas);
    }

}
