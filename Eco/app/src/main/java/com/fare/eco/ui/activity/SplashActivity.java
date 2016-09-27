package com.fare.eco.ui.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fare.eco.config.Constants;
import com.fare.eco.ui.BaseActivity;
import com.fare.eco.ui.R;
import com.fare.eco.ui.util.FileUtil;
import com.fare.eco.ui.util.Unpacker;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * splash 处理app启动耗时操作
 * @author Xiao_V
 * @since 2015/7/2
 *
 */
public class SplashActivity extends BaseActivity {
	
	private static final int SKIP_TO_ACTIVITY = 1001;
	
	private ImageView anim_image;
	private Timer mTimer;
	
	private Handler doActionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch ( msg.what) {
			case SKIP_TO_ACTIVITY:
				chooseToStartActivity();
				break;
				
			default:
				break;
			}
		}
	};

	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		createAppDir(); // 创建app文件夹

		init();
	}
	
	private void init() {
		anim_image = (ImageView) findViewById(R.id.anim_image);
		anim_image.setImageResource(R.drawable.splash);
		mTimer = new Timer();

		doActionHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!checkResDir("girls")) { // 在子线程中调用
					copyAssets("girls.zip");
					unpackZip("girls.zip");
				}
				
				AnimatorSet set = new AnimatorSet();
				set.playTogether(
						ObjectAnimator.ofFloat(anim_image, "scaleX", 1.5f),
						ObjectAnimator.ofFloat(anim_image, "scaleY", 1.5f));
				set.setDuration(2200);
				set.start();
				
//				Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.init_iamge_big);
//				anim_image.startAnimation(animation);

				setTimerTask();
			}
		}, 800);
	}
	
	private void chooseToStartActivity() {
		SharedPreferences preferences = getSharedPreferences("splash", Context.MODE_PRIVATE);
		boolean isFirstLaunch = preferences.getBoolean("isFirstLaunch", true);
		Intent intent;
		if (isFirstLaunch) {
			intent = new Intent(SplashActivity.this, WelcomeActivity.class);
		} else {
			intent = new Intent(SplashActivity.this, MainActivity.class);
		}
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			SplashActivity.this.finish();
		}
	}
	
	private void setTimerTask() {

		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message message = doActionHandler.obtainMessage();
				message.what = SKIP_TO_ACTIVITY;
				message.sendToTarget();
			}
		}, 2000);
	}
	
	/** 判断所需的 *文件夹* 是否存在 */
	private Boolean checkResDir(String fileName) {
		File resDir = new File(getExternalFilesDir(null), fileName);
		return resDir.exists() && resDir.isDirectory();
	}

	/**
	 * 将assets中的文件copy到缓存目录下(sd/Android/data)
	 * @param fileName
	 */
	private void copyAssets(String fileName) {
		AssetManager assetManager = getAssets();
		try {
			InputStream in;
			OutputStream out;
			in = assetManager.open(fileName);
			File outFile = new File(getExternalFilesDir(null), fileName);
			out = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			int read;
			try {
				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			in.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void unpackZip(String fileName) {
		File extDir = getExternalFilesDir(null); // Android/data/package
		if (extDir == null) {
			return;
		}
		Unpacker unpacker = new Unpacker(extDir.getPath() + "/" + fileName, extDir.getPath() + "/");
		unpacker.unzip();
	}

	@Override
	public void onBackPressed() {

	}
	
	/** 创建app文件夹 */
    private void createAppDir() {
        try {
            File file = new File(Constants.APP_ROOT);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileUtil.ensureAppPath(Constants.CAMERA_SAVE_DIR);
            FileUtil.ensureAppPath(Constants.IMAGE_CACHE_THUMB_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
