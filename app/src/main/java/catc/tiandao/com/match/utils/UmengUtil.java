package catc.tiandao.com.match.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 友盟分享工具类,为了方便项目中后期如果更换或者升级分享库时,
 * 不需要每个页面改动,所以将分享封装成工具类,提供方法供页面调用
 * <p>
 * Created by yangtufa on 2017/4/21.
 */
public class UmengUtil {

    /**
     * 单个平台分享,用于自定义分享Ui时调用
     *
     * @param activity         页面Activity
     * @param platform         分享平台
     * @param shareUrl         分享地址
     * @param shareTitle       分享标题
     * @param logoUrl          logoUrl
     * @param shareDescription 分享描述
     */
    public static void shareSinglePlatform(Activity activity, SHARE_MEDIA platform, String shareUrl, String shareTitle, String logoUrl, String shareDescription) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(activity, logoUrl));
        web.setDescription(shareDescription);//描述
        new ShareAction(activity).withMedia(web).setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        String errorMsg = throwable.getMessage();
                        if (errorMsg != null && errorMsg.contains("错误码：2008")) {
                            Toast.makeText(activity, "请确认是否安装了该应用", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "分享错误", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "取消分享", Toast.LENGTH_LONG).show();
                    }
                }).share();
    }

    public static void shareSinglePlatform(Activity activity, SHARE_MEDIA platform, String shareUrl, String shareTitle, int logoImageRes, String shareDescription) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(activity, logoImageRes));
        web.setDescription(shareDescription);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(platform)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Toast.makeText(activity, "分享错误", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "取消分享", Toast.LENGTH_LONG).show();
                    }
                }).share();
    }

    /***
     * 三大平台分享
     * @param activity 页面Activity
     * @param shareUrl 分享地址
     * @param shareTitle 分享标题
     * @param logoUrl logoUrl
     * @param shareDescription 分享描述
     */
    public static void shareAllPlatform(Activity activity, String shareUrl, String shareTitle, String logoUrl, String shareDescription) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);//标题
        web.setThumb(new UMImage(activity, logoUrl));
        web.setDescription(shareDescription);//描述
        new ShareAction(activity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "分享成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        Toast.makeText(activity, "分享错误", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        Toast.makeText(activity, "取消分享", Toast.LENGTH_LONG).show();
                    }
                }).open();
    }

    /**
     * 分享回调
     * 注意:调用分享api,必须在 {@link Activity#onActivityResult(int, int, Intent)}中回调此方法
     * 否则可能会导致分享出错,如果是在 {@link Fragment}中执行此分享,则不可在Fragment中回调,需要在其Activity中
     * 回调此方法
     *
     * @param activity    activity
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        intent
     */
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }


    /***
     * 友盟统计自定义事件
     * @param context 上下文对象
     * @param eventId
     */
    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }
}
