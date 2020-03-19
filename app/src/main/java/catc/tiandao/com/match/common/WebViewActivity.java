package catc.tiandao.com.match.common;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;


import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;


/**
 * 内部web页面
 **/
public class WebViewActivity extends BaseActivity {

	public static final String URL_STR = "url_str";
	public static final String OPEN_TYPE = "open_type";
	public static final String IS_COME_FROM_LAUNCH = "isComeFromLaunch";
	public static final String TITLE_RETURN_ANWAY = "titleReturnAnway";//标题左侧的返回键是不是点击后直接退出界面

	private ImageView web_return;
	private WebView webView1;
	private int type;
	private int open_type;
	private boolean isComeFromLaunch = false;

	final static int sleepTime = 2000;
	private String urlStr;//首页地址

	private boolean isExit;
	private boolean titleReturnAnway = false;
	private boolean isLogin = false;
	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				setProgressVisibility(View.GONE);
				break;
				case 2:
					isExit = false;
					break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		PushAgent.getInstance(this).onAppStart();
		MobclickAgent.onEvent(this, "OpenWebView");


		setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
		setStatusBarMode(true);

		Intent inetn = this.getIntent();
		urlStr = inetn.getStringExtra(URL_STR);
		System.out.print("open_url: " + urlStr);
		open_type = inetn.getIntExtra(OPEN_TYPE, 0);
		type = inetn.getIntExtra("type", 0);
		titleReturnAnway = inetn.getBooleanExtra(TITLE_RETURN_ANWAY, false);
		isComeFromLaunch = inetn.getBooleanExtra(IS_COME_FROM_LAUNCH, false);

		web_return = (ImageView) this.findViewById( R.id.activity_return);
		webView1 = (WebView) findViewById(R.id.webView1);
		// 设置标题栏
		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				if(title != null && !title.startsWith("wpa")){
					if(title.length() > 10){
						title = title.substring(0,10) + "...";
					}
					setTitleText(title);
				}
			}

		};
		// 设置setWebChromeClient对象
		webView1.setWebChromeClient(wvcc);

		if (type == 0) {
			WebSettings webSettings = webView1.getSettings();
			// 支持获取手势焦点，输入用户名、密码或其他
			if (Build.VERSION.SDK_INT >= 21) {
				webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
			}
			webView1.requestFocusFromTouch();
			webSettings.setJavaScriptEnabled(true); // 支持js
			webSettings.setPluginState(PluginState.ON); // 支持插件
			webSettings.setRenderPriority(RenderPriority.HIGH); // 提高渲染的优先级
			// 设置自适应屏幕，两者合用
			webSettings.setUseWideViewPort(true); // 将图片调整到适合webview的大小
			webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
			webSettings.setSupportZoom(true); // 支持缩放，默认为true。是下面那个的前提。
			webSettings.setBuiltInZoomControls(true); // 设置内置的缩放控件。
			// 若上面是false，则该WebView不可缩放，这个不管设置什么都不能缩放。
			webSettings.setDisplayZoomControls(false); // 隐藏原生的缩放控件
			//webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局
			webSettings.setAllowFileAccess(true); // 设置可以访问文件
			webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
			webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
			webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
			webSettings.setDefaultTextEncodingName("utf-8");// 设置编码格式
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存，只从网络获取数据.
			webSettings.setAppCacheEnabled(true);// 应用可以有缓存
			webSettings.setDomStorageEnabled(true);// 设置可以使用localStorage
			webSettings.setAppCacheMaxSize(10 * 1024 * 1024);// 缓存最多可以有10M
			webSettings.setPluginState(PluginState.ON);
			webSettings.setTextZoom(100);//设置webview内部字体的缩放比例
			webSettings.setTextSize(WebSettings.TextSize.NORMAL);
			// 触摸焦点起作用
			webView1.requestFocusFromTouch();
			webView1.requestFocus();
			//webView1.setScrollBarStyle();// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
			webView1.setWebViewClient(new MyWebViewClient());
			// 用于视 频 播放
			webView1.getSettings().setPluginState(PluginState.ON);
			webView1.setWebViewClient(new MyWebViewClient());
			webView1.setDownloadListener(new MyWebViewDownLoadListener());
			webView1.loadUrl(urlStr);
		} else {
			// 能够的调用JavaScript代码
			// product_picture_html = new WebView(ProductDetails.this);
			webView1.getSettings().setDefaultTextEncodingName("utf-8");
			// 加载HTML字符串进行显示
			webView1.getSettings().setJavaScriptEnabled(true);
			webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
			webView1.getSettings().setUseWideViewPort(true);
			webView1.getSettings().setLoadWithOverviewMode(true);
			webView1.setSaveEnabled(true);
			webView1.getSettings().setRenderPriority(RenderPriority.HIGH);
			webView1.getSettings().setSupportZoom(false);// 支持缩放
			webView1.getSettings().setTextZoom(100);
			webView1.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
			webView1.loadData(urlStr, "text/html; charset=UTF-8", null);// 这种写法可以正确解码
			setProgressVisibility(View.GONE);
		}

		web_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(titleReturnAnway){
					webView1.loadData("", "text/html; charset=UTF-8", null);
					WebViewActivity.this.finish();
				}else {
//					String onUrl = webView1.getUrl();
//					System.out.print("onUrl: " + onUrl);
//					if (onUrl != null && onUrl.equals(urlStr)) {
//						if(isComeFromLaunch){
//							Intent intent = new Intent(WebViewActivity.this,MainActivity.class);
//							startActivity(intent);
//							overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//						}
//						webView1.loadData("", "text/html; charset=UTF-8", null);
//						WebViewActivity.this.finish();
//					} else {
//						webView1.goBack();// 返回前一个页面
//					}
					onBackPressed();
				}
			}
		});


	}




	// 监听
	private class MyWebViewClient extends WebViewClient {

		// 当有新连接时使用当前的webview进行显示
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// 当有新连接时使用当前的webview进行显示
			System.out.println(url);
			if(url.startsWith("yiqibaziapp")){
				Uri uri = Uri.parse(url);
				if(uri != null){
					String type = uri.getQueryParameter("type");
					String target = uri.getQueryParameter("target");
					String target_id = uri.getQueryParameter("target_id");
					if(type != null && type.equals("app") && target != null){

					}
				}
				return true;
			}else if (!url.startsWith("http")) {
				try{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
				}catch (ActivityNotFoundException a){
					a.getMessage();
				}
				return true;
			} else {
				Map<String,String> extraHeaders = new HashMap<String, String>();
				if(view.getUrl() != null){
					extraHeaders.put("Referer", view.getUrl());
				}
				view.loadUrl(url,extraHeaders);
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

		// 加载完成时要做的工作
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Message msg = myHandler.obtainMessage();
			msg.what = 1;
			myHandler.sendMessageDelayed(msg, sleepTime);
		}

		// 开始加载网页时要做的工作
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			setProgressVisibility(View.GONE);
		}

		//// 加载错误时要做的工作
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("OpenWebView"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长

		try {
			webView1.getClass().getMethod("onResume").invoke(webView1, (Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("OpenWebView");
		// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
//		webView1.onPause();
		try {
			webView1.getClass().getMethod("onPause").invoke(webView1, (Object[]) null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 下载
	 * **/
	public class MyWebViewDownLoadListener implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			try {
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	// 返回
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		String onUrl = webView1.getUrl();
		if(onUrl != null && onUrl.equals(urlStr)){
				WebViewActivity.this.finish();
		}else {
				if(webView1.canGoBack()){
					webView1.goBack();;
				}else {
					WebViewActivity.this.finish();
				}
		}
	}
}