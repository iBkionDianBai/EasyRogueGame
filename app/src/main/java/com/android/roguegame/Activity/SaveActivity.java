package com.android.roguegame.Activity;

import static com.android.roguegame.Save.LoadStorage.loadStorage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.android.roguegame.R;
import com.android.roguegame.Save.GameData;
import com.android.roguegame.Save.LoadStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save);

        ListView listView = findViewById(R.id.lvSaveFiles);
        File directory = getFilesDir();
        File[] files = directory.listFiles();

        List<String> saveFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".json")) {
                    saveFiles.add(file.getName().replace(".json", ""));
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, saveFiles);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String saveName = saveFiles.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("saveName", saveName);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}