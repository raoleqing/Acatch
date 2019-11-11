package catc.tiandao.com.match.ui.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.my.CollectionActivity;
import catc.tiandao.com.match.ui.event.IntelligenceFragment;
import catc.tiandao.com.match.ui.event.MatchDetailsActivity;
import catc.tiandao.com.match.utils.UmengUtil;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import cn.jzvd.Jzvd;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/***
 * */
public class NewsDetailsActivity extends BaseActivity implements View.OnClickListener{

    public static final String NEW_ID = "newId";
    private int newId;

    private WebView webView;
    private String urlStr;

    private String startUrl;
    private ImageView tv_collection;
    private int iCollection;

    private PopupWindow popupWindow;
    private LinearLayout rl_contianer;
    private String shareTitle = "";
    private NewOperationRun setRun;
    private GetNewRun run;

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {

                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    parseData(result1);
                    break;

                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    setParseData(result3);
                    break;

                default:
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_news_details );
        setTitleText( "" );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        //获取新闻详情——你们根据新闻id跳转到http://www.leisuvip1.com/New/Index/token=**&newId=新闻id
        newId = this.getIntent().getIntExtra( NEW_ID,0 );
        //http://www.leisuvip1.com/New/Index? token=**&newId=新闻id
        urlStr = "http://www.leisuvip1.com/New/Index?token="+ UserUtils.getToken( this )+"&newId=" + newId;
        viewInfo();
        GetNew();
        setProgressVisibility( View.GONE );
    }



    private void viewInfo() {

        ImageView image = ViewUtls.find( this,R.id.activity_return );
        tv_collection = ViewUtls.find( this,R.id.tv_collection );
        tv_collection.setVisibility( View.VISIBLE );
        ImageView tv_share = ViewUtls.find( this,R.id.tv_share );
        rl_contianer = ViewUtls.find(this, R.id.rl_container);
        tv_share.setVisibility( View.VISIBLE );

        if(iCollection == 0){
            tv_collection.setImageResource( R.mipmap.navbar_icon_collect_default );
        }else {
            tv_collection.setImageResource( R.mipmap.icon_collect );
        }


        tv_collection.setOnClickListener( this );
        tv_share.setOnClickListener( this );
        image.setOnClickListener( this);

        webView = ViewUtls.find( this,R.id.webView );

        // 设置setWebChromeClient对象
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle( view, title );

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                setProgressVisibility( View.GONE );
                super.onProgressChanged(view, newProgress);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
               // pickFile();
            }

        });


            // 设置标题栏
            WebSettings webSettings = webView.getSettings();
            // 支持获取手势焦点，输入用户名、密码或其他
        webView.requestFocusFromTouch();
        webView.requestFocus();

            //https 与 http 混合开发不显示图片：（android 5.0后出现的）
            if (Build.VERSION.SDK_INT >= 21) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webSettings.setJavaScriptEnabled(true); // 支持js

            //用于视 频 播放
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染的优先级
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

        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_return:

                NewsDetailsActivity.this.onBackPressed();
                break;
            case R.id.tv_share:

                if(UserUtils.isLanded( NewsDetailsActivity.this )){
                    showShare();
                }else {
                    UserUtils.startLongin( NewsDetailsActivity.this );
                }


                break;
            case R.id.tv_collection:

                if(UserUtils.isLanded( NewsDetailsActivity.this )){
                    NewOperation("shouCang");
                }else {
                    UserUtils.startLongin( NewsDetailsActivity.this );
                }


                break;
        }

    }

    private void GetNew() {


        try{
            if (CheckNet.isNetworkConnected( NewsDetailsActivity.this)) {
                setProgressVisibility( View.VISIBLE );
                // type=类型& newId=新闻id& pingLun=评论内容
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( NewsDetailsActivity.this ) );
                param.put("newId",newId + "");


                run = new GetNewRun(param);
                ThreadPoolManager.getsInstance().execute(run);
            } else {

                Toast.makeText(NewsDetailsActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     *
     * */
    class GetNewRun implements Runnable{
        private HashMap<String, String> param;

        GetNewRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( NewsDetailsActivity.this,HttpUtil.GETNEW ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x001;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void parseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            if(code == 0){
                JSONObject data  = obj.optJSONObject( "data" );
                shareTitle = data.optString( "title" );
                String dt = data.optString( "dt" );
                String html = data.optString( "html" );
                iCollection = data.optInt( "iCollect" );
                String newUrl = data.optString( "newUrl" );
                urlStr = newUrl.replace( "###",UserUtils.getToken( NewsDetailsActivity.this )  );
                webView.loadUrl(urlStr);

                if(iCollection == 0){
                    tv_collection.setImageResource( R.mipmap.navbar_icon_collect_default );
                }else {
                    tv_collection.setImageResource( R.mipmap.icon_collect );
                }


            }

            Toast.makeText(NewsDetailsActivity.this,message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }









    //dianZan，zhuanFa，pingLun，shouCang
    private void NewOperation(String type) {

        try{
            if (CheckNet.isNetworkConnected( NewsDetailsActivity.this)) {
                setProgressVisibility( View.VISIBLE );
                // type=类型& newId=新闻id& pingLun=评论内容
                HashMap<String, String> param = new HashMap<>(  );
                param.put("token", UserUtils.getToken( NewsDetailsActivity.this ) );
                param.put("type",type);
                param.put("newId", newId + "");
                param.put("pingLun", "");

                setRun = new NewOperationRun(param);
                ThreadPoolManager.getsInstance().execute(setRun);
            } else {

                Toast.makeText(NewsDetailsActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
                setProgressVisibility( View.GONE );
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *
     * */
    class NewOperationRun implements Runnable{
        private HashMap<String, String> param;

        NewOperationRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( NewsDetailsActivity.this,HttpUtil.NEW_OPERATION ,param,new HttpUtil.HttpUtilInterface(){
                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x003;
                    myHandler.sendMessage( message );
                }
            });



        }
    }



    private void setParseData(String result) {

        if(result == null){
           setProgressVisibility( View.GONE );
            return;
        }

        try{
            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );

            if(code == 0) {

                if(iCollection == 0){
                    iCollection = 1;
                    tv_collection.setImageResource( R.mipmap.icon_collect );
                }else {
                    iCollection = 0;
                    tv_collection.setImageResource( R.mipmap.navbar_icon_collect_default );
                }


            }

            Toast.makeText(NewsDetailsActivity.this,message,Toast.LENGTH_SHORT ).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }




    private void showShare() {
        //分享
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pop_share, null);
            popupWindow = new PopupWindow(this);
            popupWindow.setContentView(contentView);
            popupWindow.setAnimationStyle(R.style.bottomShowAnimStyle);
            popupWindow.setWidth( LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    setBackgroundAlpha(1f);
                }
            });

            contentView.findViewById(R.id.iv_zone).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare( SHARE_MEDIA.QZONE);
                    popupWindow.dismiss();
                }
            });
            contentView.findViewById(R.id.iv_moment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    popupWindow.dismiss();
                }
            });

            contentView.findViewById(R.id.iv_wiexin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleShare(SHARE_MEDIA.WEIXIN);
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rl_contianer, Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.5f);
    }

    /***设置背景透明度*/
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    /***分享*/
    private void singleShare(SHARE_MEDIA shareMedia) {


        //新闻网址：http://www.leisuvip1.com/New/Index? token=**&newId=新闻id

        int logoResId = R.mipmap.app_icon;

        UmengUtil.shareSinglePlatform(this, shareMedia, urlStr,getString( R.string.app_name ) , logoResId, shareTitle);
    }




    /**
     * 下载
     * **/
    public class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            try {
                System.out.println(url);
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    // 监听
    private class MyWebViewClient extends WebViewClient {



        // 当有新连接时使用当前的webview进行显示
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 当有新连接时使用当前的webview进行显示
            System.out.println("连接：" + url);
           if (!url.startsWith("http")  && !url.startsWith( "yy://" ) && !url.startsWith( "file:" )) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }catch (ActivityNotFoundException a){
                    a.getMessage();
                }
                return true;
            } else {
                if(startUrl!=null && startUrl.equals(url)) {
                    Map<String,String> extraHeaders = new HashMap<String, String>();
                    if(view.getUrl() != null){
                        extraHeaders.put("Referer", view.getUrl());
                    }

                    view.loadUrl(url,extraHeaders);
                } else {
                    //交给系统处理
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }

        }




        // 加载完成时要做的工作
        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            setProgressVisibility( View.GONE );

        }

        // 开始加载网页时要做的工作
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startUrl = url;
            setProgressVisibility(View.VISIBLE);

        }

        //// 加载错误时要做的工作
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }

    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        NewsDetailsActivity.this.finish();
    }



}
