package com.fare.eco.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fare.eco.config.Constants;

import android.util.Log;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

/**
 * 文件操作
 * @author Xiao_V
 * @since 2015/8/29
 */
public class FileUtil {

	private static final String TAG = "FileUtil";
	private static final boolean DEBUG = false;
	
	/** 图片的后缀名 */
	private static final String IMAGE_SUFFIX_JPG = ".jpg";

	/**
	 * 判断SD卡是否存在
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/** 创建文件 dir为文件目录 */
	public File createFileOnSDCard(String fileName, String dir) throws IOException {
		File file = new File(Constants.SDCARD_PATH + File.separator + dir + File.separator + fileName);
		if (DEBUG) Log.v("createFileOnSDCard", Constants.SDCARD_PATH + File.separator + dir + File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 检查是否有足够的存储空间
	 * 
	 * @param path 文件路径
	 * @param needSize 所需空间
	 * @return
	 */
	public static boolean hasFreeSpace(String path, long needSize) {
		if (TextUtils.isEmpty(path)) {
			return true;
		}

		long freeSpace = 0;

		if (path.startsWith(Environment.getRootDirectory().getAbsolutePath())) {
			// /system 是否需要先判断该分区是否可写
			freeSpace = getFreeSpaceOfInternalStorage();
		} else if (path.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
			// /mnt/sdcard
			freeSpace = getFreeSpaceOfExternalStorage();
		} else if (path.startsWith(Environment.getDataDirectory().getAbsolutePath())) {
			// /data
			freeSpace = getFreeSpaceOfAppData();
		} else {
			freeSpace = 0;
		}

		return freeSpace >= needSize;
	}

	/**
	 * 获取获取指定目录剩余空间大小 返回值单位字节
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getFreeSpaceOfPath(File fileDir) {
		StatFs sf = new StatFs(fileDir.getPath());
		long blockSize = sf.getBlockSize();
		long availableCount = sf.getAvailableBlocks();
		long freeSpace = availableCount * blockSize;

		return freeSpace;
	}

	/**
	 * 获取外部存储剩余空间大小 (单位字节)
	 * 
	 * @return
	 */
	public static long getFreeSpaceOfExternalStorage() {
		File sdcardDir = Environment.getExternalStorageDirectory();
		return getFreeSpaceOfPath(sdcardDir);
	}

	/**
	 * 获取内部存储剩余空间大小 (单位字节)
	 * 
	 * @return
	 */
	public static long getFreeSpaceOfInternalStorage() {
		// 获取 Android 的根目录/system
		File rootDir = Environment.getRootDirectory();
		return getFreeSpaceOfPath(rootDir);
	}

	/**
	 * 获取应用数据目录剩余空间大小 (单位字节)
	 * 
	 * @return
	 */
	public static long getFreeSpaceOfAppData() {
		// 获取 Android 数据目录。
		File dataDir = Environment.getDataDirectory();
		return getFreeSpaceOfPath(dataDir);
	}

	/**
	 * 格式化网络文件名
	 * 
	 * @param url 网络url
	 * @param fileSuffix 文件后缀名 eg: .jpg  .png
	 * @return
	 */
	public static String convertUrlToFileName(String url, String fileSuffix) {

		if (TextUtils.isEmpty(url)) {
			return "";
		}

		String filename = url;
		final Pattern p = Pattern.compile("(?<=v=)[\\w]{1,}(?=&|$)"); // regEx根据实际情况决定
		final Matcher m = p.matcher(filename);
		if (m.find()) {
			final int beginIndex = m.start();
			final int endIndex = m.end();
			filename = filename.substring(beginIndex, endIndex);
		}

		filename = filename.replaceAll("/", ".").replaceAll(":", "");
		filename = filename.replaceAll("\\?", "_");
		filename = filename.replaceAll("=", "_");

		if (!TextUtils.isEmpty(fileSuffix) && !filename.endsWith(fileSuffix)) {
			filename = filename + fileSuffix;
		}

		return filename;
	}

	/**
	 * Make sure APP root is created
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean ensureAppPath(String uri) {
		if (DEBUG) Log.i(TAG, "ensureAppPath uri:" + uri);
		String dataDir = Environment.getDataDirectory().getAbsolutePath(); // 获取data文件的绝对路径 -- /data
		if (DEBUG) Log.i(TAG, "data dir:" + dataDir);
		if ((dataDir).equals(uri.substring(0, 5))) {
			File file = new File(uri);
			if (!file.exists()) {
				boolean success = file.mkdirs();
				if (!success) {
					return false;
				}
			}
		} else { // 一般会走else
			String sdcardDir = Environment.getExternalStorageDirectory().getAbsolutePath(); // sd卡 --/storage
			if (DEBUG) Log.i(TAG, "ExtStoragePath:" + sdcardDir);
			if (DEBUG) Log.i(TAG, "BASE_PATH:" + Constants.APP_ROOT);
			if (new File(sdcardDir).canRead()) {
				File file = new File(Constants.APP_ROOT);
				if (!file.exists()) {
					boolean success = file.mkdirs();
					if (!success) {
						return false;
					}
				}
				// Prevent system media library scan (屏蔽系统媒体库扫描)
				file = new File(Constants.APP_ROOT + "/.nomedia");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				file = new File(uri);
				if (!file.exists()) {
					boolean success = file.mkdirs();
					if (!success) {
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * 获取唯一的文件名
	 * 
	 * @param type 文件类型(.jpg)
	 * @param tempName (前缀名 eg:icon)
	 * @return
	 */
	public static String getUniqueName(String type, String tempName) {
		int i = new Random().nextInt(10000000);
		StringBuilder newName = new StringBuilder();
		newName.append(tempName);
		newName.append("_");
		newName.append(i);
		newName.append(type);

		while (true) { // 检查目录中是否存在生成的name,若存在i++,不存在则创建
			File file = new File(Constants.IMAGE_SAVE_DIR + "/" + newName.toString());
			File file1 = new File(Constants.CAMERA_SAVE_DIR + "/" + newName.toString());
			File file2 = new File(Constants.IMAGE_CACHE_THUMB_DIR + "/" + newName.toString());
			if (file1.exists() || file2.exists() || file.exists()) {
				i++;
				newName = new StringBuilder();
				newName.append(tempName);
				newName.append("_");
				newName.append(i);
				newName.append(type);
			} else {
				break;
			}
		}
		return newName.toString();
	}

	public static String newFilePath(String url, String dir) {
		String fileName = convertUrlToFileName(url, IMAGE_SUFFIX_JPG);
		String dirPath = dir;
		File dirFile = new File(dirPath);
		dirFile.mkdirs();
		String pathFileName = dirPath + "/" + fileName;
		return pathFileName;
	}

	/**
	 * 文件的复制操作方法
	 * @param fromFile 被复制的文件
	 * @param toFile 复制的目录文件
	 */
	public static boolean copyFile(String fromFilePath, String toFilePath) {
		boolean isCopySuccess = false;
		File fromFile = new File(fromFilePath);
		if (!fromFile.exists() || !fromFile.isFile() || !fromFile.canRead()) {
			return isCopySuccess;
		}
		File toFile = new File(toFilePath);
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);

			byte[] buffer = new byte[1024];
			int c;
			while ((c = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, c);
			}
			fis.close();
			fos.close();
			isCopySuccess = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isCopySuccess;

	}

	/**
	 * 获取文件的类型 eg: .html .zip gif
	 * @param strpath文件路径
	 * @return 文件的类型
	 */
	public static String getFileMiMeType(String strpath) {
		if (TextUtils.isEmpty(strpath))
			return null;

		String extension = MimeTypeMap.getFileExtensionFromUrl(strpath);
		if (extension != null) {
			MimeTypeMap mime = MimeTypeMap.getSingleton();
			return mime.getMimeTypeFromExtension(extension);
		}
		return null;
	}

	public static long getFileSize(File f) {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				s = fis.available();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return s;
	}
	
	/** 日志写入文件 */
    public static void writeLog(String str, String logFileName){
        File file = new File(Constants.APP_ROOT, logFileName);  // "ce_log.txt"
        try {  
            file.createNewFile();
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        byte bt[] = new byte[1024];  
        bt = str.getBytes();  
        try {  
            FileOutputStream fos = new FileOutputStream(file);  
            try {  
                fos.write(bt, 0, bt.length);  
                fos.close();  
                if (DEBUG) Log.i(TAG, "write log success");  
            } catch (IOException e) {  
                e.printStackTrace();  
                if (DEBUG) Log.i(TAG, "write log fail");
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }
    }

}
