package catc.tiandao.com.match.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.UserBen;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.my.LoginActivity;

public class UserUtils {

    public static final String IS_LOGIN = "isLogin";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String LOGIN_TYPE = "loginType";
    public static final String TOKEN = "token";
    public static final String NICKNAME = "nickName";
    public static final String ICONURL = "iconUrl";
    public static final String SEX = "sex";
    public static final String U_ID = "uid";



    /**
     * 保存用户资料
     *
     * private String token;
     *     private String nickName;
     *     private String iconUrl;
     **/
    public static void sarvUserInfo(Context context, UserBen mUserBen) {
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getDefaultSharedPreferences(context);

        sharedPreferences.edit()
                .putString(TOKEN, mUserBen.getToken())
                .putString(NICKNAME, mUserBen.getNickName())
                .putString(ICONURL, mUserBen.getIconUrl())
                .putBoolean(IS_LOGIN, true)
                .commit();
    }


    /**
     * 获取资料
     **/
    public static UserBen getUserInfo(Context context) {

        UserBen mUserBen = new UserBen();
        SharedPreferences sharedPreferences = SharedPreferencesUtil.getDefaultSharedPreferences(context);
        mUserBen.setToken( sharedPreferences.getString(PHONE,"" ));
        mUserBen.setToken( sharedPreferences.getString(TOKEN,"" ));
        mUserBen.setNickName( sharedPreferences.getString(NICKNAME,"" ));
        mUserBen.setIconUrl( sharedPreferences.getString(ICONURL,"" ));
        mUserBen.setSex( sharedPreferences.getString(SEX,"" ) );
        return mUserBen;
    }

    /**
     * 查询是否登陆
     **/
    public static boolean isLanded(Context context) {
        boolean isLogin = SharedPreferencesUtil.getBoolean(context,IS_LOGIN,false);
        return isLogin;
    }


    /**
     * 登陆
     **/
    public static void startLongin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition( R.anim.push_left_in, R.anim.day_push_left_out);
    }

    /**
     * 去登陆
     **/
    public static void startChooseLanding(final Context context) {
        // TODO Auto-generated method stub

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您还没有登录，是否进行登录？!").setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition( R.anim.push_left_in, R.anim.day_push_left_out);
                    }
                }).setNegativeButton("取消", null).show();

    }



    /**
     * 拿 到用户 MemberId
     **/
    public static String getToken(Context context){


        String token = SharedPreferencesUtil.getString(context,TOKEN);
        if(token == null || token.equals("")){
            token = "0";
        }

        System.out.println( "token: " + token );
        return token;
    }



    /**
     * 拿 到用户 头像
     **/
    public static String getUserAvatar(Context context) {
        return SharedPreferencesUtil.getString(context,ICONURL);

    }


    public static void SignOut(Context mContext) {

        SharedPreferencesUtil.putBoolean( mContext,IS_LOGIN,false );
    }

    //6.F前锋 M中锋 D后卫 G守门员,其他为未知
    public static String getPosition(String position) {

        if(position.equals( "F" )){
            return "前锋";
        }else if(position.equals( "M" )){
            return "中锋";
        }else if(position.equals( "D" )){
            return "后卫";
        }else if(position.equals( " G" )){
            return "守门员";
        }

        return "";


    }
}
