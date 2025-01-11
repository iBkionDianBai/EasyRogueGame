package com.android.roguegame.Save;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class SaveStorage {

    public static boolean saveStorage(Context context, GameData gameData, String saveName) {
        try {
            // 根据存档名生成文件名
            String fileName = saveName + ".json";
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            // 将 GameData 对象序列化为 JSON
            Gson gson = new Gson();
            String json = gson.toJson(gameData);

            // 写入文件
            writer.write(json);
            writer.flush();
            writer.close();
            fos.close();

            return true; // 保存成功
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 保存失败
        }
    }
}
