package catc.tiandao.com.match.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import catc.tiandao.com.match.MainActivity;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import catc.tiandao.com.matchlibrary.CheckNet;
import catc.tiandao.com.matchlibrary.ben.UserBen;

public class GetTokenUtils {
    private Context mContext;
    private GetTokenUtilInterface mInterface;

    public GetTokenUtils(Context mContext){
        this.mContext = mContext;
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x002:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    getTokenParseData(result2);
                    break;
                case 0x003:
                    Bundle bundle3 = msg.getData();
                    String result3 = bundle3.getString("result");
                    parseData(result3);
                    break;
                default:
                    break;
            }
        }


    };





    /**
     *重新获取token
     * **/
    public void getToken(GetTokenUtilInterface mInterface){
        this.mInterface = mInterface;
        String userKey = SharedPreferencesUtil.getString( mContext,SharedPreferencesUtil.USER_KEY );
        if(UserUtils.isLanded( mContext )){
            String loginType  = SharedPreferencesUtil.getString( mContext, UserUtils.LOGIN_TYPE);
            if(loginType.equals( "phone" )){
                LoginOn(mContext);
            }else {
                String uid = SharedPreferencesUtil.getString( mContext,UserUtils.U_ID);
                QuicklyLoginOn(loginType,uid);
            }
        }else {
            AppLoginOn(userKey);

        }
    }


    //phonekey=手机key& ip=手机ip
    private void AppLoginOn(String phonekey) {

        try{

            if (CheckNet.isNetworkConnected( mContext)) {

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phonekey",phonekey );
                param.put( "ip", DeviceUtils.getIPAddress(mContext) );

                AppLoginOnRun getTokenRun = new AppLoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(getTokenRun);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class AppLoginOnRun implements Runnable{
        private HashMap<String, String> param;

        AppLoginOnRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {

            HttpUtil.post( mContext,HttpUtil.APP_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

                @Override
                public void onResponse(String result) {

                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString( "result", result );
                    message.setData( data );
                    message.what = 0x002;
                    myHandler.sendMessage( message );

                }
            });

        }
    }



    private void getTokenParseData(String result) {

        if(result == null){
            return;
        }


        try{

            System.out.println("获取 token: " +  result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            // {"code":0,"message":"登录成功","data":{"sex":0,"token":"f9d8498a-ee56-4d18-8f69-215dcb0e7168","nickName":null,"iconUrl":null}}
            if(code == 0) {
                JSONObject data = obj.optJSONObject( "data" );
                String token = data.optString( "token" );
                SharedPreferencesUtil.putString( mContext,UserUtils.TOKEN, token);

            }


            mInterface.onResponse();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private void QuicklyLoginOn(String loginType,String openid) {


        try{

            if (CheckNet.isNetworkConnected(mContext)) {

                //http:// 域名/LSQB/ QuicklyLoginOn?loginType=登录类型（qq/wechat）&uid=唯一编码&ip=ip

                String phoneKey = SharedPreferencesUtil.getString( mContext,SharedPreferencesUtil.USER_KEY );


                HashMap<String, String> param = new HashMap<>(  );
                param.put("loginType",loginType );
                param.put( "uid", openid);
                param.put( "phoneKey", phoneKey);
                param.put( "ip",DeviceUtils.getIPAddress( mContext));

                QuicklyLoginOnRun quicklyRun = new QuicklyLoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(quicklyRun);

            } else {
                Toast.makeText(mContext, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }



    /**
     *
     * */
    class QuicklyLoginOnRun implements Runnable{
        private HashMap<String, String> param;

        QuicklyLoginOnRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( mContext,HttpUtil.QUICKLY_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

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



    /**
     * 登录
     * **/
    private void LoginOn(Context mContext) {

        try{

            String phone = SharedPreferencesUtil.getString( mContext, UserUtils.PHONE);
            String psw = SharedPreferencesUtil.getString( mContext,UserUtils.PASSWORD);
            String phoneKey  = SharedPreferencesUtil.getString( mContext,SharedPreferencesUtil.USER_KEY );

            if (CheckNet.isNetworkConnected( mContext)) {


                HashMap<String, String> param = new HashMap<>(  );
                param.put("name",phone );
                param.put( "psw", DES.encode(DES.KEY,psw));
                param.put( "phoneKey", phoneKey);
                param.put( "ip", DeviceUtils.getIPAddress(mContext)  );

                LoginOnRun run = new LoginOnRun(mContext,param);
                ThreadPoolManager.getsInstance().execute(run);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class LoginOnRun implements Runnable{
        private HashMap<String, String> param;
        private Context mContext;

        LoginOnRun(Context mContext,HashMap<String, String> param){
            this.mContext = mContext;
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( mContext,HttpUtil.LOGIN_ON ,param,new HttpUtil.HttpUtilInterface(){

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



    private void parseData(String result) {

        if(result == null){
            return;
        }


        try{

            System.out.println( " 登录： " + result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            // {"code":0,"message":"登录成功","data":{"token":"2dac2028-526c-40d9-9546-3547d298ba5c","nickName":"13006884459","iconUrl":null}}

            if(code == 0) {
                JSONObject data = obj.optJSONObject( "data" );
                Gson gson = new Gson();
                UserBen mUserBen = gson.fromJson(data.toString(), UserBen.class);

                // 记录登录类型
                UserUtils.sarvUserInfo( mContext, mUserBen);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public interface GetTokenUtilInterface{
        void onResponse();
    }


}
