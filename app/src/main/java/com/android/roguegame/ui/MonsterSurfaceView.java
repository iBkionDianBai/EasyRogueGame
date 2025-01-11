package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.android.roguegame.R;

public class MonsterSurfaceView {
    private int[][] mapData;
    private Bitmap monsterBitmap;
    private int monsterX, monsterY; // 玩家位置
    private Context context;

    // 方向常量
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    private int direction;  // 当前方向

    public MonsterSurfaceView(Context context,int[][] mapData,int initialX,int initialY) {
        this.context = context;
        this.mapData = mapData;
        this.monsterX = initialX;
        this.monsterY = initialY;
        this.direction = DIR_DOWN;// 初始偏移量为 0

        loadMonsterImage(context);
    }

    // 根据当前方向加载对应的图片
    private void loadMonsterImage(Context context) {
        switch (direction) {
            case DIR_UP:
                monsterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_up);  // 请替换为你的资源名
                break;
            case DIR_DOWN:
                monsterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_down);
                break;
            case DIR_LEFT:
                monsterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_left);
                break;
            case DIR_RIGHT:
                monsterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_right);
                break;
        }
        monsterBitmap = Bitmap.createScaledBitmap(monsterBitmap, 64, 64, true);
    }

    // 绘制怪物到画布
    public void draw(Canvas canvas,int offsetX,int offsetY) {
        canvas.drawBitmap(monsterBitmap, (monsterX) * 64 + offsetX, (monsterY) * 64 + offsetY, null);
    }

    // 更新怪物的位置
    public void updatePosition(int deltaX, int deltaY, int direction) {
        // 计算目标位置
        int targetX = monsterX + deltaX;
        int targetY = monsterY + deltaY;

        // 检查目标位置是否合法（即是否为 0）
        if (isMoveValid(targetX, targetY)) {
            monsterX = targetX;
            monsterY = targetY;
            this.direction = direction;  // 更新方向
            loadMonsterImage(context);  // 更新图片
        }
    }

    // 检查目标位置是否为有效的通路（即值为 0）
    private boolean isMoveValid(int targetX, int targetY) {
        // 检查坐标是否在有效范围内
        if (targetX >= 0 && targetX < mapData.length && targetY >= 0 && targetY < mapData[0].length) {
            return mapData[targetX][targetY] == 0;  // 只有值为 0 的地方才允许移动
        }
        return false;
    }

    // 获取怪物目前位置
    public int getMonsterX(){
        return monsterX;
    }

    public int getMonsterY(){
        return monsterY;
    }
}
