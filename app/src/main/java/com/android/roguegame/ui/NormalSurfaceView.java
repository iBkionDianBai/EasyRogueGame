package com.android.roguegame.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.roguegame.Game.GameThread;
import com.android.roguegame.Model.ItemSpawn;
import com.android.roguegame.Model.MapSpawn;
import com.android.roguegame.Model.MonsterMove;
import com.android.roguegame.R;
import com.android.roguegame.Save.GameData;
import com.android.roguegame.Save.LoadStorage;
import com.android.roguegame.Save.SaveStorage;

import java.util.Date;
import java.util.Locale;

public class NormalSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread gameThread = null;
    private MapSpawn mapSpawn;
    private MonsterMove monsterMove;

    private int[][] mapData; // 地图数据（0：通路，1：障碍，2：宝藏）
    private int playerX, playerY; // 玩家坐标
    private int monsterX, monsterY;// 怪物坐标
    private int treasureX,treasureY;
    private boolean hasTreasure = false; // 玩家是否获得宝藏
    private boolean isGameOver = false;// 游戏是否结束
    private boolean hasPickedUpItem = false; // 玩家是否获得道具
    private boolean isPlayerTurn = true; // 当前是否为玩家的回合
    private int monsterSteps = 2; // 怪物每回合移动的步数，默认两步
    private int playerSteps = 0;
    private int itemCount = 0;

    private int offsetY = 400;
    private boolean isFirstTime = true;

    private Bitmap[] playerSprites; // 玩家四方向贴图
    private Bitmap[] monsterSprites; // 怪物四方向贴图
    private Bitmap treasureSprite; // 宝藏贴图
    private Bitmap wallSprite; // 墙壁贴图
    private Bitmap pathSprite; // 通路贴图
    private Bitmap roomSprite; // 房间贴图
    private Bitmap itemSprite; // 道具贴图
    private Bitmap bgImage;

    private Bitmap[] scaledPlayerSprites;  // 玩家四方向缩放后的贴图
    private Bitmap[] scaledMonsterSprites; // 怪物四方向缩放后的贴图
    private Bitmap scaledTreasureSprite;   // 缩放后的宝藏贴图
    private Bitmap scaledWallSprite;       // 缩放后的墙壁贴图
    private Bitmap scaledPathSprite;       // 缩放后的通路贴图
    private Bitmap scaledRoomSprite;       // 缩放后的房间贴图
    private Bitmap scaledItemSprite;       // 缩放后的道具贴图


    private int playerDirection = 2; // 玩家方向：0=上, 1=右, 2=下, 3=左
    private int monsterDirection = 2; // 怪物方向：0=上, 1=右, 2=下, 3=左

    public NormalSurfaceView(Context context, AttributeSet attrs) {
        super(context,attrs);
        getHolder().addCallback(this);
        loadSprites(context);
        gameThread = new GameThread(getHolder(), this);
        initGame();
    }

    private void initGame() {
        mapData = new int[20][20];
        playerX = 1; playerY = 1;
        monsterX = 17; monsterY = 17;
        treasureX = 10; treasureY = 10;
        hasTreasure = false;
        isGameOver = false;
        isPlayerTurn = true;
        hasPickedUpItem = false;
        monsterSteps = 2; // 恢复怪物默认步数
        playerSteps = 0;
        mapSpawn.generateMaze(mapData);
    }

    private void initGameState2() {
        mapData = new int[20][20];
        playerX = 1; playerY = 1;
        monsterX = 18; monsterY = 18;
        treasureX = 10; treasureY = 10;
        hasTreasure = false;
        isGameOver = false;
        isPlayerTurn = true;
        hasPickedUpItem = false;
        monsterSteps = 2; // 恢复怪物默认步数
        playerSteps = 0;
        mapSpawn.generateMazeState2(mapData);
    }
    public GameThread getGameThread() {
        return this.gameThread;
    }

    private void loadSprites(Context context) {
        // 加载玩家四方向贴图
        playerSprites = new Bitmap[4];
        playerSprites[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_up);
        playerSprites[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_right);
        playerSprites[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_down);
        playerSprites[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_left);

        // 加载怪物四方向贴图
        monsterSprites = new Bitmap[4];
        monsterSprites[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_up);
        monsterSprites[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_right);
        monsterSprites[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_down);
        monsterSprites[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.slime_left);

        // 加载其他贴图
        treasureSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.treasure);
        wallSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.bark);
        pathSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.bricks_2);
        roomSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.planks);
        itemSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.freeze);
        bgImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_normal);

        // 获取屏幕大小和tileSize
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // 缩放背景图片
        bgImage = Bitmap.createScaledBitmap(bgImage, metrics.widthPixels, metrics.heightPixels, true);
    }

    public synchronized void drawGame(Canvas canvas) {
        if (canvas == null) return;

        // 绘制背景
        canvas.drawBitmap(bgImage, 0, 0, null);

        // 获取单元格大小
        int tileSize = Math.min(getWidth() / mapData[0].length, getHeight() / mapData.length);

        // 缩放贴图到tileSize * tileSize
        scaledPlayerSprites = new Bitmap[4];
        for (int i = 0; i < 4; i++) {
            scaledPlayerSprites[i] = Bitmap.createScaledBitmap(playerSprites[i], tileSize, tileSize, true);
        }

        scaledMonsterSprites = new Bitmap[4];
        for (int i = 0; i < 4; i++) {
            scaledMonsterSprites[i] = Bitmap.createScaledBitmap(monsterSprites[i], tileSize, tileSize, true);
        }

        scaledTreasureSprite = Bitmap.createScaledBitmap(treasureSprite, tileSize, tileSize, true);
        scaledWallSprite = Bitmap.createScaledBitmap(wallSprite, tileSize, tileSize, true);
        scaledPathSprite = Bitmap.createScaledBitmap(pathSprite, tileSize, tileSize, true);
        scaledRoomSprite = Bitmap.createScaledBitmap(roomSprite, tileSize, tileSize, true);
        scaledItemSprite = Bitmap.createScaledBitmap(itemSprite, tileSize, tileSize, true);

        // 绘制地图
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[0].length; x++) {
                Bitmap tileBitmap = null;

                // 根据地图数据选择对应的贴图
                if (mapData[y][x] == 1) {
                    tileBitmap = scaledWallSprite;
                } else if (mapData[y][x] == 0) {
                    tileBitmap = scaledPathSprite;
                } else if (mapData[y][x] == 2) {
                    tileBitmap = scaledRoomSprite;
                } else if (mapData[y][x] == 6) {
                    tileBitmap = scaledItemSprite;
                } else if (mapData[y][x] == 7) {
                    tileBitmap = scaledTreasureSprite;
                }

                if (tileBitmap != null) {
                    // 绘制贴图
                    canvas.drawBitmap(tileBitmap, x * tileSize, y * tileSize + offsetY, null);
                }
            }
        }

        // 绘制玩家
        Bitmap playerBitmap = scaledPlayerSprites[playerDirection];
        canvas.drawBitmap(playerBitmap, playerX * tileSize, playerY * tileSize + offsetY, null);

        // 绘制怪物
        Bitmap monsterBitmap = scaledMonsterSprites[monsterDirection];
        canvas.drawBitmap(monsterBitmap, monsterX * tileSize, monsterY * tileSize + offsetY, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            movePlayer(x, y);
            return true;
        }
        return false;
    }

    public synchronized void updateGameLogic() {
        if (isGameOver || hasTreasure) return;

        if (!isPlayerTurn) {
            for (int i = 0; i < monsterSteps; i++) {
                int[] newMonsterState = MonsterMove.monsterMove(mapData, monsterX, monsterY, playerX, playerY, monsterDirection);
                monsterX = newMonsterState[0];
                monsterY = newMonsterState[1];
                monsterDirection = newMonsterState[2];

                if (playerX == monsterX && playerY == monsterY){
                    isGameOver = true;
                    showGameStatusDialog("Fail!","Better luck next time!");
                    break;
                }
            }
            isPlayerTurn = true;
        }

        if (playerX == treasureX && playerY == treasureY) {
            hasTreasure = true;
            showVictory1Dialog("Get the Treasure!","Wanna next challenge?");
        }

        // 如果步数达到 5，生成新道具
        if (playerSteps >= 5 && itemCount <= 3) {
            ItemSpawn.ItemSpawn(mapData,playerX,playerY);
            playerSteps= 0; // 重置计数器
        }

        // 检查是否拾取道具
        if (mapData[playerX][playerY] == 6) {
            hasPickedUpItem = true;
            monsterSteps = 1; // 将怪物移动步数减为一步
            mapData[playerY][playerX] = 0; // 移除道具
            itemCount --;
        }
    }

    private void showVictory1Dialog(String title,String message) {
        Context context = getContext();
        if(context instanceof Activity) {
            gameThread.setPaused(true);
            ((Activity) context).runOnUiThread(() -> {
                new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false) // 弹窗不可取消
                        .setPositiveButton("Next Level!", (dialog, which) -> {
                            nextLevelGame();
                        })
                        .setNegativeButton("Exit",(dialog, which) -> ((Activity) context).finish())
                        .show();
            });
        }
    }

    private void showGameStatusDialog(String title, String message) {
        Context context = getContext();
        if (context instanceof Activity) {
            gameThread.setPaused(true); // 暂停线程
            ((Activity) context).runOnUiThread(() -> {
                new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false) // 弹窗不可取消
                        .setPositiveButton("Restart", (dialog, which) -> {
                            restartGame();
                        })
                        .setNegativeButton("Exit", (dialog, which) -> ((Activity) context).finish())
                        .show();
            });
        }
    }

    private void movePlayer(float x, float y) {
        if (isGameOver) return;

        int deltaX = 0, deltaY = 0;

        if (x < getWidth() / 2 && Math.abs(x - getWidth() / 2) > Math.abs(y - getHeight() / 2)) {
            deltaX = -1; // 左
            playerDirection = 3;
        } else if (x > getWidth() / 2 && Math.abs(x - getWidth() / 2) > Math.abs(y - getHeight() / 2)) {
            deltaX = 1; // 右
            playerDirection = 1;
        } else if (y < getHeight() / 2) {
            deltaY = -1; // 上
            playerDirection = 0;
        } else if (y > getHeight() / 2) {
            deltaY = 1; // 下
            playerDirection = 2;
        }

        if (canMove(playerX + deltaX, playerY + deltaY)) {
            playerX += deltaX;
            playerY += deltaY;

            // 增加步数计数
            playerSteps++;

            // 切换到怪物回合
            isPlayerTurn = false;
        }
    }

    private boolean canMove(int x, int y) {
        return x >= 0 && x < mapData[0].length && y >= 0 && y < mapData.length && mapData[y][x] != 1;
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameThread == null || isFirstTime) {
            gameThread = new GameThread(holder, this);
            gameThread.setRunning(true);
            gameThread.start();
        }
        isFirstTime = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameThread != null) {
            gameThread.setRunning(false);
            try {
                gameThread.join(); // 等待线程结束
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameThread = null;
        }
    }

    public void pauseGame() {
        gameThread.setPaused(true);
    }

    public void resumeGame() {
        gameThread.setRunning(true);
        gameThread.setPaused(false);
    }

    public void restartGame() {
        initGame(); // 重新初始化游戏数据
        resumeGame(); // 继续游戏
    }

    public void nextLevelGame() {
        initGameState2();
        restartGame();
    }

    public void saveGame() {

        // 弹出对话框让用户输入存档名称
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("保存游戏");

                // 创建 EditText 用于输入存档名
                final EditText input = new EditText(context);
                input.setHint("输入存档名称");
                builder.setView(input);

                // 添加保存按钮
                builder.setPositiveButton("保存", (dialog, which) -> {
                    String saveName = input.getText().toString().trim();
                    if (saveName.isEmpty()) {
                        Toast.makeText(context, "存档名不能为空！", Toast.LENGTH_SHORT).show();
                    } else {
                        // 获取当前时间并格式化为友好的时间字符串
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String saveTime = sdf.format(new Date());

                        // 创建存档数据对象
                        GameData gameData = new GameData(mapData, playerX, playerY, monsterX, monsterY, itemCount,
                                playerSteps, monsterSteps, playerDirection, monsterDirection, hasPickedUpItem,
                                isPlayerTurn, saveTime, saveName);

                        // 保存存档
                        if (SaveStorage.saveStorage(context, gameData, saveName)) {
                            Toast.makeText(context, "存档成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "存档失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                // 添加取消按钮
                builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());

                builder.show();
            });
        }
    }
    public void loadGame(String saveName) {
        GameData gameData = LoadStorage.loadStorage(this.getContext(), saveName);

        if (gameData != null) {
            initGame();
            this.mapData = gameData.mapData;
            this.playerX = gameData.playerX;
            this.playerY = gameData.playerY;
            this.monsterX = gameData.monsterX;
            this.monsterY = gameData.monsterY;
            this.itemCount = gameData.itemCount;
            this.playerSteps = gameData.playerSteps;
            this.monsterSteps = gameData.monsterSteps;
            this.playerDirection = gameData.playerDirection;
            this.monsterDirection = gameData.monsterDirection;
            this.hasPickedUpItem = gameData.hasPickedUpItem;
            this.isPlayerTurn = gameData.isPlayerTurn;

            Toast.makeText(this.getContext(), "游戏加载成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), "加载失败！", Toast.LENGTH_SHORT).show();
        }
    }
}
