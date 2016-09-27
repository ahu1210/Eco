package com.fare.eco.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.fare.eco.config.Constants;
import com.fare.eco.externalLibrary.imageload.core.DisplayImageOptions;
import com.fare.eco.externalLibrary.imageload.core.ImageLoader;
import com.fare.eco.ui.R;

/**
 * 图片工具类
 * 
 * @author Xiao_V
 * @since 2015/7/2
 */
public class BitmapUtils {
	
	/**
	 * 图片缓存
	 * @param imageUrl
	 * @param imageView
	 */
	public static void loadImg(String imageUrl, ImageView imageView) {
		if (!TextUtils.isEmpty(imageUrl)) {
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
			ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
		} else {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		// Bitmap b = ImageLoader.getInstance().loadImageSync(imageUrl, options);
	}
	
	/**
	 * 压缩图片,像素压缩法
	 * @param picPath
	 * @param destWidth
	 * @param destHeight 768 * 1280 
	 * @return
	 */
	public static Bitmap compressImage(String picPath, int destWidth, int destHeight) {
		Bitmap sourceBitmap = null;
		do {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1; //1表示不缩放  
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(picPath, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}
			options.inSampleSize = computeSampleSize(options, destWidth, destHeight); // 缩放比
			options.inJustDecodeBounds = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565; // 默认值 ARGB_8888
			InputStream is = null;
			try {
				is = new FileInputStream(picPath);
				sourceBitmap = BitmapFactory.decodeStream(is, null, options);
				if (null == sourceBitmap) {
					break;
				}
			} catch (OutOfMemoryError e) {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				if (null != sourceBitmap && !sourceBitmap.isRecycled()) {
					sourceBitmap.recycle();
					sourceBitmap = null;
				}
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} while (false); // 作用：未知
		return sourceBitmap;
	}
    
	/**
	 * 质量压缩法
	 * @param bmp
	 * @param imageSize 单位为kb
	 * @return
	 */
    public static Bitmap compressImage2Quality(Bitmap bmp, int imageSize) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int quality = 90;
        while (baos.toByteArray().length / 1024 > imageSize) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset(); //清空baos  
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos); //这里压缩options%，把压缩后的数据存放到baos中  
            quality -= 10; //每次都减少10
            if (quality < 10) {
            	break;
            }
        }
        Log.i("BitmapUtils", "压缩后的bitmap的大小为：" + baos.toByteArray().length / 1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        return bitmap;  
    }  
	
