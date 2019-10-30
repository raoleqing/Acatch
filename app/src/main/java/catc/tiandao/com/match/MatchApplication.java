package catc.tiandao.com.match;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.utils.DeviceUtils;

public class MatchApplication extends Application {

    private static MatchApplication instance;

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
        // 缓存图片的配置，一般通用的配置
        initImageLoader(getApplicationContext());

        /**
         * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
         * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
         * UMConfigure.init调用中appkey和channel参数请置为null）。
         */
        //友盟统计初始化
        String channel = DeviceUtils.getAppMetaData(this,"UMENG_CHANNEL");
        UMConfigure.init(this, Constant.UMENG_APP_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE,null);
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
        // 创建ImageLoaderConfiguration对象
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaulOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder( QueueProcessingType.LIFO).build();
        // ImageLoader对象的配置
        ImageLoader.getInstance().init(configuration);
    }

}
