package com.android.roguegame.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.roguegame.R;
import com.android.roguegame.ui.NormalSurfaceView;

public class NormalActivity extends AppCompatActivity {
    private NormalSurfaceView normalSurfaceView;
    private RelativeLayout menuOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        // 获取传递的存档名
        String saveName = getIntent().getStringExtra("saveName");

        // 获取按钮并设置点击监听器
        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> toggleMenu(true));


        normalSurfaceView = findViewById(R.id.normal_surface_view);
        menuOverlay = findViewById(R.id.menuOverlay);

        if (menuOverlay == null) {
            throw new IllegalStateException("menuOverlay is not properly initialized. Check your layout file.");
        }

        if (saveName != null && !saveName.isEmpty()) {
            // 加载存档
            normalSurfaceView.loadGame(saveName);
        }

        Button restartButton = findViewById(R.id.restartButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button resumeButton = findViewById(R.id.resumeButton);

        restartButton.setOnClickListener(v -> restartGame());
        saveButton.setOnClickListener(v -> saveGame());
        resumeButton.setOnClickListener(v -> toggleMenu(false));
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (normalSurfaceView != null && normalSurfaceView.getGameThread() != null) {
            normalSurfaceView.getGameThread().setRunning(false); // 停止游戏线程
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (normalSurfaceView != null && normalSurfaceView.getGameThread() != null) {
            normalSurfaceView.getGameThread().setRunning(true); // 恢复游戏线程
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (normalSurfaceView != null && normalSurfaceView.getGameThread() != null) {
            normalSurfaceView.getGameThread().setRunning(false); // 确保线程停止
            normalSurfaceView.getGameThread().interrupt(); // 中断线程以防未正常退出
        }
    }
    private void restartGame() {
        normalSurfaceView.restartGame(); // 在 SurfaceView 中实现 restartGame 方法
        toggleMenu(false);
    }

    private void saveGame() {
        // 保存游戏逻辑
        normalSurfaceView.saveGame();
    }

    private void toggleMenu(boolean show) {
        if (show) {
            normalSurfaceView.pauseGame(); // 暂停游戏逻辑
            menuOverlay.setVisibility(View.VISIBLE);
        } else {
            normalSurfaceView.resumeGame(); // 恢复游戏逻辑
            menuOverlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (menuOverlay != null && menuOverlay.getVisibility() == View.VISIBLE) {
            toggleMenu(false); // 如果菜单已经显示，则关闭菜单
        } else {
            super.onBackPressed(); // 否则执行默认的返回操作
        }
    }
}