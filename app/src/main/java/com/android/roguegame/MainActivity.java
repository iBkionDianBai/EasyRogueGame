package com.android.roguegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.roguegame.Activity.NewGameActivity;
import com.android.roguegame.Activity.NormalActivity;
import com.android.roguegame.Activity.SaveActivity;
import com.android.roguegame.Activity.SettingsActivity;
import com.android.roguegame.ui.NormalSurfaceView;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private NormalSurfaceView normalSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        normalSurfaceView = findViewById(R.id.normal_surface_view);

        // 如果 SDK 版本大于等于 23，动态请求存储权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查是否有写外部存储权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限，申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            }
        }

        // 按钮初始化
        Button new_game_btn = findViewById(R.id.homePageNewGame);
        Button load_game_btn = findViewById(R.id.homePageLoadGame);
        Button settings_btn = findViewById(R.id.homePageSettings);

        new_game_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 打开新游戏界面
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                startActivity(intent);
            }
        });

        load_game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        settings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            String saveName = data.getStringExtra("saveName");
            if (saveName != null && !saveName.isEmpty()) {
                // 跳转到 NormalActivity，并传递存档名
                Intent intent = new Intent(MainActivity.this, NormalActivity.class);
                intent.putExtra("saveName", saveName);
                startActivity(intent);
            }
        }
    }
}