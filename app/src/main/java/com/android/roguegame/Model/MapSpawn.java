package com.android.roguegame.Model;

import android.graphics.Color;

import java.util.Random;

public class MapSpawn {
    static final int ROW = 20;
    static final int COL = 20;

    public static void generateMaze(int[][] maze) {
        Random rand = new Random(); // 创建 Random 实例

        // 初始化迷宫，填充为墙壁
        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                maze[i][j] = 1; // 1 表示墙壁
            }
        }

        // 使用深度优先搜索生成迷宫
        int startX = 1, startY = 1;
        dfsGenerateMaze(maze, startX, startY, rand);

        // 随机打破一些墙壁
        randomBreakWalls(maze, rand);

        // 设置中央区域为路径
        for (int i = ROW / 2 - 1; i <= ROW / 2 + 1; ++i) {
            for (int j = COL / 2 - 1; j <= COL / 2 + 1; ++j) {
                maze[i][j] = 2;
            }
        }

        // 设置起点和终点
        maze[ROW / 2][COL / 2] = 7;
        maze[1][1] = 0; // 起始位置
        maze[ROW - 2][COL - 2] = 0; // 终点

        // 确保右下角通路（保证怪物不被封闭）
        ensureRightBottomOpen(maze);

        // 设置边界墙
        for (int i = 0; i < ROW; i++) {
            maze[i][0] = 1; // 左边墙
            maze[i][COL - 1] = 1; // 右边墙
        }

        for (int j = 0; j < COL; j++) {
            maze[0][j] = 1; // 上边墙
            maze[ROW - 1][j] = 1; // 下边墙
        }
    }

    public static void generateMazeState2(int[][] maze) {
        Random rand = new Random(); // 创建 Random 实例

        // 初始化迷宫，填充为墙壁
        for (int i = 0; i < ROW; ++i) {
            for (int j = 0; j < COL; ++j) {
                maze[i][j] = 1; // 1 表示墙壁
            }
        }

        // 使用深度优先搜索生成迷宫
        int startX = 1, startY = 1;
        dfsGenerateMaze(maze, startX, startY, rand);

        // 随机打破一些墙壁
        randomBreakWalls(maze, rand);

        maze[1][1] = 0; // 起始位置
        maze[ROW - 2][COL - 2] = 0; // 终点

        // 确保右下角通路（保证怪物不被封闭）
        ensureRightBottomOpen(maze);

        // 设置边界墙
        for (int i = 0; i < ROW; i++) {
            maze[i][0] = 1; // 左边墙
            maze[i][COL - 1] = 1; // 右边墙
        }

        for (int j = 0; j < COL; j++) {
            maze[0][j] = 1; // 上边墙
            maze[ROW - 1][j] = 1; // 下边墙
        }
    }

    // 深度优先搜索生成迷宫
    private static void dfsGenerateMaze(int[][] maze, int x, int y, Random rand) {
        maze[x][y] = 0; // 设置当前位置为通路

        // 定义4个方向：上下左右
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        // 随机打乱方向
        int[] directions = {0, 1, 2, 3};
        for (int i = directions.length - 1; i > 0; --i) {
            int j = rand.nextInt(i + 1);
            int temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        // 遍历随机方向
        for (int dir : directions) {
            int nx = x + dx[dir] * 2;
            int ny = y + dy[dir] * 2;

            // 检查新位置是否在迷宫范围内，并且尚未访问
            if (nx > 0 && nx < ROW - 1 && ny > 0 && ny < COL- 1 && maze[nx][ny] == 1) {
                // 打通墙壁
                maze[x + dx[dir]][y + dy[dir]] = 0;
                dfsGenerateMaze(maze, nx, ny, rand);
            }
        }
    }
    // 随机打破一些墙壁
    private static void randomBreakWalls(int[][] maze, Random rand) {
        int breakAttempts = (maze.length * maze[0].length) / 10; // 打破墙壁次数，约为总格子的 10%
        for (int i = 0; i < breakAttempts; ++i) {
            int x = rand.nextInt(maze.length - 2) + 1;
            int y = rand.nextInt(maze[0].length - 2) + 1;
            if (maze[x][y] == 1) { // 仅打破墙壁
                maze[x][y] = 0;
            }
        }
    }

    // 确保右下角通路
    private static void ensureRightBottomOpen(int[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        // 打通右下角附近的墙壁
        maze[rows - 3][cols - 3] = 0;
        maze[rows - 3][cols - 2] = 0;
        maze[rows - 2][cols - 3] = 0;
    }
}