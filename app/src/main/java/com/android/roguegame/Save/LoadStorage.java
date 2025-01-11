package com.android.roguegame.Save;

import android.app.GameState;
import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadStorage {
    public static GameData loadStorage(Context context,String saveName) {
        try {
            String fileName = saveName + ".json"; // 根据存档名生成文件名
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            bufferedReader.close();
            reader.close();
            fis.close();

            Gson gson = new Gson();
            return gson.fromJson(jsonBuilder.toString(), GameData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<String> getSavedFiles(Context context) {
        try {
            String[] files = context.fileList();
            List<String> saveFiles = new ArrayList<>();
            for (String file : files) {
                if (file.endsWith(".json")) {
                    saveFiles.add(file.replace(".json", "")); // 去掉后缀只显示存档名
                }
            }
            return saveFiles;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
