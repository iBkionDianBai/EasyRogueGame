package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.android.roguegame.R;

public class ItemTreasureSurfaceView {
    private int[][] mapData;
    private Bitmap TreasureBitmap;
    private int TreasureX,TreasureY;
    private Context context;

    public ItemTreasureSurfaceView(Context context,int[][] mapData,int initialX,int initialY){
        this.TreasureX = initialX;
        this.TreasureY = initialY;
        this.mapData = mapData;

        TreasureBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure);

        // 矫正尺寸
        TreasureBitmap = Bitmap.createScaledBitmap(TreasureBitmap, 64, 64, true);
    }

    // 绘制道具到画布
    public void draw(Canvas canvas,int offsetX,int offsetY) {
        canvas.drawBitmap(TreasureBitmap, (TreasureX) * 64 + offsetX, (TreasureY) * 64 + offsetY, null);
    }

    // 获取玩家的当前位置
    public float getTreasureX() {
        return TreasureX;
    }

    public float getTreasureY() {
        return TreasureY;
    }
}
