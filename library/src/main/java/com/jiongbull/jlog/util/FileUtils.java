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

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static android.content.ContentValues.TAG;

/**
 * 文件工具.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtils {

    private static final int BUFFER_SIZE = 1024;
    /** 压缩文件的扩展名. */
    public static final String ZIP_EXT = ".zip";

    private FileUtils() {
    }

    /**
     * 判断文件是否存在.
     *
     * @param filePath 路径
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(@NonNull String filePath) {
        return isExist(new File(filePath));
    }

    /**
     * 判断文件是否存在.
     *
     * @param file 文件
     * @return true - 存在，false - 不存在
     */
    public static boolean isExist(@NonNull File file) {
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
     * 计算文件的大小.
     *
     * @param dirPath 待测量文件的路径
     * @return 文件的大小，单位byte
     */
    public static long calSize(@NonNull String dirPath) {
        File directory = new File(dirPath);
        return calSize(directory);
    }

    /**
     * 计算文件的大小.
     *
     * @param directory 待测量文件
     * @return 文件的大小，单位byte
     */
    public static long calSize(@NonNull File directory) {
        long size = 0L;
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    size += calSize(file);
                } else {
                    size += file.length();
                }
            }
        } else if (directory.isFile()) {
            size += directory.length();
        }
        return size;
    }

    /**
     * 压缩文件.
     *
     * @param sourcePath 源文件的路径
     * @param destPath   目标文件路径
     * @param isClean    压缩完毕后是否清理
     */
    public static void zip(@NonNull String sourcePath, @NonNull String destPath, boolean isClean) throws IOException {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(destPath));
            File sourceFile = new File(sourcePath);
            zip(zos, sourceFile, null);
            if (isClean) {
                boolean deleteResult = delete(sourceFile); // 压缩完毕后删除
                if (!deleteResult) {
                    Log.e(TAG, "delete file failed");
                }
            }
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }

    /**
     * 压缩文件.
     *
     * @param zos        ZipOutputStream
     * @param fileToZip  待压缩的文件
     * @param folderPath 父路径，可以为null
     * @throws IOException 压缩失败
     */
    private static void zip(ZipOutputStream zos, File fileToZip, String folderPath) throws IOException {
        String zipEntryName = fileToZip.getName();
        if (!TextUtils.isEmpty(folderPath)) {
            zipEntryName = folderPath + File.separator + fileToZip.getName();
        }
        if (fileToZip.isDirectory()) {
            for (File file : fileToZip.listFiles()) {
                zip(zos, file, zipEntryName);
            }
        } else {
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                FileInputStream fis = new FileInputStream(fileToZip);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);
                zos.putNextEntry(new ZipEntry(zipEntryName));
                int length;
                while ((length = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            } finally {
                IOUtils.closeQuietly(bis);
            }
        }
    }

    /**
     * 获取目录下所有的压缩文件.
     *
     * @param logDir 日志目录
     * @return 压缩文件数组
     */
    public static File[] getZipFiles(@NonNull File logDir) {
        FilenameFilter zipFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(ZIP_EXT);
            }
        };
        return logDir.listFiles(zipFilter);
    }

    /**
     * 删除文件.
     *
     * @param file 将要删除的文件
     * @return true - 删除成功，false - 删除失败
     */
    public static boolean delete(File file) {
        boolean success = true;
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                success &= delete(subFile);
            }
            return success;
        }
        return file.delete();
    }

    /**
     * 按文件的最后修改时间正序排序，越近的越靠前，越早的越靠后.
     *
     * @param files 待排序的文件
     */
    public static void sortByModifyDate(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    /**
     * 按文件的最后修改时间倒序排序，越近的越靠后，越早的越靠前.
     *
     * @param files 待排序的文件
     */
    public static void sortByModifyDateDesc(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return 1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }
}