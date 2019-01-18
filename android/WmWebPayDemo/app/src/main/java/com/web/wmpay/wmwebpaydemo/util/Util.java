package com.web.wmpay.wmwebpaydemo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by East.K on 2018/10/10.
 */

public class Util {

    /**
     * 获取 APP 的版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "";
        if (context == null) return version;
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 判断字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        if (text == null || text.equals("")) return true;
        return false;
    }

    /**
     * 字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(Object text) {
        if (text == null) return true;
        return isEmpty(text.toString());
    }

    /**
     * 打电话，跳拨号界面
     *
     * @param context
     * @param phoneNum
     */
    public static void callPhone(Context context, String phoneNum) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回File 如果没有就创建
     *
     * @return directory
     */
    public static File getDirectory(String path) {
        File appDir = new File(path);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    /**
     * 返回数据目录
     *
     * @return
     */
    public static String getDataPath(Context context) {
        return "/data/data/" + context.getPackageName() + "/";
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param negative
     * @param neutral
     * @param positive
     * @param listener
     */
    public static AlertDialog show(Context context,
                                   String title,
                                   String message,
                                   String negative,
                                   String neutral,
                                   String positive,
                                   DialogInterface.OnClickListener listener) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        if (!isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!isEmpty(message)) {
            builder.setMessage(message);
        }
        if (!isEmpty(negative)) {
            builder.setNegativeButton(negative, listener);
        }
        if (!isEmpty(neutral)) {
            builder.setNeutralButton(neutral, listener);
        }
        if (!isEmpty(positive)) {
            builder.setPositiveButton(positive, listener);
        }
        return builder.show();
    }

    /**
     * @param rootPath
     * @param file
     * @param isIgnoreEntryError 是否忽略zip内单个文件加压失败
     * @return
     */
    public static boolean unzip(String rootPath, File file, boolean isIgnoreEntryError) {
        int BUFFER = 4 * 1024; // 这里缓冲区我们使用4KB，
        ZipInputStream zis = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            BufferedOutputStream dest = null; // 缓冲输出流
            ZipEntry entry; // 每个zip条目的实例

            while ((entry = zis.getNextEntry()) != null) {
                try {
                    int count;
                    byte data[] = new byte[BUFFER];
                    //文件夹
                    if (entry.isDirectory()) {
                        File directoryFile = new File(rootPath + entry.getName().substring(0, entry.getName().length() - 1));
                        directoryFile.mkdir();
                    } else {
                        File entryFile = new File(rootPath + entry.getName());// 解压后的文件名用原文件名
                        File entryDir = new File(entryFile.getParent());
                        if (!entryDir.exists()) {
                            entryDir.mkdirs();
                        }
                        FileOutputStream fos = new FileOutputStream(entryFile);
                        dest = new BufferedOutputStream(fos, BUFFER);
                        while ((count = zis.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, count);
                        }
                        dest.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (!isIgnoreEntryError) {
                        return false;
                    }
                } finally {
                    if (dest != null) {
                        dest.close();
                    }
                }
            }
        } catch (Exception cwj) {
            cwj.printStackTrace();
            return false;
        } finally {
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
