package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.android.roguegame.R;

public class ItemFreezeSurfaceView {
    private int[][] mapData;
    private Bitmap ItemFreezeBitmap;
    private int ItemFreezeX, ItemFreezeY;
    private Context context;

    public ItemFreezeSurfaceView(Context context,int[][] mapData,int initialX,int initialY) {
        this.ItemFreezeX = initialX;
        this.ItemFreezeY = initialY;
        this.mapData = mapData;
        this.context = context;

        ItemFreezeBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.freeze);

        // 矫正尺寸
        ItemFreezeBitmap = Bitmap.createScaledBitmap(ItemFreezeBitmap, 64, 64, true);
    }

    // 绘制道具到画布
    public void draw(Canvas canvas,int offsetX,int offsetY) {
        canvas.drawBitmap(ItemFreezeBitmap, (ItemFreezeX) * 64 + offsetX, (ItemFreezeY) * 64 + offsetY, null);
    }

    // 获取玩家的当前位置
    public float getItemFreezeX() {
        return ItemFreezeX;
    }

    public float getItemFreezeY() {
        return ItemFreezeY;
    }
}
