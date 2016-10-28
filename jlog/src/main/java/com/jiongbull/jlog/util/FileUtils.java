/*
 * Copyright JiongBull 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiongbull.jlog.util;

import com.jiongbull.jlog.JLog;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文件工具类.
 */
public class FileUtils {

    /** 读写文件的线程池，单线程模型. */
    private static ExecutorService sExecutorService;

    static {
        sExecutorService = Executors.newSingleThreadExecutor();
    }

    private FileUtils() {
    }

    /**
     * 判断文件或目录是否存在.
     *
     * @param filePath 路径
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(@NonNull String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建目录，若目录已存在则不处理.
     *
     * @param dirPath 目录路径
     * @return true - 目录存在（创建成功或已存在），false - 目录不存在
     */
    public static boolean createDir(@NonNull String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return file.exists();
    }

    /**
     * 把一连串文本写入文件中
     *
     * @param dirPath       目录路径
     * @param fileName      文件名
     * @param list          待写内容
     * @param isOverride    写入模式，true - 覆盖，false - 追加
     */
    public static void write(@NonNull final String dirPath, @NonNull final String fileName, @NonNull final List<String> list, final boolean isOverride) {
        for (String str : list) {
            write(dirPath, fileName, str, isOverride);
        }
    }

    /**
     * 把文本写入文件中.
     *
     * @param dirPath    目录路径
     * @param fileName   文件名
     * @param content    待写内容
     * @param isOverride 写入模式，true - 覆盖，false - 追加
     */
    public static void write(@NonNull final String dirPath, @NonNull final String fileName, @NonNull final String content, final boolean isOverride) {
        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                String filePath = dirPath + File.separator + fileName;
                FileOutputStream fos = null;
                try {
                    if (createDir(dirPath)) {
                        File file = new File(filePath);
                        boolean isExist = file.exists();
                        fos = new FileOutputStream(file, !(!isExist || isOverride));
                        fos.write(content.getBytes(JLog.getSettings().getCharset()));
                    }
                } catch (IOException e) {
                    Log.e("FileUtils", e.getMessage());
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            Log.e("FileUtils", e.getMessage());
                        }
                    }
                }
            }
        });
    }


    /**
     * 获取文件夹的大小
     *
     * @param dirPath 需要测量大小的文件夹地址
     * @return 返回文件夹大小，单位byte
     */
    public static long folderSize(String dirPath) {
        long length = 0;
        File directory = new File(dirPath);
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file.getAbsolutePath());
        }
        return length;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dirPath 将要删除的文件目录地址
     * @return 删除成功返回true，否则返回false
     */
    public static boolean deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isDirectory()) {
            String[] children = file.list();
            // 递归删除目录中的子目录下
            for (String aChildren : children) {
                boolean success = deleteDir(aChildren);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return file.delete();
    }
}