	/**
	 * 计算缩放比
	 */
	private static int computeSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int w = options.outWidth; // 图片的宽高
		int h = options.outHeight;
		int candidateW = Math.round((float) w / (float) reqWidth);
		int candidateH = Math.round((float) h / (float) reqHeight);
		int candidate = Math.max(candidateW, candidateH);
		if (candidate == 0) return 1;
		return candidate;
	}

	/**
	 * 创建圆形图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap circleBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;

		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		return output;
	}
	
	/**
	 * 保存头像(用于修改头像) path: Constants.IMAGE_CACHE_THUMB_DIR / AVATER_NAME
	 * @param bmp
	 * @param picName
	 * @param path 图片保存的路径(文件夹名)
	 */
	public static File saveAvater(Bitmap bmp, String picName, String path) {
		if (TextUtils.isEmpty(path)) { // 没有sd卡,转为内部存储
			path = Constants.ECMC_FILE_PATH;
		}
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		File f = new File(path, picName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 60, out); // JPEG比PNG保存时空间占用小
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
	
	/**
	 * 从sd卡中读取图片,获取到bitmap对象后需要做非空验证,为空则读取默认图片
	 * @param path
	 * @param picName
	 * @return
	 */
	public static Bitmap getAvaterFromSD(String path, String picName) {
		Bitmap bitmap;
		if (TextUtils.isEmpty(path)) {
			path = Constants.IMAGE_CACHE_THUMB_DIR;
		}
		bitmap = BitmapFactory.decodeFile(path + picName);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeFile(Constants.ECMC_FILE_PATH + picName);
		}
		return bitmap;
	}
	
	/**
	 * 保存图片并返回该图片(文件形式)
	 * @param bmp
	 * @param fileName 文件名(一般为时间的毫秒数)
	 * @return
	 */
    public static File saveImage(Bitmap bmp, String fileName) {
        File appDir = new File(Constants.IMAGE_SAVE_DIR);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

	 /**
     * 生成缩略图 先对bitmap压缩 (用于动态生成 eg:头像list)
     * @param width
     * @param height
     * @return
     */
	public static Bitmap getImageThumbnail(Bitmap bmp, int width, int height) {
		// TODO 压缩
		return ThumbnailUtils.extractThumbnail(bmp, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	}
	
	
	/**
	 * 读取图片属性：旋转的角度 (拍照旋转)
	 * 
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片 if(readPictureDegree() != 0)
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if (angle == 0 || bitmap == null) {
			return bitmap;
		}

		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * 查询图片的本地绝对路径
	 * 
	 * @param uri 可由intent.getData()获取
	 * @return
	 * 
	 * 相册选取uri:content://media/external/images/media/322775
	 * 		4.4	content://com.android.providers.media.documents/document/image:3951
	 *      path:/storage/sdcard0/Pictures/meituan/xxx.jpg
	 */
	 @SuppressLint("NewApi")  
     public static String getImagePath(final Context context, final Uri uri) {  

         final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  

         // DocumentProvider  
         if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
             // ExternalStorageProvider  
             if (isExternalStorageDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 if ("primary".equalsIgnoreCase(type)) {  
                     return Environment.getExternalStorageDirectory() + "/" + split[1];  
                 }  

             }  
             // DownloadsProvider  
             else if (isDownloadsDocument(uri)) {  
                 final String id = DocumentsContract.getDocumentId(uri);  
                 final Uri contentUri = ContentUris.withAppendedId(  
                         Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  

                 return getDataColumn(context, contentUri, null, null);  
             }  
             // MediaProvider  
             else if (isMediaDocument(uri)) {  
                 final String docId = DocumentsContract.getDocumentId(uri);  
                 final String[] split = docId.split(":");  
                 final String type = split[0];  

                 Uri contentUri = null;  
                 if ("image".equals(type)) {  
                     contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("video".equals(type)) {  
                     contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
                 } else if ("audio".equals(type)) {  
                     contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
                 }  

                 final String selection = "_id=?";  
                 final String[] selectionArgs = new String[] { split[1] };  

                 return getDataColumn(context, contentUri, selection, selectionArgs);  
             }  
         }  
         // MediaStore (and general)  
         else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {  
             // Return the remote address  
             if (isGooglePhotosUri(uri))  
                 return uri.getLastPathSegment();  

             return getDataColumn(context, uri, null, null);  
         }  
         // File  
         else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {  
             return uri.getPath();  
         }  

         return null;  
     }  
	 
     public static String getDataColumn(Context context, Uri uri, String selection,  
             String[] selectionArgs) {  
         Cursor cursor = null;  
         final String column = "_data";  
         final String[] projection = { column };  

         try {  
             cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);  
             if (cursor != null && cursor.moveToFirst()) {  
                 final int index = cursor.getColumnIndexOrThrow(column);  
                 return cursor.getString(index);  
             }  
         } finally {  
             if (cursor != null)  
                 cursor.close();  
         }  
         return null;  
     }  

     /** content://com.android.providers.media.documents/document/image:3951 */
     public static boolean isExternalStorageDocument(Uri uri) {  
         return "com.android.externalstorage.documents".equals(uri.getAuthority());  
     }  

     public static boolean isDownloadsDocument(Uri uri) {  
         return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
     }  

     public static boolean isMediaDocument(Uri uri) {  
         return "com.android.providers.media.documents".equals(uri.getAuthority());  
     }  

     public static boolean isGooglePhotosUri(Uri uri) {  
         return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
     }  
     
     /**
 	 * Drawable对象转化Bitmap对象
 	 * @param drawable
 	 * @return 
 	 */
 	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
 	}

 	/**
 	 * Bitmap对象转化byte数组
 	 * @param bitmap Bitmap对象
 	 * @return byte数组
 	 */
 	public static byte[] bitmapToBytes(Bitmap bitmap) {
 		if (null == bitmap) {
 			return null;
 		}
 		ByteArrayOutputStream baos = new ByteArrayOutputStream();
 		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
 		return baos.toByteArray();
 	}

 	/**
 	 * Byte数组转化为Bitmap对象
 	 * @param data byte数组
 	 * @return Bitmap对象
 	 */
 	public static Bitmap bytesToBimap(byte[] data) {
 		if (data.length != 0) {
 			return BitmapFactory.decodeByteArray(data, 0, data.length);
 		} else {
 			return null;
 		}
 	}
}
