package com.android.roguegame.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.roguegame.R;

public class TutorialSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    int[][] mapData; // 地图的二维数组
    Bitmap tile0;    // 图片资源：0 对应的图片
    Bitmap tile1;    // 图片资源：1 对应的图片
    Bitmap backgroundImage; // 背景图片
    int tileWidth = 64;// 每个格子的大小（单位：像素）
    int tileHeight = 64;
    int offsetX,offsetY;
    SurfaceHolder surfaceHolder;
    private Bitmap mapCache; // 缓存的地图位图
    private boolean isMapCached = false;// 标志位，指示地图是否已缓存
    private PlayerSurfaceView player;
    private MonsterSurfaceView monster;
    private ItemFreezeSurfaceView freeze;
    private ItemKeySurfaceView key;
    private ItemTreasureSurfaceView treasure;

    public TutorialSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // 初始化偏移量
        offsetY = 0;
        offsetX = 0;

        // 加载图片资源
        tile0 = BitmapFactory.decodeResource(getResources(), R.drawable.bricks_2);
        tile1 = BitmapFactory.decodeResource(getResources(), R.drawable.bark);
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.bg_tutorial);

        // 可选：根据屏幕大小缩放背景图片
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, metrics.widthPixels, metrics.heightPixels, true);

        // 如果贴图不是 32×32 的大小，可以缩放位图到固定尺寸
        tile0 = Bitmap.createScaledBitmap(tile0, tileWidth, tileHeight, true);
        tile1 = Bitmap.createScaledBitmap(tile1, tileWidth, tileHeight, true);
    }

    // 设置地图数据
    public void setMapData(int[][] mapData) {
        this.mapData = mapData;
    }

    // 计算偏移量
    private void calculateOffset() {
        // 获取屏幕尺寸
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        int mapWidth = mapData[0].length * tileWidth;
        int mapHeight = mapData.length * tileHeight;
        // 计算偏移量
        offsetX = (screenWidth - mapWidth) / 2;
        offsetY = (screenHeight - mapHeight) / 2;
    }
    // 缓存地图
    private void cacheMap() {
        // 计算地图尺寸
        int mapWidth = mapData[0].length * tileWidth;
        int mapHeight = mapData.length * tileHeight;

        mapCache = Bitmap.createBitmap(mapWidth,mapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mapCache);
        // 绘制到缓存
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[i].length; j++) {
                int pixelX = j * tileWidth;
                int pixelY = i * tileHeight;

                if (mapData[i][j] == 0) {
                    canvas.drawBitmap(tile0, pixelX, pixelY, null);
                } else if (mapData[i][j] == 1) {
                    canvas.drawBitmap(tile1, pixelX, pixelY, null);
                }
            }
        }
        isMapCached = true;
    }
    public void setPlayer(PlayerSurfaceView player) {
        this.player = player;
    }
    public void setMonster(MonsterSurfaceView monster) {
        this.monster = monster;
    }
    public void setFreeze(ItemFreezeSurfaceView freeze) {
        this.freeze = freeze;
    }
    public void setKey(ItemKeySurfaceView key) {
        this.key = key;
    }
    public void setTreasure(ItemTreasureSurfaceView treasure) {
        this.treasure = treasure;
    }
    // 渲染地图
    public void drawMap() {
        if(!isMapCached){
            cacheMap();
        }

        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            try {
                canvas.drawBitmap(backgroundImage,0,0,null);
                canvas.drawBitmap(mapCache,offsetX,offsetY,null);
                player.draw(canvas,offsetX,offsetY);
                monster.draw(canvas,offsetX,offsetY);
                freeze.draw(canvas,offsetX,offsetY);
                key.draw(canvas,offsetX,offsetY);
                treasure.draw(canvas,offsetX,offsetY);
            }finally {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateOffset();
        cacheMap();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 可根据需求重绘
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 清理资源
    }
}