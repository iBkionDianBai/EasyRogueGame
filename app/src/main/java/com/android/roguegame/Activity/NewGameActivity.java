package com.android.roguegame.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.roguegame.R;

public class NewGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_game);

        // 初始化按钮
        Button diff_tutorial_btn = findViewById(R.id.diffTutorial);
        Button diff_normal_btn = findViewById(R.id.diffNormal);

        diff_tutorial_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 打开教程窗口
                Intent intent = new Intent(NewGameActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });

        diff_normal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(NewGameActivity.this, NormalActivity.class);
                startActivity(intent);
            }
        });
    }
}