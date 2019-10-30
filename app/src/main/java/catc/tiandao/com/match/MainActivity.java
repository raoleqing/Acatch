package catc.tiandao.com.match;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import catc.tiandao.com.match.ben.UserBen;
import catc.tiandao.com.match.common.CheckNet;
import catc.tiandao.com.match.common.Constant;
import catc.tiandao.com.match.common.OnFragmentInteractionListener;
import catc.tiandao.com.match.common.SharedPreferencesUtil;
import catc.tiandao.com.match.my.LoginActivity;
import catc.tiandao.com.match.my.MyFragment;
import catc.tiandao.com.match.score.ScoreFragment;
import catc.tiandao.com.match.ui.MainFragment;
import catc.tiandao.com.match.utils.DES;
import catc.tiandao.com.match.utils.DeviceUtils;
import catc.tiandao.com.match.utils.UserUtils;
import catc.tiandao.com.match.utils.ViewUtls;
import catc.tiandao.com.match.webservice.HttpUtil;
import catc.tiandao.com.match.webservice.ThreadPoolManager;


public class MainActivity extends BaseActivity implements View.OnClickListener , OnFragmentInteractionListener {

    private static final int REQUEST_CODE = 123;

    private LinearLayout main_host_layout01,main_host_layout02,main_host_layout03;
    private ImageView main_host_image01,main_host_image02,main_host_image03;
    private TextView main_host_text01,main_host_text02,main_host_text03;

    private Fragment fragment01;
    private Fragment fragment02;
    private Fragment fragment03;
    private Fragment mContent;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int onPosition = 1;

    private GetAppLocalTokenRun getTetKeyRun;
    private AppLoginOnRun getTokenRun;
    private LoginOnRun run;
    private QuicklyLoginOnRun quicklyRun;

    private  String[] mPermissionList = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean isGetData;


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x001:
                    Bundle bundle1 = msg.getData();
                    String result1 = bundle1.getString("result");
                    getKeyParseData(result1);
                    break;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitleVisibility( View.GONE );
        setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
        setStatusBarMode(true);

        viewInfo();
        ContentInfo();

        boolean isOPen = SharedPreferencesUtil.getBoolean( this,SharedPreferencesUtil.OPEN_PERMISSIONS,false );
        if(!isOPen){
            if(Build.VERSION.SDK_INT>=23){
                ActivityCompat.requestPermissions(this,mPermissionList,REQUEST_CODE);
            }
        }



