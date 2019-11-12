package catc.tiandao.com.matchlibrary;

import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

public class CheckNet {

	/**
	 * 1）判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 2）判断是否有网络连接,如果没有开启网络的话，就进入到网络开启那个界面
	 * 
	 * @param context
	 * @return
	 */
	protected static boolean checkNetwork(final Context context) {
		// TODO Auto-generated method stub
		boolean flag = false;
		ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null)
			flag = cwjManager.getActiveNetworkInfo().isAvailable();
		if (!flag) {
			Builder b = new Builder(context).setTitle("网络不可用").setMessage("请开启GPRS或WIFI网路连接");
			b.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent("/");
					ComponentName comp = new ComponentName("com.android.settings",
							"com.android.settings.WirelessSettings");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					context.startActivity(mIntent);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub dialog.cancel();
				}
			}).create();
			b.show();
		}
		return flag;
	}

	/**
	 * 3）判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 4）判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 5）获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 6）判断是否是3G网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is3rd(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkINfo = cm.getActiveNetworkInfo();
		if (networkINfo != null && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
			return true;
		}
		return false;
	}

	/**
	 * 打开网络设置
	 **/
	public static void showOpenNetwork(final Context contxt) {
		Builder builder = new Builder(contxt);
		builder.setMessage("没有可用的网络，请检查网络连接！").setTitle("提示")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 跳到系统设置
						Intent wifiSettingsIntent = new Intent("android.settings.SETTINGS");
						contxt.startActivity(wifiSettingsIntent);

					}
				}).setNegativeButton("取消", null).show();
	}

	/**
     * 用来判断服务是否运行.
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
	public static boolean isServiceRunning(Context mConent, String className){
		
		
		
		boolean isRunning = false;
		
		ActivityManager activityManager = (ActivityManager)mConent.getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size()>0)) {
			return false;
		}
		
		for(int i = 0;i<serviceList.size(); i++){
			if(serviceList.get(i).service.getClassName().equals(className)){
				
				System.out.println("现有的service: " + serviceList.get(i).service.getClassName());
				isRunning = true;
			}
		}
		
		
		return isRunning;
		
	}

	/**
	 * 打开网络设置
	 **/
	public static void showOpenNetwork(final Context contxt, DialogInterface.OnClickListener listener) {
		Builder builder = new Builder(contxt);
		builder.setCancelable(false);
		builder.setMessage("没有可用的网络，请检查网络连接！").setTitle("提示")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 跳到系统设置
						Intent wifiSettingsIntent = new Intent("android.settings.SETTINGS");
						contxt.startActivity(wifiSettingsIntent);

					}
				}).setNegativeButton("取消", listener).show();
	}
}