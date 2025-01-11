package com.android.roguegame.Model;

public class MonsterMove {

    /**
     * 更新怪物的位置并改变方向
     *
     * @param mapData 地图数据
     * @param monsterX 怪物的当前X坐标
     * @param monsterY 怪物的当前Y坐标
     * @param playerX 玩家当前X坐标
     * @param playerY 玩家当前Y坐标
     * @param monsterDirection 怪物当前方向（0=上, 1=右, 2=下, 3=左）
     * @return 包含更新后位置和方向的数组：[新X, 新Y, 新方向]
     */
    public static int[] monsterMove(int[][] mapData, int monsterX, int monsterY, int playerX, int playerY, int monsterDirection) {
        // 初始化怪物的新位置
        int newX = monsterX;
        int newY = monsterY;

        // 计算与玩家的距离
        int deltaX = playerX - monsterX;
        int deltaY = playerY - monsterY;

        // 根据玩家位置调整怪物移动优先级（例如优先垂直移动，或者优先水平移动）
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // 水平方向优先
            if (deltaX > 0 && canMove(mapData, monsterX + 1, monsterY)) {
                newX++;
                monsterDirection = 1; // 右
            } else if (deltaX < 0 && canMove(mapData, monsterX - 1, monsterY)) {
                newX--;
                monsterDirection = 3; // 左
            } else if (deltaY > 0 && canMove(mapData, monsterX, monsterY + 1)) {
                newY++;
                monsterDirection = 2; // 下
            } else if (deltaY < 0 && canMove(mapData, monsterX, monsterY - 1)) {
                newY--;
                monsterDirection = 0; // 上
            }
        } else {
            // 垂直方向优先
            if (deltaY > 0 && canMove(mapData, monsterX, monsterY + 1)) {
                newY++;
                monsterDirection = 2; // 下
            } else if (deltaY < 0 && canMove(mapData, monsterX, monsterY - 1)) {
                newY--;
                monsterDirection = 0; // 上
            } else if (deltaX > 0 && canMove(mapData, monsterX + 1, monsterY)) {
                newX++;
                monsterDirection = 1; // 右
            } else if (deltaX < 0 && canMove(mapData, monsterX - 1, monsterY)) {
                newX--;
                monsterDirection = 3; // 左
            }
        }

        // 返回更新后的位置和方向
        return new int[]{newX, newY, monsterDirection};
    }

    /**
     * 判断怪物是否可以移动到指定位置
     *
     * @param mapData 地图数据
     * @param x 目标X坐标
     * @param y 目标Y坐标
     * @return 如果可以移动则返回 true，否则返回 false
     */
    private static boolean canMove(int[][] mapData, int x, int y) {
        return x >= 0 && x < mapData[0].length && y >= 0 && y < mapData.length && mapData[y][x] != 1;
    }
}