package com.android.roguegame.MapStorage;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MapStorage{
    public static void SaveMap(int[][] mapData, String name, Context context){
        // 获取应用私有外部存储路径
        File file = new File(context.getExternalFilesDir(null), name);

        // 序列化二维数组并保存到文件
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(mapData);
            Log.d("GameData", "Map data saved to private external storage");
            Log.d("GameData", "Map saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] loadMap(Context context, String fileName) {
        int[][] mapData = null;

        try {
            // 获取 AssetManager 并打开文件
            InputStream inputStream = context.getAssets().open(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            // 读取对象
            mapData = (int[][]) objectInputStream.readObject();

            // 关闭流
            objectInputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapData;
    }
}

