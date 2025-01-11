package com.android.roguegame.Game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.android.roguegame.ui.NormalSurfaceView;

public class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private NormalSurfaceView gameView;
    private boolean running = true;
    private boolean paused = false;
    private static final int MAX_FPS = 30; // 最大帧率
    private static final int FRAME_PERIOD = 1000 / MAX_FPS; // 每帧毫秒数

    public GameThread(SurfaceHolder holder, NormalSurfaceView view) {
        this.surfaceHolder = holder;
        this.gameView = view;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void run() {
        while (running) {
            if (paused) {
                try {
                    Thread.sleep(FRAME_PERIOD); // 暂停时降低资源消耗
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            Canvas canvas = null;
            try {
                // 锁定 Canvas 以进行绘制
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        // 绘制游戏内容
                        gameView.drawGame(canvas);
                    }
                }
            } finally {
                // 确保释放锁
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            // 更新游戏逻辑
            gameView.updateGameLogic();

            // 控制帧率 (16ms -> 约60 FPS)
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
