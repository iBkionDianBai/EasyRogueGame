package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.android.roguegame.R;

public class PlayerSurfaceView{
    private int[][] mapData;
    private Bitmap playerBitmap;
    private int playerX, playerY; // 玩家位置// 玩家位置偏移量
    private Context context;

    // 方向常量
    public static final int DIR_UP = 0;
    public static final int DIR_DOWN = 1;
    public static final int DIR_LEFT = 2;
    public static final int DIR_RIGHT = 3;

    private int direction;  // 当前方向

    public PlayerSurfaceView(Context context,int[][] mapData,int initialX,int initialY){
        this.playerX = initialX;
        this.playerY = initialY;
        this.mapData = mapData;
        this.direction = DIR_DOWN;
        this.context = context;

        loadPlayerImage(context);
    }

    // 根据当前方向加载对应的图片
    private void loadPlayerImage(Context context) {
        switch (direction) {
            case DIR_UP:
                playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_up);  // 请替换为你的资源名
                break;
            case DIR_DOWN:
                playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_down);
                break;
            case DIR_LEFT:
                playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left);
                break;
            case DIR_RIGHT:
                playerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right);
                break;
        }
        playerBitmap = Bitmap.createScaledBitmap(playerBitmap, 64, 64, true);
    }

    // 绘制玩家到画布
    public void draw(Canvas canvas,int offsetX,int offsetY) {
        canvas.drawBitmap(playerBitmap, (playerX) * 64 + offsetX, (playerY) * 64 + offsetY, null);
    }

    // 更新玩家的位置
    public void updatePosition(int deltaX, int deltaY, int direction) {
        // 计算目标位置
        int targetX = playerX + deltaX;
        int targetY = playerY + deltaY;

        // 检查目标位置是否合法（即是否为 0）
        if (isMoveValid(targetX, targetY)) {
            playerX = targetX;
            playerY = targetY;
            this.direction = direction;  // 更新方向
            loadPlayerImage(context);  // 更新图片
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


    // 获取玩家的当前位置
    public float getPlayerX() {
        return playerX;
    }

    public float getPlayerY() {
        return playerY;
    }
}
