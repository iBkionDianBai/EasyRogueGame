package com.android.roguegame.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.roguegame.MapStorage.MapStorage;
import com.android.roguegame.R;
import com.android.roguegame.ui.GameTextTutorial;
import com.android.roguegame.ui.ItemFreezeSurfaceView;
import com.android.roguegame.ui.ItemKeySurfaceView;
import com.android.roguegame.ui.ItemTreasureSurfaceView;
import com.android.roguegame.ui.TutorialSurfaceView;
import com.android.roguegame.ui.MonsterSurfaceView;
import com.android.roguegame.ui.PlayerSurfaceView;

public class TutorialActivity extends AppCompatActivity {
    // 地图组
    private String Tutorial_map_name;
    private int[][] mapData;

    private TutorialSurfaceView tutorialSurfaceView;
    private PlayerSurfaceView player;
    private MonsterSurfaceView monster;
    private ItemFreezeSurfaceView freeze;
    private ItemKeySurfaceView key;
    private ItemTreasureSurfaceView treasure;

    private GameTextTutorial tutorialView;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorial);

        // 地图组初始化
        Tutorial_map_name = "tutorial_level.dat";
        mapData = MapStorage.loadMap(this,Tutorial_map_name);

        // 初始化游戏界面
        tutorialSurfaceView = findViewById(R.id.mapView);
        // 设置地图
        tutorialSurfaceView.setMapData(mapData);

        // 初始化玩家
        player = new PlayerSurfaceView(this,mapData,1,1);
        // 初始化怪物
        monster = new MonsterSurfaceView(this,mapData,2,1);
        // 初始化道具
        freeze = new ItemFreezeSurfaceView(this,mapData,3,1);

        key = new ItemKeySurfaceView(this,mapData,4,1);
        // 初始化宝物
        treasure = new ItemTreasureSurfaceView(this,mapData,1,2);

        tutorialSurfaceView.setPlayer(player);
        tutorialSurfaceView.setMonster(monster);
        tutorialSurfaceView.setFreeze(freeze);
        tutorialSurfaceView.setKey(key);
        tutorialSurfaceView.setTreasure(treasure);
        tutorialSurfaceView.post(()-> tutorialSurfaceView.drawMap());

        // 初始化提示词
        String[] messages = {
                "Welcome to Rogue Game!",
                "First is Player.\nWhat you need to motivate.",
                "Second is Monster.\nTry Avoid to let it get close to you!",
                "Third is Item-Freeze.\nIt could let monster stop for 5 steps you take.",
                "Forth is Key.\nYou need to get at least 3 key to escape\nafter you get the Treasure!",
                "And the last is Treasure.\nGet it and escape with full body!",
                "Have Fun in Rogue Game!",
        };
        tutorialView = findViewById(R.id.TextView);
        tutorialView.initialize(32, messages);
        tutorialView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                tutorialView.nextTutorial();
                return true;
            }
            return false;
        });
    }

}