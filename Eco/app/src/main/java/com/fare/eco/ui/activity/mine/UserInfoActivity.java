package com.fare.eco.ui.activity.mine;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fare.eco.config.Constants;
import com.fare.eco.externalLibrary.volley.Request;
import com.fare.eco.externalLibrary.volley.RequestQueue;
import com.fare.eco.externalLibrary.volley.Response.ErrorListener;
import com.fare.eco.externalLibrary.volley.Response.Listener;
import com.fare.eco.externalLibrary.volley.VolleyError;
import com.fare.eco.externalLibrary.volley.toolbox.Volley;
import com.fare.eco.manager.http.MultiPartStack;
import com.fare.eco.manager.http.MultiPartStringRequest;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.util.BitmapUtils;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.util.TakePicture;
import com.fare.eco.ui.view.TitleBarLayout;
import com.fare.eco.ui.view.TitleBarLayout.LeftAction;
import com.fare.eco.ui.view.TitleBarLayout.OnTitleBarLeftClickListener;
import com.fare.eco.ui.view.widget.CircleImageView;

public class UserInfoActivity extends BaseActivity implements Runnable, View.OnClickListener{
	
	private static final String TAG = UserInfoActivity.class.getSimpleName();
	private static final boolean DEBUG = true;

	/** 头像保存到本地的名称(TODO:用MD5加密) */
	public static final String AVATER_NAME = "headImage.jpeg";
	/** 头像的本地路径 */
	private static final String AVATER_PATH = Constants.IMAGE_CACHE_THUMB_DIR + "/" + AVATER_NAME;

	private TitleBarLayout titleBar;
	/** 头像及其所在的布局 */
	private LinearLayout head_ll;
	private CircleImageView head_img;
	/** 拍照或相册选取的图片的路径 */
	private String path;
	private File avaterFile = new File(AVATER_PATH);
	/** 用户名和昵称 */
	private TextView userName, nickName;
	private Bitmap bmp;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // TODO 常量表示
				if (DEBUG) Log.i(TAG, "get bitmap from thread");
				if (bmp != null) head_img.setImageBitmap(bmp);
				String imageUrl = "http://192.168.1.100:8080/UploadAvater/servlet/Upload";
				if (!avaterFile.exists()) {
					ShowUtil.showToast(UserInfoActivity.this, "!avaterFile.exists");
				} else {
					send2Server(imageUrl, avaterFile);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initView();
		
		// TODO 从给定uri(本地或网络)加载缓存头像
		if (avaterFile.exists()) {
			if (DEBUG) Log.i(TAG, "avaterFile.length = " + avaterFile.length());
			
			BitmapFactory.Options options = new Options();
			options.inJustDecodeBounds = true;
			options.inSampleSize = 2;
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			Bitmap avaterBmp = BitmapFactory.decodeFile(AVATER_PATH, options);
			head_img.setImageBitmap(avaterBmp);
//			BitmapUtils.loadAvater("file:///mnt/sdcard/Fare/imageCache/thumbnail/headImage.jpeg", head_img);
		} else {
			if (DEBUG) Log.i(TAG, "else avaterFile not exists");
		}
		
		titleBar.setTitle("个人资料");
		titleBar.setLeftAction(LeftAction.BACK);
		
		setListener();
	}
	
	protected void initView() {
		titleBar = (TitleBarLayout) findViewById(R.id.titleBar);
		head_ll = (LinearLayout) findViewById(R.id.head_ll);
		head_img = (CircleImageView) findViewById(R.id.head_img);
		userName = (TextView) findViewById(R.id.tv_username);
		nickName = (TextView) findViewById(R.id.tv_nickname);

		userName.setText("13843855438");
		nickName.setText("西门吹雪");
	}
	
	private void setListener() {
		titleBar.setOnTitleBarLeftClickListener(new OnTitleBarLeftClickListener() {

			@Override
			public void onLeftClickListener(View view) {
				finish();
			}
		});

		head_ll.setOnClickListener(this);
	}

	/** different way to get photo */
	public void choosePicture(final Activity activity) {
		final AlertDialog dialog = new Builder(activity).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_choose_picture);

		window.findViewById(R.id.btn_camera).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						TakePicture.getInstance().takePictureFromCamare(activity, Constants.CAMERA);
						dialog.dismiss();
					}
				});

		window.findViewById(R.id.btn_album).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						TakePicture.getInstance().takePictureFromAlbum(activity, Constants.ALBUM);
						dialog.dismiss();
					}
				});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			ShowUtil.showToast(this, "请选择图片");
			return;
		}
		if (requestCode == Constants.ALBUM) {
			setAvater(data.getData());
		}
		if (requestCode == Constants.CAMERA) {
			Uri uri;
			if (data != null && data.getData() != null) {
				uri = data.getData();
			} else {
				uri = TakePicture.getInstance().getImageUri();
			}
			if (uri == null) {
				return;
			}
			setAvater(uri);
		}
	}

	// http://[ip]:[port]/UploadAvater/servlet/Upload
	private void setAvater(Uri uri) {
		path = BitmapUtils.getImagePath(this, uri); // 图片路径
		if (path == null) {
			return;
		}
		if (DEBUG) Log.i(TAG, uri + "    path=" + path);
		
		new Thread(this).start();
	}
	
	private void send2Server(String url, final File avaterFile) {
		RequestQueue queue = Volley.newRequestQueue(this, new MultiPartStack());
		if (DEBUG) Log.i(TAG, "new volley");
	    MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
	            Request.Method.POST, url, new Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response == null) {
							if (DEBUG) Log.i(TAG, "response == null");
							return;
						}
						Log.i(TAG, response + "---");
						 // TODO 解析response得到下载头像的url,或者固定图片的url
//						BitmapUtils.loadAvater(response, head_img);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (DEBUG) Log.i(TAG, "error");
					}
				}) {

	        @Override
	        public Map<String, File> getFileUploads() {
	        	if (DEBUG) Log.i(TAG, "override getFileUploads");
	        	Map<String, File> map = new HashMap<String, File>();
	        	map.put("avater", avaterFile);
	            return map;
	        }

	    };

	    queue.add(multiPartRequest);
	}
	
	@Override
	public void run() {
		bmp = BitmapUtils.compressImage(path, 640, 853);
		bmp = BitmapUtils.compressImage2Quality(bmp, 100);
		avaterFile = BitmapUtils.saveAvater(bmp, AVATER_NAME, Constants.IMAGE_CACHE_THUMB_DIR);
		
		if (bmp != null) {
			Message message = handler.obtainMessage();
			message.what = 1; // TODO 常量表示
			handler.sendMessage(message);
			
			// TODO 广播只用来通知,通知前将所需要的数据(eg:imageUrl)保存到sharePreference
			Intent intent = new Intent(Constants.CHANGE_AVATER_ACION); // 广播action
			intent.putExtra("imagePath", AVATER_PATH); // 上传成功后传递url
			sendBroadcast(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.head_ll:
			choosePicture(UserInfoActivity.this);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bmp != null) {
			bmp.recycle();
		}
		bmp = null;
		TakePicture.destroyInstance();
	}
}