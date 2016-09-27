package com.fare.eco.ui.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 解压文件
 * @author Xiao_V
 * @since 2015/7/24
 */
public class Unpacker {
    private final String zip;
    private final String loc;

    public Unpacker(String zipFile, String location) {
        zip = zipFile;
        loc = location;
        checkDir(""); // 判断location是否是目录
    }

    public void unzip() {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zip));
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                	checkDir(ze.getName()); // 是目录(文件夹)就创建
                } else { // else直接复制
                    FileOutputStream fout = new FileOutputStream(loc + ze.getName());
                    byte[] buffer = new byte[4096];
                    for (int c = zin.read(buffer); c != -1; c = zin.read(buffer)) {
                        fout.write(buffer, 0, c);
                    }
                    zin.closeEntry();
                    fout.close();
                }
            }
            Log.i("Unpacker", "Unpacker successed");
            zin.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /** 判断是否是目录并创建 */
    private void checkDir(String dir) {
        File f = new File(loc + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }
}