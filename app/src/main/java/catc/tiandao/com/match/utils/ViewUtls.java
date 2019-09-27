package catc.tiandao.com.match.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;


/**
 * 控件工具类
 * **/
public class ViewUtls {
	
	
	/** 
	 * 获取状态栏高度——方法1 
	 * */ 
	public static int getStatusBarHeight(Context context){
		
		int statusBarHeight1 = 0;  
		//获取status_bar_height资源的ID  
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");  
		if (resourceId > 0) {  
		    //根据资源ID获取响应的尺寸值  
		    statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);  
		} 
		return statusBarHeight1;
	}
	  
	
	
	
	  /* 
     * 获取控件宽 
     */  
    public static int getWidth(View view)  
    {  
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        view.measure(w, h);  
        return (view.getMeasuredWidth());         
    }  
    /* 
     * 获取控件高 
     */  
    public static int getHeight(View view)  
    {  
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
        view.measure(w, h);  
        return (view.getMeasuredHeight());         
    }

    /**
     * 替代findviewById方法
     */
    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

    /**
     * 替代findviewById方法
     */
    public static <T extends View> T find(Activity context, int id) {
        return (T) context.findViewById(id);
    }



    public static void useSpan(TextView textView,String color) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(textView.getText());
          //设置文字颜色。
        ssb.setSpan(new ForegroundColorSpan( Color.parseColor(color)), 0, 5, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(ssb);
    }



}
