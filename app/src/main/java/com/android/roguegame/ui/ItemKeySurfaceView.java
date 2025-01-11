package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.android.roguegame.R;

import java.security.Key;

public class ItemKeySurfaceView {
    private int[][] mapData;
    private Bitmap KeyBitmap;
    private int KeyX,KeyY;
    private Context context;

    public ItemKeySurfaceView(Context context,int[][] mapData,int initialX,int initialY){
        this.KeyX = initialX;
        this.KeyY = initialY;
        this.mapData = mapData;

        KeyBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.key);

        // 矫正尺寸
        KeyBitmap = Bitmap.createScaledBitmap(KeyBitmap, 64, 64, true);
    }

    // 绘制道具到画布
    public void draw(Canvas canvas,int offsetX,int offsetY) {
        canvas.drawBitmap(KeyBitmap, (KeyX) * 64 + offsetX, (KeyY) * 64 + offsetY, null);
    }


    // 获取玩家的当前位置
    public float getKeyX() {
        return KeyX;
    }

    public float getKeyY() {
        return KeyY;
    }
}
