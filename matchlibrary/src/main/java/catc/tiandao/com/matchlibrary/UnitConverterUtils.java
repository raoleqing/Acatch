package catc.tiandao.com.matchlibrary;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

//cjl  单位之间的转换工具类
public class UnitConverterUtils {

	/** 
	* 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	*/  
	public static int dip2px(Context context, float dpValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (int) (dpValue * scale + 0.5f);  
	}  
	  
	/** 
	* 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
	*/  
	public static int px2dip(Context context, float pxValue) {  
	  final float scale = context.getResources().getDisplayMetrics().density;  
	  return (int) (pxValue / scale + 0.5f);  
	}

	/**
	 * sp转px
	 *
	 * @param context context
	 * @param spVal   sp
	 * @return
	 */
	public static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}
	
	
	/**
	 * 设置view的高度 为比1
	 * **/
	public static void setViewHeignt5ratio3(View view,int weight){
		
		float viewHeight = (float) weight * 0.6f;
		
		LayoutParams params = view.getLayoutParams();
		params.height = (int) viewHeight;
		view.setLayoutParams(params);
		
		
	}
	
	
	/**
	 * 
	 * 屏幕宽度
	 **/
	public static int getDeviceWidth(Activity context) {

		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);

		return metric.widthPixels; // 屏幕宽度
	}

	// 屏幕高度
	public static int getDeviceHeight(Activity context) {

		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels; // 屏幕高度（像素）

	}


	/**
	 * 设置view的高度 为比1
	 **/
	public static void setViewHeignt2ratio1(View view, int weight) {

		float viewHeight = (float) weight * 0.5f;

		LayoutParams params = view.getLayoutParams();
		params.height = (int) viewHeight;
		view.setLayoutParams(params);

	}

	/**
	 * 设置view的高度 为比1
	 **/
	public static void setViewHeignt4ratio1(View view, int weight) {

		float viewHeight = (float) weight * 0.4f;

		LayoutParams params = view.getLayoutParams();
		params.height = (int) viewHeight;
		view.setLayoutParams(params);

	}




}
