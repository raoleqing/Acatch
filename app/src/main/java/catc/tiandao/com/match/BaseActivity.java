package catc.tiandao.com.match;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.message.PushAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import catc.tiandao.com.matchlibrary.ViewUtls;
import pl.droidsonroids.gif.GifImageView;


/**
 * 所有窗口的父级activity 主要放 标题， 没有网络提示
 **/
public class BaseActivity extends FragmentActivity {

	private String TAG = "Match";

	private LinearLayout parentLinearLayout;// 把父类activity和子类activity的view都add到这里
	private RelativeLayout base_title_layout;// 标题布局
	private TextView activity_title;// 标题布局
	private ImageView activity_return;// 返回

	private View progress;
	private GifImageView iv_image;
	private int progressIndex = 1;
	private int backgroundColor;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE);
		initContentView(R.layout.base_activity);

		PushAgent.getInstance(this).onAppStart();
		backgroundColor = ContextCompat.getColor( this,R.color.colorPrimary );
		infoView();

		int mAppStatus  = AppStatusManager.getInstance().getAppStatus();
		switch (mAppStatus) {
			case AppStatusConstant.STATUS_FORCE_KILLED:
				Log.e(TAG, "STATUS_FORCE_KILLED");
				//*处理APP被强杀*//*
				protectApp();
				break;
			case AppStatusConstant.STATUS_KICK_OUT:
				//*处理APP被退出登录*//*
				Log.e(TAG, "STATUS_KICK_OUT");
				break;
			case AppStatusConstant.STATUS_NORMAL:
				//*APP正常状态*//*
				Log.e(TAG, "STATUS_NORMAL");

				break;
		}



	}



	/**
     * 要改变状态栏颜色复写该方法
     * @return
     */
	public int getStatusBarColor() {
		return ContextCompat.getColor(this, R.color.colorPrimary);
	}

	/**
	 * 初始化contentview
	 */
	private void initContentView(int layoutResID) {
		// TODO Auto-generated method stub
		ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
		viewGroup.removeAllViews();
		parentLinearLayout = new LinearLayout(this);
		parentLinearLayout.setOrientation( LinearLayout.VERTICAL);
		viewGroup.addView(parentLinearLayout);
		LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

		progress = LayoutInflater.from(this).inflate(R.layout.progress, null);
		//progress.setPadding(0, getStatusBarHeight(this), 0, 0);
		iv_image = ViewUtls.find( progress,R.id.iv_image );

		viewGroup.addView(progress);

		// 不作任何处理
		progress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setProgressVisibility( View.GONE);
			}
		});

	}

	@Override
	public void setContentView(int layoutResID) {

		LayoutInflater.from(this).inflate(layoutResID, parentLinearLayout, true);

	}

	@Override
	public void setContentView(View view) {

		parentLinearLayout.addView(view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {

		parentLinearLayout.addView(view, params);

	}


	/**
	 * 控件初始化
	 **/
	private void infoView() {
		// TODO Auto-generated method stub
		base_title_layout = (RelativeLayout) findViewById(R.id.base_title_layout);
		base_title_layout.setBackgroundColor(backgroundColor);
		activity_title = (TextView) findViewById(R.id.activity_title);
		activity_return = (ImageView) findViewById(R.id.activity_return);

	}


	private void setTitlebgColor(int color) {
		if(base_title_layout != null){
			base_title_layout.setBackgroundColor( color );
			setStatusBarColor( color);
		}

	}




	/**
	 * 设置标题的可视化
	 **/
	public void setTitleVisibility(int visibility) {

		if (base_title_layout != null)
			base_title_layout.setVisibility(visibility);

	}


	/**
	 * 加载条的显示与隐藏
	 **/
	public void setProgressVisibility(int visibility) {

		if(progress != null){
			progress.setVisibility(visibility);
		}


	}

	/**
	 * 加载条的显示与隐藏
	 **/
	public void showProgress() {

		if (progress != null) {
			progressIndex++;
			progress.setVisibility( View.VISIBLE);
		}

	}

	public void hideProgress() {

		if (progress != null) {
			progressIndex--;
			if (progressIndex <= 0) {
				progressIndex = 0;
				progress.setVisibility( View.GONE);
			}

		}

	}

	/**
	 * 设置标题
	 **/
	public void setTitleText(String text) {


		if (activity_title != null)
			activity_title.setText(text);
	}






	// 返回
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		System.gc();
	}



	/**
	 * 设置状态栏为透明
	 *
	 */
	public  void setTranslucentStatus() {
		//Android6.0（API 23）以上，系统方法
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor( Color.TRANSPARENT);

		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.setFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}


	/**
	 * 修改状态栏颜色，支持4.4以上版本
	 * @param colorId
	 * (用到的类要在清单文件的activity声明中添加android:alwaysRetainTaskState="true")
	 */
	public void setStatusBarColor(int colorId) {

		//Android6.0（API 23）以上，系统方法
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.setStatusBarColor(colorId);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
			setTranslucentStatus();
			//设置状态栏颜色
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(colorId);
		}
	}



	/**
	 * 设置状态栏模式
	 * @param isTextDark 文字、图标是否为黑色 （false为默认的白色）
	 * @return
	 */
	public  void setStatusBarMode( boolean isTextDark) {

		//4.4以上才可以改文字图标颜色
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


			//6.0以上，调用系统方法
			Window window = getWindow();
			window.addFlags( WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			if(isTextDark){
				window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
			}else {
				getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_VISIBLE);
			}


		}
	}






	/**
	 * 设置Flyme系统状态栏的文字图标颜色
	 * @param isDark 状态栏文字及图标是否为深色
	 * @return
	 */
	public boolean setFlymeStatusBarTextMode(boolean isDark) {
		Window window = getWindow();
		boolean result = false;
		if (window != null) {
			try {
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
						.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
						.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (isDark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 设置MIUI系统状态栏的文字图标颜色（MIUIV6以上）
	 * @param isDark 状态栏文字及图标是否为深色
	 * @return
	 */
	public boolean setMIUIStatusBarTextMode(boolean isDark) {
		boolean result = false;
		Window window = getWindow();
		if (window != null) {
			Class clazz = window.getClass();
			try {
				int darkModeFlag = 0;
				Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
				if (isDark) {
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
				} else {
					extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
				}
				result = true;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					//开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
					if (isDark) {
						getWindow().getDecorView().setSystemUiVisibility( View
								.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
								.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
					} else {
						getWindow().getDecorView().setSystemUiVisibility( View
								.SYSTEM_UI_FLAG_VISIBLE);
					}
				}
			} catch (Exception e) {

			}
		}
		return result;
	}



	private static int getStatusBarHeight(Context context) {
		int statusBarHeight = 0;
		Resources res = context.getResources();
		int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = res.getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}

	//APP字体大小，不随系统的字体大小变化而变化的方法
	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		Configuration config=new Configuration();
		config.setToDefaults();
		res.updateConfiguration(config,res.getDisplayMetrics() );
		return res;
	}


	/**
	 * 处理APP被强杀
	 */
	protected void protectApp() {
		/*跳转主界面处理*/
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(AppStatusConstant.KEY_HOME_ACTION, AppStatusConstant.ACTION_RESTART_APP);
		startActivity(intent);
	}





}
