package com.android.roguegame.Model;

import java.util.ArrayList;
import java.util.Random;

public class ItemSpawn {
    static final int ROW = 20;
    static final int COL = 20;
    private int[][] maze;
    private Random random = new Random();

    public void setMapData(int[][] mapData) {
        this.maze = mapData;
    }

    public static void ItemSpawn(int[][] maze, int playerX,int playerY) {
        int emptyCellsCount = 0;
        int[] emptyX = new int[24];
        int[] emptyY = new int[24];

        // 搜集玩家附近的空格子
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int newX = playerX + i;
                int newY = playerY + j;

                // 判断边界和是否为空地
                if (newX >= 0 && newX < ROW && newY >= 0 && newY < COL && maze[newX][newY] == 0) {
                    emptyX[emptyCellsCount] = newX;
                    emptyY[emptyCellsCount] = newY;
                    emptyCellsCount++;
                }
            }
        }

        // 如果有空格子且物品数量未超过上限
        if (emptyCellsCount > 0) {
            Random random = new Random();

            // 从空格子中随机选择一个位置
            int randomIndex = random.nextInt(emptyCellsCount);
            int randomX = emptyX[randomIndex];
            int randomY = emptyY[randomIndex];

            maze[randomX][randomY] = 6;
        }
    }

}

