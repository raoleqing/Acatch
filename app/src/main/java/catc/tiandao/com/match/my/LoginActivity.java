package catc.tiandao.com.match.my;

import androidx.core.content.ContextCompat;
import catc.tiandao.com.match.BaseActivity;
import catc.tiandao.com.match.MainActivity;
import catc.tiandao.com.match.R;
import catc.tiandao.com.match.ben.UserBen;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.utils.DES;
import catc.tiandao.com.match.utils.DeviceUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView close_image;
    private EditText uername,password;
    private CheckBox myCheckbox;
    private TextView protocol,registered,forget_password;
    private Button login;
    private ImageView shwo_icon,close_icon,wexin_but,qq_but;

    private int showPassworedType = 0;
    private boolean isName,isPassword;


    private LoginOnRun run;
    private RegisterByOthersRun othersRun;
    private QuicklyLoginOnRun quicklyRun;
    private String loginType;

    private String phone,paws;
    private String openid = "";
    private String profile_image_url = "";
    private String screen_name = "";
    private int sex = 1;

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
                case 0x002:
                    Bundle bundle2 = msg.getData();
                    String result2 = bundle2.getString("result");
                    otherParseData(result2);
                    break;

                default:
                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        setTitleVisibility( View.GONE );


        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);
        setTranslucentStatus();


        viewInfo();
        setContent();
        setProgressVisibility( View.GONE );
    }



    private void viewInfo() {
        close_image = ViewUtls.find( this,R.id.close_image );
        uername = ViewUtls.find( this,R.id.uername );
        password = ViewUtls.find( this,R.id.password );
        myCheckbox = ViewUtls.find( this,R.id.myCheckbox );
        protocol = ViewUtls.find( this,R.id.protocol );
        registered = ViewUtls.find( this,R.id.registered );
        forget_password = ViewUtls.find( this,R.id.forget_password );
        login = ViewUtls.find( this,R.id.login );
        shwo_icon = ViewUtls.find( this,R.id.shwo_icon );
        close_icon = ViewUtls.find( this,R.id.close_icon );
        wexin_but = ViewUtls.find( this,R.id.wexin_but );
        qq_but = ViewUtls.find( this,R.id.qq_but );


        close_image.setOnClickListener(this);
        protocol.setOnClickListener(this);
        registered.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        wexin_but.setOnClickListener(this);
        qq_but.setOnClickListener(this);
        shwo_icon.setOnClickListener(this);
        close_icon.setOnClickListener(this);
        login.setOnClickListener(this);

        //为EditText设置监听，注意监听类型为TextWatcher
        uername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(uername.getText().toString().trim().length() > 0) {
                    isName = true;
                }else {
                    isName = false;
                }
                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(password.getText().toString().trim().length() > 0) {
                    isPassword = true;
                }else {
                    isPassword = false;
                }
                setLoginBut();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void setContent() {
        String phone = SharedPreferencesUtil.getString( LoginActivity.this, UserUtils.PHONE);
        String psw = SharedPreferencesUtil.getString( LoginActivity.this,UserUtils.PASSWORD);
        uername.setText( phone );
        password.setText( psw );

    }



    private void setLoginBut() {
        if(isName && isPassword ){
            login.setBackgroundResource( R.drawable.bg_search_normal11_host );
            login.setClickable( true );
        }else {
            login.setBackgroundResource( R.drawable.bg_search_normal11 );
            login.setClickable( false );
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.close_image:
                LoginActivity.this.onBackPressed();
                break;

            case R.id.protocol:
                //协义

                break;
            case R.id.login:

                loginType = "phone";
                getContent();

                break;
            case R.id.registered:

                Intent intent01 = new Intent(LoginActivity.this, RegisteredActivity.class);
                startActivity(intent01);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.forget_password:

                Intent intent02 = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent02);
                overridePendingTransition(R.anim.push_left_in, R.anim.day_push_left_out);

                break;
            case R.id.wexin_but:

                loginType = "wechat";
                UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN.toSnsPlatform().mPlatform, authListener);

                break;
            case R.id.qq_but:
                loginType = "qq";
                UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ.toSnsPlatform().mPlatform, authListener);
                break;
            case R.id.shwo_icon:

                if(showPassworedType == 0){
                    showPassworedType = 1;
                    shwo_icon.setBackgroundResource( R.mipmap.icon_password_display );
                    password.setInputType(0x90);//不可见
                }else {
                    shwo_icon.setBackgroundResource( R.mipmap.icon_password_hidden );
                    showPassworedType = 0;
                    password.setInputType(0x81);//可见
                }

                break;
            case R.id.close_icon:

                password.setText( "" );

                break;

        }

    }


    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            Toast.makeText(LoginActivity.this, "授权成功了", Toast.LENGTH_LONG).show();

            //获取用户授权后的信息
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            Set<String> strings = data.keySet();
            for (String string:strings){

                if (string.equals("iconurl")){
                    profile_image_url = data.get("iconurl");
                }
                if (string.equals("name")){
                    screen_name = data.get("name");
                }



            }

            openid = data.get( "openid" );
            RegisterByOthers(openid,screen_name,sex);

        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(LoginActivity.this, "授权失败：" + t.getMessage(),  Toast.LENGTH_LONG).show();
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(LoginActivity.this, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };



    private void getContent() {

        if(isName && isPassword ){

            phone = uername.getText().toString();
            if(phone == null || phone.isEmpty()){
                Toast.makeText( LoginActivity.this,"请输入手机号码",Toast.LENGTH_SHORT ).show();
                return ;
            }

            paws = password.getText().toString();

            if(paws == null || paws.isEmpty()){
                Toast.makeText( LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT ).show();
                return ;
            }

            LoginOn(phone,paws);


        }

    }


    //name=用户名&psw=加密密码&ip=ip
    private void LoginOn(String name,String psw) {

        try{

            if (CheckNet.isNetworkConnected(LoginActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("name",name );
                param.put( "psw", DES.encode(DES.KEY,psw));
                param.put( "ip", DeviceUtils.getIPAddress( LoginActivity.this) );

                run = new LoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(run);

            } else {
                Toast.makeText(LoginActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
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

        LoginOnRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( LoginActivity.this,HttpUtil.LOGIN_ON ,param,new HttpUtil.HttpUtilInterface(){

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


           //  {"code":0,"message":"登录成功","data":{"userId":104,"sex":1,"token":"1b4fb49e-b01a-4133-a1a1-065ed4047fc2","nickName":"13873146635","iconUrl":null}}
            //2019-10-30 10:53:18.851 32377-32377/catc.tiandao.com.match I/System.out:

            if(code == 0) {

                JSONObject data = obj.optJSONObject( "data" );


                Gson gson = new Gson();
                UserBen mUserBen = gson.fromJson(data.toString(), UserBen.class);

                //存储登录的账号与密码
                if (loginType.equals("phone")) {
                    SharedPreferencesUtil.putString( LoginActivity.this,UserUtils.PHONE,phone );
                    SharedPreferencesUtil.putString( LoginActivity.this,UserUtils.PASSWORD,paws);
                }

                // 记录登录类型
                SharedPreferencesUtil.putString(LoginActivity.this, UserUtils.LOGIN_TYPE, loginType);
                UserUtils.sarvUserInfo(LoginActivity.this, mUserBen);


                //发送登录广播
                EventBus.getDefault().post( Constant.LOGIN_SUCCESS);

                LoginActivity.this.onBackPressed();

            }

            Toast.makeText( LoginActivity.this,message ,Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }
    }




    private void RegisterByOthers(String uid,String name,int sex) {

        try{

            if (CheckNet.isNetworkConnected(LoginActivity.this)) {

                //http:// 域名/LSQB/ RegisterByOthers? phoneKey=手机key&name=昵称&sex=性别(1男0女)&loginType=登录类型（qq/wechat）&uid=唯一编码（qq是uid,wechat是unionId）

                String phoneKey = SharedPreferencesUtil.getString( LoginActivity.this,SharedPreferencesUtil.USER_KEY );

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phoneKey",phoneKey );
                param.put( "name", name);
                param.put( "sex", sex + "");
                param.put( "loginType", loginType);

                othersRun = new RegisterByOthersRun(param);
                ThreadPoolManager.getsInstance().execute(othersRun);

            } else {
                Toast.makeText(LoginActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class RegisterByOthersRun implements Runnable{
        private HashMap<String, String> param;

        RegisterByOthersRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( LoginActivity.this,HttpUtil.REGISTER_BY_OTHERS ,param,new HttpUtil.HttpUtilInterface(){

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


    private void otherParseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            System.out.println( result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );
            //loginType=登录类型（qq/wechat）&uid
            if(code == 0) {

                SharedPreferencesUtil.putString( LoginActivity.this,UserUtils.LOGIN_TYPE,loginType );
                SharedPreferencesUtil.putString( LoginActivity.this,UserUtils.U_ID,openid);


                QuicklyLoginOn(loginType,openid);

            }

            Toast.makeText( LoginActivity.this,message ,Toast.LENGTH_SHORT).show();


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }
    }

    private void QuicklyLoginOn(String loginType,String openid) {


        try{

            if (CheckNet.isNetworkConnected(LoginActivity.this)) {

                //http:// 域名/LSQB/ QuicklyLoginOn?loginType=登录类型（qq/wechat）&uid=唯一编码&ip=ip

                String phoneKey = SharedPreferencesUtil.getString( LoginActivity.this,SharedPreferencesUtil.USER_KEY );

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("loginType",loginType );
                param.put( "uid", openid);
                param.put( "ip",DeviceUtils.getIPAddress( LoginActivity.this));

                quicklyRun = new QuicklyLoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(quicklyRun);

            } else {
                Toast.makeText(LoginActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
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


            HttpUtil.post( LoginActivity.this,HttpUtil.QUICKLY_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

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





    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(run != null)
            ThreadPoolManager.getsInstance().cancel(run);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }




    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        LoginActivity.this.finish();
    }



}
