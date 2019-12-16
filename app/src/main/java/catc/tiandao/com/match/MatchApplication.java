package catc.tiandao.com.match;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.score.ScoreDetailsActivity;
import catc.tiandao.com.match.utils.DeviceUtils;
import catc.tiandao.com.match.utils.StringEscapeUtils;

public class MatchApplication extends Application {

    private static MatchApplication instance;
    private String TAG = "Match";

    /**
     * Called when the application is starting, before any activity, service, or
     * receiver objects (excluding content providers) have been created.
     * （当应用启动的时候，会在任何activity、Service或者接收器被创建之前调用，所以在这里进行ImageLoader 的配置）
     * 当前类需要在清单配置文件里面的application下进行name属性的设置。
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        AppStatusManager.init(this);


        // 缓存图片的配置，一般通用的配置
        initImageLoader(getApplicationContext());

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        //友盟统计初始化
        String channel = DeviceUtils.getAppMetaData(this,"UMENG_CHANNEL");
        UMConfigure.init(this, Constant.UMENG_APP_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE,Constant.UMENG_MESSAGE_SECRET);
        // 开启友盟推送
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d(TAG, "device_token：" + deviceToken);
                if(deviceToken != null && !deviceToken.equals("")){
                    SharedPreferencesUtil.putString( MatchApplication.this,SharedPreferencesUtil.DEVICE_TOKEN,deviceToken );
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d(TAG, "onFailure：" + s + s1);
            }
        });

        mPushAgent.setNotificationPlaySound( MsgConstant.NOTIFICATION_PLAY_SERVER); //服务端控制声音
        setMessageHandler(mPushAgent);



        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
           // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //U盟SDK初始化
        UMShareAPI.get(this);
        //    appid: wxccbfabb317ba4195
        //appsecret:838c2e270cccb3c6c9df93c16e94be3c
        PlatformConfig.setWeixin("wxccbfabb317ba4195", "838c2e270cccb3c6c9df93c16e94be3c");
        //APP ID.  1109853841
        //APP KEY.  koinzLw4tebC6UWz
        PlatformConfig.setQQZone("1109853841", "koinzLw4tebC6UWz");


    }


    public void setMessageHandler(PushAgent mPushAgent) {

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {

                Log.e(TAG,"dealWithCustomMessage: " + msg);

                new Handler(getMainLooper()).post( new Runnable() {
                    @Override
                    public void run() {
                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }

                        //Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /*
             * 自定义通知栏样式的回调方法
             */
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                try {

                    JSONObject obj = new JSONObject(msg.custom);

                    // 通知栏提示文字
                    String ticker = StringEscapeUtils.HTMLDecode(obj.optString("ticker"));
                    // 通知标题
                    String title = StringEscapeUtils.HTMLDecode(obj.optString("title"));
                    // 通知文字描述
                    String text = StringEscapeUtils.HTMLDecode(obj.optString("text"));
                    // 网络图片，加载优先级最高
                    String img = StringEscapeUtils.HTMLDecode(obj.optString("img"));

                    msg.ticker = ticker;
                    msg.title = title;
                    msg.text = text;
                    if (img != null && !img.equals("")) {
                        msg.img = img;
                    }


                    int shake =  SharedPreferencesUtil.getInt( getApplicationContext(), SharedPreferencesUtil.SHAKE );
                    int sound  =  SharedPreferencesUtil.getInt( getApplicationContext(),SharedPreferencesUtil.SOUND );

                    if (Build.VERSION.SDK_INT >= 26) {
                        NotificationManager manager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE );
                        NotificationChannel channel = new NotificationChannel( "channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH );


                        channel.enableLights( true ); //设置开启指示灯，如果设备有的话
                        channel.setLightColor( Color.RED ); //设置指示灯颜色
                        channel.setShowBadge( true ); //设置是否显示角标
                        channel.setLockscreenVisibility( Notification.VISIBILITY_PRIVATE );//设置是否应在锁定屏幕上显示此频道的通知
                        channel.setDescription( "" );//设置渠道描述


                        if (sound == 1) {
                            // 自定义声音
                            Uri uri = Uri.parse( "android.resource://" + getPackageName() + "/raw/qq.wav" );
                            channel.setSound( null, Notification.AUDIO_ATTRIBUTES_DEFAULT );
                        }
                        if (shake == 1) {
                            // 设置通知出现时的震动（如果 android 设备支持的话）
                            channel.enableVibration( true );
                            channel.setVibrationPattern( new long[]{100, 200, 300, 400, 500, 600} );//设置震动频率
                        }
                        channel.setBypassDnd( true );//设置是否绕过免打扰模式

                        if (manager != null) {
                            manager.createNotificationChannel( channel );
                        }


                        Notification.Builder builder = new Notification.Builder( context, "channel_id" );
                        //Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE

                        if (sound == 1) {
                            ///使用默认的声音
                            builder.setDefaults( Notification.DEFAULT_SOUND );
                        }
                        if (shake == 1) {
                            ////使用默认的震动
                            builder.setDefaults( Notification.DEFAULT_VIBRATE );
                        }

                        RemoteViews myNotificationView = new RemoteViews( context.getPackageName(), R.layout.notification_view );
                        myNotificationView.setTextViewText( R.id.notification_title, msg.title );
                        myNotificationView.setTextViewText( R.id.notification_text, msg.text );
                        if (img != null && !img.trim().equals( "" )) {
                            myNotificationView.setImageViewBitmap( R.id.notification_large_icon, getLargeIcon( context, msg ) );
                        } else {
                            myNotificationView.setImageViewResource( R.id.notification_large_icon, R.mipmap.app_icon );
                        }

                        myNotificationView.setImageViewResource( R.id.notification_small_icon, R.mipmap.app_icon );
                        builder.setCustomContentView( myNotificationView )
                                .setSmallIcon( R.mipmap.app_icon )
                                .setTicker( msg.ticker )
                                .setAutoCancel( true );

                        return builder.build();

                    }else {
                        Notification.Builder builder = new Notification.Builder(context);
                        if(sound == 1){
                            ///使用默认的声音
                            builder.setDefaults(  Notification.DEFAULT_SOUND);
                        }
                        if(shake == 1){
                            ////使用默认的震动
                            builder.setDefaults(  Notification.DEFAULT_VIBRATE);
                        }
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        if(img != null && !img.trim().equals( "" )){
                            myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        }else {
                            myNotificationView.setImageViewResource(R.id.notification_large_icon, R.mipmap.app_icon);
                        }
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, R.mipmap.app_icon);
                        builder.setContent(myNotificationView)
                                .setSmallIcon(R.mipmap.app_icon)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.build();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                        return super.getNotification(context, msg);
                }

            }


        };

        mPushAgent.setMessageHandler(messageHandler);


        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {


            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
                pushOpen(msg.custom);
                Log.e(TAG,"launchApp: " + msg.custom);
            }
            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
                pushOpen(msg.custom);
                Log.e(TAG,"openUrl: " + msg.custom);
            }
            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
                pushOpen(msg.custom);

                Log.e(TAG,"openActivity: " + msg.custom);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                pushOpen(msg.custom);
                Log.e(TAG,"dealWithCustomAction: " + msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);



    }


    public void pushOpen(String custom) {


        if(custom != null ){

            //足球比分或赛事详情页：football#bifen/saishi#25811
            //篮球比分或赛事详情页：basketball#bifen/saishi#25811

            String[] array = custom.split( "\\/" );

            Intent mIntent = new Intent();
            mIntent.setClass( getApplicationContext(), MainActivity.class );

            if(array.length > 0){
                if(array[0].equals( "football#bifen" )){
                    if(array.length > 1 ){
                        String[] idArray = array[1].split( "#" );
                        mIntent.putExtra( MainActivity.BALL_TYPE,  0);
                        mIntent.putExtra( MainActivity.BALL_ID,  idArray[1] );
                    }

                }else if(array[0].equals( "basketball#bifen" )){

                    if(array.length > 1){
                        String[] idArray = array[1].split( "#" );
                        mIntent.putExtra( MainActivity.BALL_TYPE,  1);
                        mIntent.putExtra( MainActivity.BALL_ID,  idArray[1] );
                    }

                }
            }




            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);



            try {

                JSONObject body = new JSONObject(custom);

                int action_type = body.optInt("action_type");
                String action_id = body.optString("action_id");
                JSONObject recall_extend = body.optJSONObject( "recall_extend" );
                System.out.println("recall_extend: " + recall_extend);

                String title = StringEscapeUtils.HTMLDecode(body.optString("title"));
                // 通知文字描述
                String text = StringEscapeUtils.HTMLDecode(body.optString("text"));
                String url = StringEscapeUtils.HTMLDecode(body.optString("url"));



            } catch (JSONException e) {
                e.printStackTrace();
            }




        }


    }



    public static MatchApplication getInstance() {
        if ( instance == null )
            instance = new MatchApplication();
        return instance;
    }

    private void initImageLoader(Context context) {
        // TODO Auto-generated method stub
        // 创建DisplayImageOptions对象
        DisplayImageOptions defaulOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height
                .threadPoolSize(3)// 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) // 降低线程的优先级保证主UI线程不受太大影响
                .denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(5 * 1024 * 1024)) // 建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(5 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) // 缓存的文件数量
                .defaultDisplayImageOptions(defaulOptions)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);

    }

}