        getData();
    }

    private void getData() {

        String userKey = SharedPreferencesUtil.getString( this,SharedPreferencesUtil.USER_KEY );
        if(userKey == null || userKey.equals( "" )){
            GetAppLocalToken();
        }else {
            if(UserUtils.isLanded( this )){
                String loginType  = SharedPreferencesUtil.getString(MainActivity.this, UserUtils.LOGIN_TYPE);
                if(loginType.equals( "phone" )){
                    LoginOn();
                }else {
                    String uid = SharedPreferencesUtil.getString( MainActivity.this,UserUtils.U_ID);
                    QuicklyLoginOn(loginType,uid);
                }
            }else {
                AppLoginOn(userKey);

            }
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume()： ==============");
    }

    private void viewInfo() {

        manager = getSupportFragmentManager();

        main_host_layout01 = ViewUtls.find( this,R.id.main_host_layout01 );
        main_host_layout02 = ViewUtls.find( this,R.id.main_host_layout02 );
        main_host_layout03 = ViewUtls.find( this,R.id.main_host_layout03 );
        main_host_image01 = ViewUtls.find( this,R.id.main_host_image01 );
        main_host_image02 = ViewUtls.find( this,R.id.main_host_image02 );
        main_host_image03 = ViewUtls.find( this,R.id.main_host_image03 );
        main_host_text01 = ViewUtls.find( this,R.id.main_host_text01 );
        main_host_text02 = ViewUtls.find( this,R.id.main_host_text02 );
        main_host_text03 = ViewUtls.find( this,R.id.main_host_text03 );

        main_host_layout01.setOnClickListener( this );
        main_host_layout02.setOnClickListener( this );
        main_host_layout03.setOnClickListener( this );

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.main_host_layout01:
                if(onPosition != 0){
                    setContontView(0);
                }
                break;
            case R.id.main_host_layout02:
                if(onPosition != 1){
                    setContontView(1);
                }
                break;
            case R.id.main_host_layout03:
                if(onPosition != 2){
                    setContontView(2);
                }
                break;

        }

    }




    /*
     * 修改中间的内容 *
     */
    private void setContontView(int i) {
        onPosition = i;
        // TODO Auto-generated method stub
        transaction = manager.beginTransaction();

        int color01 = ContextCompat.getColor(MainActivity.this,R.color.text1);
        int color02 = ContextCompat.getColor(MainActivity.this,R.color.text2);

        switch (i) {
            case 0:
                setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
                setStatusBarMode(true);

                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home_default );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me_default );
                main_host_text01.setTextColor( color01 );
                main_host_text02.setTextColor( color02 );
                main_host_text03.setTextColor( color02 );

                if (fragment01 == null) {
                    fragment01 = ScoreFragment.newInstance("","");
                }


                switchContent(fragment01, "fragment01");
                break;
            case 1:

                setStatusBarColor( ContextCompat.getColor(this, R.color.white ));
                setStatusBarMode(true);
                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match_default );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me_default );
                main_host_text01.setTextColor( color02 );
                main_host_text02.setTextColor( color01 );
                main_host_text03.setTextColor( color02 );

                if (fragment02 == null) {
                    fragment02 =  MainFragment.newInstance("","");
                }

                switchContent(fragment02, "fragment02");
                break;
            case 2:

                setTranslucentStatus(  );

                main_host_image01.setImageResource( R.mipmap.tabbar_icon_match_default );
                main_host_image02.setImageResource( R.mipmap.tabbar_icon_home_default );
                main_host_image03.setImageResource( R.mipmap.tabbar_icon_me );
                main_host_text01.setTextColor( color02 );
                main_host_text02.setTextColor( color02 );
                main_host_text03.setTextColor( color01 );


                if (fragment03 == null) {
                    fragment03 =  MyFragment.newInstance("","");
                }
                switchContent(fragment03, "fragment03");
                break;
            default:
                break;
        }
    }




    private void ContentInfo() {
        if(fragment02 == null)
            fragment02 = MainFragment.newInstance("","");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, fragment02,"fragment02").show(fragment02);
        mContent = fragment02;
        transaction.commit();
    }

    /** 修改显示的内容 不会重新加载 **/
    public void switchContent(Fragment to, String tag) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(mContent).add(R.id.main_content, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mContent = to;
        }
    }



    //phoneType=手机型号&systemType=系统版本
    private void GetAppLocalToken() {

        try{

            String phone = SharedPreferencesUtil.getString( MainActivity.this, UserUtils.PHONE);
            String psw = SharedPreferencesUtil.getString( MainActivity.this,UserUtils.PASSWORD);

            if (CheckNet.isNetworkConnected( MainActivity.this)) {

                String brand = android.os.Build.BRAND;
                String model = android.os.Build.MODEL;

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phoneType",model );
                param.put( "systemType", brand);

                getTetKeyRun = new GetAppLocalTokenRun(param);
                ThreadPoolManager.getsInstance().execute(getTetKeyRun);

            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     *
     * */
    class GetAppLocalTokenRun implements Runnable{
        private HashMap<String, String> param;

        GetAppLocalTokenRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( MainActivity.this,HttpUtil.GET_APPLOCAL_TOKEN ,param,new HttpUtil.HttpUtilInterface(){

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



    private void getKeyParseData(String result) {

        if(result == null){
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            isGetData = true;

            System.out.println( "get key:" + result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            if(code == 0) {

                String data = obj.optString( "data" );
                SharedPreferencesUtil.putString( MainActivity.this,SharedPreferencesUtil.USER_KEY,data );
                if(UserUtils.isLanded( this )){

                    String loginType  = SharedPreferencesUtil.getString(MainActivity.this, UserUtils.LOGIN_TYPE);
                    if(loginType.equals( "phone" )){
                        LoginOn();
                    }else {
                        String uid = SharedPreferencesUtil.getString( MainActivity.this,UserUtils.U_ID);
                        QuicklyLoginOn(loginType,uid);

                    }

                }else {
                    AppLoginOn(data);
                }

            }



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }

    }



    //phonekey=手机key& ip=手机ip
    private void AppLoginOn(String phonekey) {

        try{

            if (CheckNet.isNetworkConnected( MainActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                //

                HashMap<String, String> param = new HashMap<>(  );
                param.put("phonekey",phonekey );
                param.put( "ip", DeviceUtils.getIPAddress(MainActivity.this) );

                getTokenRun = new AppLoginOnRun(param);
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

            HttpUtil.post( MainActivity.this,HttpUtil.APP_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

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
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            isGetData = true;

            System.out.println("获取 token: " +  result );
            JSONObject obj = new JSONObject( result );
            int code = obj.optInt( "code",0 );
            String message = obj.optString( "message" );


            // {"code":0,"message":"登录成功","data":{"sex":0,"token":"f9d8498a-ee56-4d18-8f69-215dcb0e7168","nickName":null,"iconUrl":null}}
            if(code == 0) {
                JSONObject data = obj.optJSONObject( "data" );
                String token = data.optString( "token" );
                SharedPreferencesUtil.putString( MainActivity.this,UserUtils.TOKEN, token);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }



    }




    private void QuicklyLoginOn(String loginType,String openid) {


        try{

            if (CheckNet.isNetworkConnected(MainActivity.this)) {

                //http:// 域名/LSQB/ QuicklyLoginOn?loginType=登录类型（qq/wechat）&uid=唯一编码&ip=ip

                String phoneKey = SharedPreferencesUtil.getString( MainActivity.this,SharedPreferencesUtil.USER_KEY );

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("loginType",loginType );
                param.put( "uid", openid);
                param.put( "ip",DeviceUtils.getIPAddress( MainActivity.this));

                quicklyRun = new QuicklyLoginOnRun(param);
                ThreadPoolManager.getsInstance().execute(quicklyRun);

            } else {
                Toast.makeText(MainActivity.this, "没有可用的网络连接，请检查网络设置", Toast.LENGTH_SHORT).show();
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


            HttpUtil.post( MainActivity.this,HttpUtil.QUICKLY_LOGINON ,param,new HttpUtil.HttpUtilInterface(){

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



    //name=用户名&psw=加密密码&ip=ip
    private void LoginOn() {

        try{

            String phone = SharedPreferencesUtil.getString( MainActivity.this, UserUtils.PHONE);
            String psw = SharedPreferencesUtil.getString( MainActivity.this,UserUtils.PASSWORD);

            if (CheckNet.isNetworkConnected( MainActivity.this)) {

                setProgressVisibility( View.VISIBLE );

                HashMap<String, String> param = new HashMap<>(  );
                param.put("name",phone );
                param.put( "psw", DES.encode(DES.KEY,psw));
                param.put( "ip", DeviceUtils.getIPAddress(MainActivity.this)  );

                run = new LoginOnRun(param);
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

        LoginOnRun(HashMap<String, String> param){
            this.param =param;
        }

        @Override
        public void run() {


            HttpUtil.post( MainActivity.this,HttpUtil.LOGIN_ON ,param,new HttpUtil.HttpUtilInterface(){

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
            setProgressVisibility( View.GONE );
            return;
        }


        try{

            isGetData = true;

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
                UserUtils.sarvUserInfo(MainActivity.this, mUserBen);

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            setProgressVisibility( View.GONE );
        }



    }



    @Override
    public void onFragmentInteraction(Uri uri) {

        if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_SHOW)){
            setProgressVisibility(View.VISIBLE);
        }else if(uri.toString().equals(OnFragmentInteractionListener.PROGRESS_HIDE)){
            setProgressVisibility(View.GONE);
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(run != null)
            ThreadPoolManager.getsInstance().cancel(run);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);


    }



    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            SharedPreferencesUtil.putBoolean( this,SharedPreferencesUtil.OPEN_PERMISSIONS,true );

            //发送登录广播
            EventBus.getDefault().post( Constant.APP_NET_SUCCESS);

            if(!isGetData){
                getData();
            }


            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
            } else {
                // Permission Denied
            }
        }
    }






    // 返回
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        overridePendingTransition(R.anim.day_push_right_in01, R.anim.day_push_right_out);
        MainActivity.this.finish();

    }


}
