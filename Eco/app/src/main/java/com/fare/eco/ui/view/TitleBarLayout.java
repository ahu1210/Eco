package com.fare.eco.ui.view;

import com.fare.eco.config.Constants;
import com.fare.eco.ui.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 标题栏布局
 * 
 */
public class TitleBarLayout extends LinearLayout implements OnGlobalLayoutListener {

	// 标题栏左侧的监听事件
	public interface OnTitleBarLeftClickListener {
		public void onLeftClickListener(View view);
	}

	// 标题栏右侧的监听事件
	public interface OnTitleBarRightClickListener {
		public void onRightClickListener(View view);
	}

	/**
	 * 左边按钮动作,枚举来定义
	 */
	public static enum LeftAction {
		/**
		 * 返回
		 */
		BACK,

		/**
		 * 侧滑栏开关
		 */
		ICON,

		/**
		 * 不显示
		 */
		GONE;
	}

	/**
	 * 右边按钮样式
	 */
	public static enum RightAction {
		/**
		 * 显示文本
		 */
		TEXT,

		/**
		 * 显示图片
		 */
		ICON,

		/**
		 * 不显示
		 */
		GONE;
	}

	OnTitleBarLeftClickListener mOnTitleBarLeftClickListener = null;
	OnTitleBarRightClickListener mOnTitleBarRightClickListener = null;

	public void setOnTitleBarLeftClickListener(
			OnTitleBarLeftClickListener onTitleBarLeftClickListener) {
		mOnTitleBarLeftClickListener = onTitleBarLeftClickListener;
	}

	public void setOnTitleBarRightClickListener(
			OnTitleBarRightClickListener onTitleBarRightClickListener) {
		mOnTitleBarRightClickListener = onTitleBarRightClickListener;
	}

	FrameLayout mRoot = null;

	/**
	 * 左边按钮
	 */
	Button btn_back = null;
	
	/**
	 * 右边按钮
	 */
	ImageView imageRight = null;

	/**
	 * 中间标题
	 */
	TextView txtMiddle = null;

	/**
	 * 右边按钮
	 */
	TextView txtRight = null;
	
	/**
	 * 自定义View，取代中间/左边的标题
	 */
	LinearLayout customView_mid = null;
	LinearLayout customView_left = null;

	int mWidth = 0;

	String mTitleText = "";

	private SharedPreferences sp;

	public TitleBarLayout(Context context) {
		super(context);
		if (isInEditMode()) {
			return;
		}
		initViews(context, null);
		bindListener();
	}

