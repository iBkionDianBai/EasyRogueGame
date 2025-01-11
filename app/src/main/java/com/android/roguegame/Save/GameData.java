package com.android.roguegame.Save;

import java.security.PublicKey;

public class GameData {
    public int[][] mapData;
    public int playerX;
    public int playerY;
    public int monsterX;
    public int monsterY;
    public int itemCount;
    public int playerSteps;
    public int monsterSteps;
    public int playerDirection;
    public int monsterDirection;
    public boolean hasPickedUpItem;
    public boolean isPlayerTurn;
    public String saveTime;
    public String saveName;

    public GameData(int[][] mapData, int playerX, int playerY, int monsterX, int monsterY,
                    int itemCount, int playerSteps, int monsterSteps, int playerDirection,
                    int monsterDirection,
                    boolean hasPickedUpItem, boolean isPlayerTurn,String saveTime,String saveName) {
        this.mapData = mapData;
        this.playerX = playerX;
        this.playerY = playerY;
        this.monsterX = monsterX;
        this.monsterY = monsterY;
        this.itemCount = itemCount;
        this.playerSteps = playerSteps;
        this.monsterSteps = monsterSteps;
        this.playerDirection = playerDirection;
        this.monsterDirection = monsterDirection;
        this.hasPickedUpItem = hasPickedUpItem;
        this.isPlayerTurn = isPlayerTurn;
        this.saveTime = saveTime;
        this.saveName = saveName;
    }
}