	public TitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		initViews(context, null);
		bindListener();
	}

	public void initViews(Context context, View root) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		setOrientation(LinearLayout.HORIZONTAL);
		layoutInflater.inflate(R.layout.title_bar, this);

		mRoot = (FrameLayout) findViewById(R.id.title_bar_layout);
		btn_back = (Button) findViewById(R.id.button_backward);
		imageRight = (ImageView) findViewById(R.id.title_bar_right_icon);
		txtMiddle = (TextView) findViewById(R.id.title_bar_middle);
		txtRight = (TextView) findViewById(R.id.title_bar_right_text);
		customView_mid = (LinearLayout) findViewById(R.id.title_bar_custom_layout_mid);
		customView_left = (LinearLayout) findViewById(R.id.title_bar_custom_layout_left);

		sp = context.getSharedPreferences(Constants.SHARE_NAME_TITLEBAR_COLOR, Context.MODE_PRIVATE);
		setTitleBarColor(sp.getInt("color", R.color.toolbar_color)); // 从sp中获取到设置的背景色
	}

	/** 设置标题栏的背景色 */
	public void setTitleBarColor(int resId) {
		setTitleBarColorAndAlpha(resId, 255);
	}

	public void setTitleBarAlpha(int alpha) {
		setTitleBarColorAndAlpha(0, alpha);
	}

	public void setTitleBarColorAndAlpha(int resId, int alpha) {
		if (resId > 0) {
			mRoot.setBackgroundColor(getResources().getColor(resId));
		}
		if (mRoot.getBackground() != null) {
			mRoot.getBackground().setAlpha(alpha);
		}

	}

	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnTitleBarLeftClickListener != null) {
					// 点击时回调(好处:不需要实例化组件,通过setAction设置组件的类型,然后直接设置本类对象的监听事件即可)
					mOnTitleBarLeftClickListener.onLeftClickListener(v);
				}
			}
		});
		
		customView_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnTitleBarLeftClickListener != null) {
					// 点击时回调(好处:不需要实例化组件,通过setAction设置组件的类型,然后直接设置本类对象的监听事件即可)
					mOnTitleBarLeftClickListener.onLeftClickListener(v);
				}
			}
		});

		txtRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnTitleBarRightClickListener != null) {
					mOnTitleBarRightClickListener.onRightClickListener(v);
				}
			}
		});

		imageRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnTitleBarRightClickListener != null) {
					mOnTitleBarRightClickListener.onRightClickListener(v);
				}
			}
		});

		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	public TextView getTitleTextView() {
		return txtMiddle;
	}

	public TextView getRightTextView() {
		return txtRight;
	}

	public void setTitle(String title) {
		mTitleText = title;
		setMidViewAlign();
		txtMiddle.setText(title);
		txtMiddle.setVisibility(View.VISIBLE);

		customView_mid.removeAllViews();
		customView_mid.setVisibility(View.GONE);
	}

	public void setTitle(int titleResId) {
		if (titleResId < 0) {
			txtMiddle.setVisibility(View.GONE);
			return;
		}
		setTitle(getResources().getString(titleResId));
	}

	/**
	 * 左边按钮的动作，是回退还是侧滑栏开关
	 * 
	 * @param action
	 *            enum LeftAction
	 */
	public void setLeftAction(LeftAction action) {
		if (LeftAction.BACK == action) {
			btn_back.setVisibility(View.VISIBLE);
		} else if (LeftAction.ICON == action) {
			btn_back.setVisibility(View.VISIBLE);
		} else {
			btn_back.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置右边按钮图片(无背景色)
	 * 
	 * @param rightAction
	 *            右边样式 显示文本或者图片
	 * @param resId
	 *            图片或者文本资源
	 */
	public void setRightAction(RightAction rightAction, int resId) {
		setRightAction(rightAction, -1, resId);
	}

	/**
	 * 设置右边按钮图片
	 * 
	 * @param rightAction右边样式(显示文本或者图片)
	 * @param bgResId背景图片
	 * @param resId图片或者文本资源
	 */
	public void setRightAction(RightAction rightAction, int bgResId, int resId) {
		if (resId <= 0) {
			txtRight.setVisibility(View.GONE);
			imageRight.setVisibility(View.GONE);
			return;
		}

		if (RightAction.ICON == rightAction) {
			txtRight.setVisibility(View.GONE);
			imageRight.setVisibility(View.VISIBLE);
			imageRight.setImageResource(resId);
			if (bgResId > 0) {
				imageRight.setBackgroundResource(bgResId);
			}
		} else if (RightAction.TEXT == rightAction) {
			txtRight.setVisibility(View.VISIBLE);
			imageRight.setVisibility(View.GONE);
			txtRight.setText(resId);
			if (bgResId > 0) {
				txtRight.setBackgroundResource(bgResId);
			}
		} else {
			txtRight.setVisibility(View.GONE);
			imageRight.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题栏整体行为(无背景色)
	 * 
	 * @param title
	 *            中间标题文本
	 * @param action
	 *            左边按钮动作
	 * @param rightAction
	 *            右边样式 显示文本或者图片
	 * @param resId
	 *            图片或者文本资源
	 */
	public void setTitleBarAction(int title, LeftAction leftAction,
			RightAction rightAction, int resId) {
		setTitleBarAction(title, leftAction, rightAction, -1, resId);
	}

	/**
	 * 设置标题栏整体行为
	 * 
	 * @param title
	 *            中间标题文本
	 * @param action
	 *            左边按钮动作
	 * @param rightAction
	 *            右边样式 显示文本或者图片
	 * @param bgResId
	 *            背景图片
	 * @param resId
	 *            图片或者文本资源
	 */
	public void setTitleBarAction(int title, LeftAction leftAction,
			RightAction rightAction, int bgResId, int resId) {
		setTitle(title);
		setLeftAction(leftAction);
		setRightAction(rightAction, bgResId, resId);
	}

	/**
	 * 设置左边自定义view,取代原始title
	 * 
	 * @param view
	 */
	public void setLeftCustomView(View view) {
		if (view == null) {
			return;
		}

		if (customView_left.getChildCount() > 0 && customView_left.getChildAt(0) == view) {
			invalidate();
			return;
		}

		customView_left.removeAllViews();

		btn_back.setVisibility(View.GONE);
		customView_left.setVisibility(View.VISIBLE);
		customView_left.addView(view);

		setMidViewAlign();
		invalidate();
	}

	/**
	 * 设置中间自定义view,取代原始title
	 * 
	 * @param view
	 */
	public void setMidCustomView(View view) {
		if (view == null) {
			return;
		}

		if (customView_mid.getChildCount() > 0 && customView_mid.getChildAt(0) == view) {
			invalidate();
			return;
		}

		customView_mid.removeAllViews();

		mTitleText = "";
		txtMiddle.setVisibility(View.GONE);
		customView_mid.setVisibility(View.VISIBLE);
		customView_mid.addView(view);

		setMidViewAlign();
		invalidate();
	}

	public void setRightTextEnabled(boolean enable) {
		txtRight.setEnabled(enable);
	}

	public void setMidViewAlign() {
		if (mWidth > 0) {
			final FrameLayout.LayoutParams leftParams = (FrameLayout.LayoutParams) btn_back.getLayoutParams();
			final int leftWidth = btn_back.getWidth() + leftParams.leftMargin + leftParams.rightMargin;
			int rightWidth = 0;
			if (txtRight.getVisibility() == View.VISIBLE) {
				final FrameLayout.LayoutParams rightParams = (FrameLayout.LayoutParams) txtRight.getLayoutParams();
				rightWidth = txtRight.getWidth() + rightParams.rightMargin;
			} else if (imageRight.getVisibility() == View.VISIBLE) {
				final FrameLayout.LayoutParams rightParams = (FrameLayout.LayoutParams) imageRight.getLayoutParams();
				rightWidth = imageRight.getWidth() + rightParams.rightMargin;
			}

			final int width = Math.max(leftWidth, rightWidth);

			final int midWidth = mWidth - getPaddingLeft() - getPaddingRight() - width * 2;

			if (mWidth - midWidth < 0) {
				return;
			}

			if (txtMiddle.getVisibility() == View.VISIBLE) {
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) txtMiddle.getLayoutParams();
				params.width = midWidth;
				txtMiddle.setLayoutParams(params);
			} else if (customView_mid.getVisibility() == View.VISIBLE && customView_mid.getChildCount() > 0) {
				FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) customView_mid.getLayoutParams();
				params.width = midWidth;
				customView_mid.setLayoutParams(params);
			}
		}
	}

	/** 获取试图的真实宽高(每次view改变时都会调用) */
	@Override
	public synchronized void onGlobalLayout() {
		int tmpW = getWidth();

		if (tmpW != 0 && tmpW != mWidth) {
			mWidth = tmpW;
			setMidViewAlign();
		}
	}
}